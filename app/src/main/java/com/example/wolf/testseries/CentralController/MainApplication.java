package com.example.wolf.testseries.CentralController;

/**
 * Created by WOLF on 15-03-2015.
 */

import android.app.Application;

import com.example.wolf.testseries.ParseModelController.CategoriesInfo;
import com.example.wolf.testseries.ParseModelController.ContactUs;
import com.example.wolf.testseries.ParseModelController.QuestionInfo;
import com.example.wolf.testseries.ParseModelController.SubscriptionInfo;
import com.example.wolf.testseries.ParseModelController.TestInfo;
import com.example.wolf.testseries.ParseModelController.TestYearInfo;
import com.parse.Parse;
import com.parse.ParseObject;

public class MainApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "pLAUDwnLBXIJ7P6t05T2e11SryF9tXpID89rmnNQ";
    public static final String YOUR_CLIENT_KEY = "KCn7nJ33TMYy35Bi7lAFA1bxASCNkVcMx6KKUbgf";

    @Override public void onCreate() {
        super.onCreate();
//        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(CategoriesInfo.class);
        ParseObject.registerSubclass(TestInfo.class);
        ParseObject.registerSubclass(QuestionInfo.class);
        ParseObject.registerSubclass(TestYearInfo.class);
        ParseObject.registerSubclass(SubscriptionInfo.class);
        ParseObject.registerSubclass(ContactUs.class);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY); // Your Application ID and Client Key are defined elsewhere
    }
}
