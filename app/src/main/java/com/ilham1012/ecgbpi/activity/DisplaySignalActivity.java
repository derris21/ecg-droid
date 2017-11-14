package com.ilham1012.ecgbpi.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.ilham1012.ecgbpi.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DisplaySignalActivity extends AppCompatActivity {
    LineChart rawChart;
    String recordingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_signal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        recordingName = extras.getString("recording_name");

        rawChart = (LineChart) findViewById(R.id.rawChart);
        plot();
    }


    public void plot() {
        JSONArray jArray;
        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File (root.getAbsolutePath() + "/ecgbpi/ecgrecord/");
            File yourFile = new File(dir, recordingName + ".json");
            FileInputStream stream = new FileInputStream(yourFile);
            String jString = null;
//            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                /* Instead of using default, pass in a decoder. */
                jString = Charset.defaultCharset().decode(bb).toString();
//            }
//            finally {
                stream.close();


                jArray = new JSONArray(jString);
                List<Entry> entries = new ArrayList<>();

                if (jArray != null){
                    for (int i=0; i < jArray.length(); i++){
                        entries.add(new Entry(i, (float) jArray.getDouble(i)));
                    }
                }

                LineDataSet dataSet = new LineDataSet(entries, "ECG");
                dataSet.setDrawValues(false);
                dataSet.setDrawCircles(false);
                dataSet.setColor(Color.MAGENTA);

                LineData lineData = new LineData(dataSet);
                rawChart.setData(lineData);
                rawChart.invalidate();
//            }


        } catch (Exception e) {e.printStackTrace();}
    }

}
