package com.ilham1012.ecgbpi.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bitalino.comm.BITalinoDevice;
import com.bitalino.comm.BITalinoFrame;
import com.ilham1012.ecgbpi.POJO.EcgRecord;
import com.ilham1012.ecgbpi.POJO.EcgRecordService;
import com.ilham1012.ecgbpi.R;
import com.ilham1012.ecgbpi.helper.SQLiteHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import retrofit.RestAdapter;
import retrofit.client.Response;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class RecordActivity extends RoboActivity {

    private static final String TAG = "MainActivity";
    private static final String API_BASE_URL = "http://192.168.2.131:8888/test_api/api";
    private static final boolean UPLOAD = true;
    private final static int [] selChannels = {2};
    /*
     * http://developer.android.com/reference/android/bluetooth/BluetoothDevice.html
     * #createRfcommSocketToServiceRecord(java.util.UUID)
     *
     * "Hint: If you are connecting to a Bluetooth serial board then try using the
     * well-known SPP UUID 00001101-0000-1000-8000-00805F9B34FB. However if you
     * are connecting to an Android peer then please generate your own unique
     * UUID."
     */
    private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    @InjectView(R.id.log)
    private TextView tvLog;
    private boolean testInitiated = false;
    private Button startBtn;
    private Button stopBtn;
    private EcgRecord ecgRecord;
    private SQLiteHandler db;
    private TestAsyncTask testAsyncTask;
    private Long tsLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        ecgRecord = new EcgRecord();

        ecgRecord.setUserId(1);

        Bundle extras = getIntent().getExtras();
        ecgRecord.setRecordingName(extras.getString("recording_name"));
        ecgRecord.setFileUrl(ecgRecord.getRecordingName() + ".txt");

        this.setTitle(ecgRecord.getRecordingName());

        db = new SQLiteHandler(getBaseContext());

        startBtn = (Button) findViewById(R.id.btnStartRecord);
        stopBtn = (Button) findViewById(R.id.btnStopRecord);

        stopBtn.setClickable(false);
        stopBtn.setActivated(false);

        startBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setClickable(false);
                startBtn.setActivated(false);
                stopBtn.setClickable(true);
                stopBtn.setActivated(true);
                startRecording();
            }
        });

        stopBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });


    }



    private void startRecording() {

        // execute
        if (!testInitiated){
            testAsyncTask = new TestAsyncTask();
            testAsyncTask.execute();
        }

        tsLong = System.currentTimeMillis()/1000;
        ecgRecord.setRecordingTime(tsLong.toString());

        Toast.makeText(getApplicationContext(),
                "Record start at " + ecgRecord.getRecordingTime(), Toast.LENGTH_LONG).show();
    }

    private void stopRecording() {

        testAsyncTask.stopTask();

        db.addEcgRecord(ecgRecord.getUserId(), ecgRecord.getRecordingTime(), ecgRecord.getRecordingName(), ecgRecord.getFileUrl());
        db.close();

        Toast.makeText(getApplicationContext(),
                "Record " + ecgRecord.getRecordingName() + " has been saved", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(RecordActivity.this, DashboardNewActivity.class);
        startActivity(intent);

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private class TestAsyncTask extends AsyncTask<Void, String, Void> {
        private TextView tvLog = (TextView) findViewById(R.id.log);
        private BluetoothDevice dev = null;
        private BluetoothSocket sock = null;
        private InputStream is = null;
        private OutputStream os = null;
        private BITalinoDevice bitalino;

        @Override
        protected Void doInBackground(Void... paramses) {
            try {
                // Let's get the remote Bluetooth device
                final String remoteDevice = "98:D3:31:90:3E:00";

                final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                dev = btAdapter.getRemoteDevice(remoteDevice);

                if (UPLOAD) {
                    // instantiate reading service client
                    RestAdapter restAdapter2 = new RestAdapter.Builder()
                            .setEndpoint(API_BASE_URL)
                            .build();
                    EcgRecordService service2 = restAdapter2.create(EcgRecordService.class);

                    // upload reading
                    Response response2 = service2.uploadReading(ecgRecord);
                    Log.e(TAG, "Response2 : " + response2.getBody());
                    assert response2.getStatus() == 200;
                }

                /*
                 * Establish Bluetooth connection
                 *
                 * Because discovery is a heavyweight procedure for the Bluetooth adapter,
                 * this method should always be called before attempting to connect to a
                 * remote device with connect(). Discovery is not managed by the Activity,
                 * but is run as a system service, so an application should always call
                 * cancel discovery even if it did not directly request a discovery, just to
                 * be sure. If Bluetooth state is not STATE_ON, this API will return false.
                 *
                 * see
                 * http://developer.android.com/reference/android/bluetooth/BluetoothAdapter
                 * .html#cancelDiscovery()
                 */
                Log.d(TAG, "Stopping Bluetooth discovery.");
                btAdapter.cancelDiscovery();

                sock = dev.createRfcommSocketToServiceRecord(MY_UUID);
                sock.connect();
                testInitiated = true;


                bitalino = new BITalinoDevice(1000, selChannels); // new int[]{0, 1, 2, 3, 4, 5});
                publishProgress("Connecting to BITalino [" + remoteDevice + "]..");
                bitalino.open(sock.getInputStream(), sock.getOutputStream());
                publishProgress("Connected.");

                // get BITalino version
                publishProgress("Version: " + bitalino.version());

                // start acquisition on predefined analog channels
                bitalino.start();

                // read until task is stopped
                int counter = 0;
                while (counter < 100) {
                    final int numberOfSamplesToRead = 1000;
                    publishProgress("Reading " + numberOfSamplesToRead + " samples..");
                    BITalinoFrame[] frames = bitalino.read(numberOfSamplesToRead);

                    if (UPLOAD) {
                        // prepare reading for upload
//                        BITalinoReading reading = new BITalinoReading();
//                        reading.setTimestamp(System.currentTimeMillis()/1000);
//                        String channelEkg = "";
//
//                        for (BITalinoFrame frame : frames){
//                            channelEkg = channelEkg + frame.getAnalog(selChannels[0]) + ", ";
//                        }
//
//                        reading.setFrameString(channelEkg);

//                        reading.setFrames(frames);
                        // instantiate reading service client
//                        RestAdapter restAdapter = new RestAdapter.Builder()
//                                .setEndpoint("http://192.168.2.131:8888/test_api/api/bitalino")
//                                .build();
//                        ReadingService service = restAdapter.create(ReadingService.class);
//
//
//
//                        // upload reading
//                        Response response = service.uploadReading(reading);
//                        assert response.getStatus() == 200;
                    }

                    // present data in screen
                    for (BITalinoFrame frame : frames)
                        publishProgress(frame.toString());

                    counter++;
                }



                // trigger digital outputs
                // int[] digital = { 1, 1, 1, 1 };
                // device.trigger(digital);
            } catch (Exception e) {
                Log.e(TAG, "There was an error.", e);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            tvLog.append("\n".concat(values[0]));
        }

        @Override
        protected void onCancelled() {
            stopTask();
        }

        public void stopTask(){
            // stop acquisition and close bluetooth connection
            try {
                bitalino.stop();
//                publishProgress("BITalino is stopped");

                sock.close();
//                publishProgress("And we're done! :-)");
            } catch (Exception e) {
                Log.e(TAG, "There was an error.", e);
            }
        }

    }

}
