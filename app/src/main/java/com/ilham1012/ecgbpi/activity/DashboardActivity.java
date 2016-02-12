package com.ilham1012.ecgbpi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ilham1012.ecgbpi.R;
import com.ilham1012.ecgbpi.helper.ListAdapter;
import com.ilham1012.ecgbpi.helper.SQLiteHandler;
import com.ilham1012.ecgbpi.helper.SessionManager;

import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private SessionManager session;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fabBtn;
    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String[] listTitles;
    private String[] listSubtitles;



    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared preferences
     * Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        fabBtn = (FloatingActionButton)findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.listViewRecords);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ListAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);


        //collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        // Sqlite db handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()){
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        //String email = user.get("email");

        // Displaying the user details on the screen
        collapsingToolbarLayout.setTitle(name);



        fillListData();
//        ListAdapter adapter = new ListAdapter(getBaseContext(), listTitles, listSubtitles);

        recyclerView.setAdapter(mAdapter);


        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Create new recording", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void fillListData(){
        /**
         * Dummy Data
         */

        listTitles = new String[]{"Recording 1", "Test Recording", "Just Test", "OK, test again",
                "Recording n", "Patient Name", "What Else?", "Aigooo", "Record no 3",
                "Morning Record", "ECG BPI", "BITalino", "BioSignal", "Lab Desain", "After Squat"};

        listSubtitles = new String[] {"1/02/2016", "1/02/2016", "3/02/2016", "4/02/2016", "5/02/2016",
                "5/02/2016", "5/02/2016", "6/02/2016", "7/02/2016", "8/02/2016",
                "10/02/2016", "10/02/2016", "10/02/2016", "11/02/2016", "11/02/2016"};
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
