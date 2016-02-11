package com.ilham1012.ecgbpi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ilham1012.ecgbpi.R;
import com.ilham1012.ecgbpi.helper.SQLiteHandler;
import com.ilham1012.ecgbpi.helper.SessionManager;

import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    private TextView txtname;
    private TextView txtEmail;
//    private Button btnLogout;

    private SQLiteHandler db;
    private SessionManager session;

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
        setSupportActionBar(toolbar);

        txtname = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
//        btnLogout = (Button) findViewById(R.id.btnLogout);

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
        String email = user.get("email");

        // Displaying the user details on the screen
        txtname.setText(name);
        txtEmail.setText(email);

        // Logout button click event
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logoutUser();
//            }
//        });
    }

}
