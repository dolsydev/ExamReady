package com.example.wolf.testseries;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.CentralController.GlobalController;


public class SplashScreenController extends ActionBarActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_controller);
        checkForLogin();
    }

    public void checkForLogin()
    {
        sharedPreferences=getSharedPreferences(GlobalController.credentialsFile, Context.MODE_PRIVATE);

        if(sharedPreferences.contains("objectId"))
        {
            GlobalController.setObjectId(sharedPreferences.getString("objectId", ""));
//			Toast.makeText(this, "Logged In", Toast.LENGTH_LONG).show();
            intent = new Intent(SplashScreenController.this, Homescreen.class);
        }
        else
        {
            intent = new Intent(SplashScreenController.this, LoginController.class);
        }


        new Handler().postDelayed(new Runnable() {

	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity


                startActivity(intent);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
