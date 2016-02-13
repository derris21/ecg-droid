package com.ilham1012.ecgbpi.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ilham1012.ecgbpi.R;
import com.ilham1012.ecgbpi.helper.SQLiteHandler;
import com.ilham1012.ecgbpi.helper.SessionManager;

public class DashboardNewActivity extends AppCompatActivity {
    private SQLiteHandler db;
    private SessionManager session;
    private ListView listView;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.ecgRecordListView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(DashboardNewActivity.this,
                        RecordActivity.class);
                startActivity(intent);
            }
        });

        // Sqlite db handler
        db = new SQLiteHandler(getApplicationContext());
//        db.initializeSampleData();

        // Session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()){
            logoutUser();
        }

        displayListView();

    }



    private void displayListView(){
        Cursor cursor = db.fetchAllEcgRecords();

        String columns[] = new String[]{
                "recording_name",
                "recording_time"
        };

        int[] toList = new int[]{
            R.id.list_item_title,
            R.id.list_item_subtitle
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.list_item,
                cursor,
                columns,
                toList,
                0);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String clickedName =
                        cursor.getString(cursor.getColumnIndexOrThrow("recording_name"));
                Toast.makeText(getApplicationContext(),
                        clickedName, Toast.LENGTH_SHORT).show();

            }
        });
    }






    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared preferences
     * Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUserTable();

        // Launching the login activity
        Intent intent = new Intent(DashboardNewActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
//                gotoSetting();
                return true;
            case R.id.logout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
