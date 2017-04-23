package com.example.wolf.testseries;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.CentralController.GlobalController;
import com.example.wolf.testseries.ParseModelController.SubscriptionInfo;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Arrays;


public class SignupController extends ActionBarActivity {

    EditText firstName,
            lastName,
            emailAddress,
            username,
            password,
            confirmPassword;
    Button signupButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_controller);
        // Enable Local Datastore.

        initialization();
        execution();

    }

    private void initialization()
    {
        firstName=(EditText)findViewById(R.id.firstName);
        lastName=(EditText)findViewById(R.id.lastName);
        emailAddress=(EditText)findViewById(R.id.email);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        confirmPassword=(EditText)findViewById(R.id.confirmPassword);
        signupButton=(Button)findViewById(R.id.signupButton);
    }

    private void execution()
    {
        signupButton.setOnClickListener(new SignupClickListener());
    }

    private class SignupClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(!CentralController.isConnected(SignupController.this))
            {
                return;
            }
            sendServerSignupRequest();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup_controller_temp, menu);
        return true;
    }

    private void generateParametersForSignupRequest(ParseUser parseUser)
    {
        //TODO

            parseUser.setUsername(username.getText().toString());
            parseUser.setPassword(password.getText().toString().trim());
            parseUser.setEmail(emailAddress.getText().toString());
            // Set custom properties
            parseUser.put("firstName", firstName.getText().toString());
            parseUser.put("lastName", lastName.getText().toString());

    }

    private Boolean validatePasswords()
    {
        if(!checkForEmptyPasswords())
        {
            return false;
        }
        if(!matchPasswords())
        {
            return false;
        }
        return true;
    }

    private Boolean matchPasswords()
    {
        String passwordText=password.getText().toString();
        String confirmPasswordText=confirmPassword.getText().toString();
        if(!passwordText.equals(confirmPasswordText))
        {
            CentralController.showToast(SignupController.this, "Confirm password didn't match..", Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private Boolean checkForEmptyPasswords()
    {
        String passwordText=password.getText().toString();
        String confirmPasswordText=confirmPassword.getText().toString();
        if(passwordText.equals(""))
        {
            CentralController.showToast(SignupController.this, "Password can't be empty.", Toast.LENGTH_LONG);
            return false;
        }
        if(confirmPasswordText.equals(""))
        {
            CentralController.showToast(SignupController.this, "Confirm password can't be empty.", Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private void sendServerSignupRequest()
    {
        if(!validatePasswords())
        {
            return;
        }
        CentralController.showProgressDialog(this);
        // Create the ParseUser
        ParseUser parseUser = new ParseUser();
        generateParametersForSignupRequest(parseUser);
// Invoke signUpInBackground
        try
        {
             parseUser.signUpInBackground(new SignupResponse());
        }
        catch(Exception e)
        {
            //TODO
            CentralController.dismissProgressDialog();
            CentralController.showToast(SignupController.this, "please provide valid information.", Toast.LENGTH_LONG);
        }
    }

    private class SignupResponse implements SignUpCallback
    {
        @Override
        public void done(ParseException e) {
            CentralController.dismissProgressDialog();
            if (e == null) {
                // Hooray! Let them use the app now.
                CentralController.showToast(SignupController.this, "You have been registered successfully.", Toast.LENGTH_LONG);
                saveObjectId();
//                generateSubscriptionInfo();
                navigateToHomePage();
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                CentralController.showToast(SignupController.this, e.getMessage(), Toast.LENGTH_LONG);
                Log.d("check", "check you" + e.getMessage());
            }
        }
    }

    private void saveObjectId()
    {
        ParseUser parseUser = ParseUser.getCurrentUser();
        String objectId=parseUser.getObjectId();
        Log.d("objectId", "objectId---> "+objectId);
        GlobalController.setObjectId(objectId);
        CentralController.saveUserObjectIdInSharedPreferences(SignupController.this, objectId);
    }

    /*private void generateSubscriptionInfo()
    {
        String subscriptionDate=CentralController.getCurrentDateTime(this);
        int subscriptionDays=1;
        CentralController.showProgressDialog(this, "Creating free subscription for one day");
        SubscriptionInfo subscriptionInfo=new SubscriptionInfo(GlobalController.getObjectId(this), subscriptionDate, subscriptionDays);
//        subscriptionInfo.setSubjectIds(Arrays.asList(1,2,3,4));
        subscriptionInfo.setIsSubscribedFree(true);
        ArrayList<SubscriptionInfo> subscriptionInfos=new ArrayList<SubscriptionInfo>();
        subscriptionInfos.add(subscriptionInfo);
//        subscriptionInfos.sa
//        ParseObject.saveAllInBackground(subscriptionInfos, new SubscriptionCallback(subscriptionDate, subscriptionDays));
//        subscriptionInfo.u
        subscriptionInfo.saveEventually(new SubscriptionCallback(subscriptionDate, subscriptionDays));
    }

    private class SubscriptionCallback implements SaveCallback
    {
        String subscriptionDate;
        int subscriptionDays;

        public SubscriptionCallback(String subscriptionDate, int subscriptionDays)
        {
            this.subscriptionDate=subscriptionDate;
            this.subscriptionDays=subscriptionDays;
        }

        @Override
        public void done(ParseException e)
        {
            CentralController.dismissProgressDialog();
            if (e == null) {
                // Hooray! Let them use the app now.
                CentralController.showToast(SignupController.this, "You have been successfully subscribed free for one day.", Toast.LENGTH_LONG);
                CentralController.saveSubscriptionInfo(SignupController.this, subscriptionDate, subscriptionDays);
                saveObjectId();
                navigateToHomePage();
            } else
            {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                CentralController.showToast(SignupController.this, e.getMessage(), Toast.LENGTH_LONG);
                Log.d("check", "check you" + e.getMessage());
            }
        }
    }*/


    private void navigateToHomePage()
    {
        Intent intent=new Intent(SignupController.this,  Homescreen.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
