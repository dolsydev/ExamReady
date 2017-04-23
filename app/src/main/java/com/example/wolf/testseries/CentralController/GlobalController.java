package com.example.wolf.testseries.CentralController;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by WOLF on 13-04-2015.
 */
public class GlobalController
{
    public static String credentialsFile="MyAccount";
    private static String objectId;
    private static String subscriptionDate;
    private static int subscriptionDays;



    public static String getObjectId(Context context) {
        if(objectId!=null)
        {
            return objectId;
        }
        SharedPreferences sharedPreferences=context.getSharedPreferences(GlobalController.credentialsFile, Context.MODE_PRIVATE);
        if(sharedPreferences.contains("objectId")) {
            GlobalController.objectId = sharedPreferences.getString("objectId", "");
        }
        return GlobalController.objectId;
    }

    public static String getSubscriptionDate(Context context) {
        if(subscriptionDate!=null)
        {
            return subscriptionDate;
        }
        subscriptionDate= getStringFromSharedPreferences(context, "subscriptionDate");
        return GlobalController.subscriptionDate;
    }

    public static int getSubscriptionDays(Context context) {
        if(subscriptionDays!=0)
        {
            return subscriptionDays;
        }
        subscriptionDays= getIntFromSharedPreferences(context, "subscriptionDays");
        return GlobalController.subscriptionDays;
    }

    private static String getStringFromSharedPreferences(Context context, String key)
    {
        String savedString="";
        SharedPreferences sharedPreferences=context.getSharedPreferences(GlobalController.credentialsFile, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(key)) {
            savedString = sharedPreferences.getString(key, "");
        }
        return savedString;
    }

    private static int getIntFromSharedPreferences(Context context, String key)
    {
        int savedInt=0;
        SharedPreferences sharedPreferences=context.getSharedPreferences(GlobalController.credentialsFile, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(key)) {
            savedInt = sharedPreferences.getInt(key, 0);
        }
        return savedInt;
    }

    public static void setObjectId(String objectId) {
        GlobalController.objectId = objectId;
    }


}
