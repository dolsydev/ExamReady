package com.example.wolf.testseries;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.CentralController.GlobalController;
import com.example.wolf.testseries.ParseController.CategoriesInfoController;
import com.example.wolf.testseries.ParseController.TestInfoController;
import com.example.wolf.testseries.ParseModelController.CategoriesInfo;
import com.example.wolf.testseries.ParseModelController.SubscriptionInfo;
import com.example.wolf.testseries.ParseModelController.TestInfo;
import com.example.wolf.testseries.ParseModelController.TestYearInfo;
import com.example.wolf.testseries.sqliteController.DatabaseController;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class LoginController extends ActionBarActivity implements CategoriesInfoController.CategoriesInfoControllerInterface, TestInfoController.TestInfoControllerInterface {

    EditText username,
    password;

    Button signinButton;
    TextView registerButton;
    ProgressBar progressBar;
    ArrayList<CategoriesInfo> categoriesInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_controller);
        initialization();
        execution();
//        handleLogin();
    }

    private void initialization()
    {

        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        signinButton=(Button)findViewById(R.id.signinButton);
        registerButton=(TextView)findViewById(R.id.registerButton);
        //TODO
        categoriesInfo=new ArrayList<CategoriesInfo>();
    }

    private void execution()
    {
        signinButton.setOnClickListener(new SigninClickListener());
        registerButton.setOnClickListener(new RegisterClickListener());
    }

    @Override
    public void fetchingServerCategoriesInfo(ArrayList<CategoriesInfo> categoriesInfoList) {

    }

    @Override
    public void fetchingTestInfo(ArrayList<TestInfo> testInfos) {

    }

    private class SigninClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(!CentralController.isConnected(LoginController.this))
            {
                return;
            }
            //TODO
//            generateTemporaryClass();
//            fetchCategories();
//            generateTemporaryTest();
//            CategoriesInfoController info=new CategoriesInfoController(LoginController.this, LoginController.this);
//            info.fetchCategories();
//            TestInfoController testInfo=new TestInfoController(LoginController.this, LoginController.this);
//            testInfo.fetchTestInfo(1);
            handleLogin();
        }
    }

    private void generateTemporaryClass()
    {
        CentralController.showProgressDialog(this);
        CategoriesInfo categoriesInfo = new CategoriesInfo(5, "Geography");
// Set the current user, assuming a user is signed in

// Immediately save the data asynchronously
        categoriesInfo.saveInBackground( new SaveServerResponse());
    }

    

    private void fetchCategories()
    {
        CentralController.showProgressDialog(this);
        ParseQuery<CategoriesInfo> query = ParseQuery.getQuery(CategoriesInfo.class);
// Define our query conditions
        query.whereGreaterThan("categoryId", 0);
// Execute the find asynchronously
        query.findInBackground(new FetchResponse());
    }



    private class FetchResponse implements FindCallback<CategoriesInfo>
    {
        @Override
        public void done(List<CategoriesInfo> categoriesInfoList, ParseException e)
        {
            CentralController.dismissProgressDialog();
            if (e == null) {
                // Access the array of results here
                categoriesInfo.addAll(categoriesInfoList);
                for(int i=0; i< categoriesInfoList.size(); i++)
                {
                    Log.d("check", "categoriesInfo ---->  " + categoriesInfoList.get(i).getCategoryName());
                }
            } else {
                Log.d("check", "Exception -->: " + e.getMessage());
            }
        }
    }

    private class RegisterClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            LoginController.this.navigateToSignup();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_controller_temp, menu);
        return true;
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

    private void handleLogin()
    {
        CentralController.showProgressDialog(this);
       String userNameText=username.getText().toString();
       String passwordText=password.getText().toString();
       sendLoginRequest(userNameText, passwordText);
    }

    public void sendLoginRequest(String username, String password)
    {
        ParseUser parseUser=new ParseUser();
        try
        {
            ParseUser.logInInBackground(username, password, new LoginServerResponse());
        }
        catch(Exception e)
        {
            CentralController.dismissProgressDialog();
        }

    }

    private class LoginServerResponse implements LogInCallback
    {

        @Override
        public void done(ParseUser parseUser, ParseException e) {
            CentralController.dismissProgressDialog();
            if (parseUser != null)
            {
                String objectId=parseUser.getObjectId();
                Log.d("objectId", "objectId---> "+objectId);
                GlobalController.setObjectId(objectId);
                CentralController.saveUserObjectIdInSharedPreferences(LoginController.this, objectId);
                CentralController.showToast(LoginController.this,"Welcome!" , Toast.LENGTH_LONG);
                fetchSubscriptionInfoFromServer(parseUser.getObjectId());
                // Hooray! The user is logged in.
            } else {
                // Signup failed. Look at the ParseException to see what happened.
                CentralController.showToast(LoginController.this, "please provide valid username or password.", Toast.LENGTH_LONG);
            }
        }
    }

    private void navigateToHomePage()
    {
        Intent intent=new Intent(LoginController.this,  Homescreen.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void navigateToSignup()
    {
        Intent intent=new Intent(LoginController.this,  SignupController.class);

        startActivity(intent);
    }

    private class SaveServerResponse implements SaveCallback
    {
        public void done(ParseException e) {
            CentralController.dismissProgressDialog();
            if (e == null) {
                Log.d("check", "saved successfully");
            } else {
                Log.d("check", "exception --> "+e.getMessage());
            }
        }
    }

    private void fetchSubscriptionInfoFromServer(String userId)
    {
        CentralController.showProgressDialog(this, "Checking subscription.");
        ParseQuery<SubscriptionInfo> query = ParseQuery.getQuery(SubscriptionInfo.class);
// Define our query conditions
        query.whereEqualTo("userId", userId);
//        query.orderByAscending("year");
// Execute the find asynchronously
        query.findInBackground(new FetchSubscriptionInfoResponse());
    }

    private class FetchSubscriptionInfoResponse implements FindCallback<SubscriptionInfo>
    {
        @Override
        public void done(List<SubscriptionInfo> subscriptionInfos, ParseException e)
        {
            CentralController.dismissProgressDialog();
            if (e == null)
            {
                ArrayList<SubscriptionInfo> subscriptionInfoList=(ArrayList<SubscriptionInfo>)subscriptionInfos;
                CentralController.filterValidSubscriptions(LoginController.this, subscriptionInfoList);
                DatabaseController databaseController=new DatabaseController(LoginController.this);
                databaseController.insertSubscriptionInfos(subscriptionInfoList);
            }
            else
            {
                Log.d("check", "Exception -->: " + e.getMessage());
            }
            navigateToHomePage();
        }
    }

}


