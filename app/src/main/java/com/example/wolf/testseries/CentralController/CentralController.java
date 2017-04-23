package com.example.wolf.testseries.CentralController;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wolf.testseries.ParseModelController.SubscriptionInfo;
import com.example.wolf.testseries.sqliteController.DatabaseController;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by WOLF on 15-03-2015.
 */
public class CentralController {
    private static RelativeLayout progressContainer;
    private static Toast toast;
    private static final int LONG_DELAY = 3500; // 3.5 seconds
    private static final int SHORT_DELAY = 2000; //
    private static ProgressDialog progressDialog;

    private static final int POSITIVE_BUTTON=0;
    private static final int NEGATIVE_BUTTON=1;


    public static void showProgressBar(Activity activity )
    {
        hideProgressBar();
        if(activity==null)
        {
            return;
        }
        ViewGroup layout=(ViewGroup)activity.findViewById(android.R.id.content).getRootView();
        ProgressBar progressBar = new ProgressBar(activity,null,android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        progressContainer = new RelativeLayout(activity);
        progressContainer.setGravity(Gravity.CENTER);
        progressContainer.addView(progressBar);
        layout.addView(progressContainer,params);
    }



    public static void hideProgressBar()
    {
        if(progressContainer==null)
        {
            return;
        }
        try
        {
            Log.d("check", "executed first");
            ViewGroup viewParent = (ViewGroup) progressContainer.getParent();
            viewParent.removeView(progressContainer);
            progressContainer=null;
        }
        catch(Exception e)
        {
            Log.d("check", "checking exception is --> "+e);
        }
    }



    public static int showToast(Activity activity, String message)
    {
        if(toast!=null)
        {
            toast.cancel();
        }
        toast= Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        toast.show();
        return getToastDuration(Toast.LENGTH_SHORT);
    }

    public static int showToast(Activity activity, String message, int timeLength)
    {
        if(toast!=null)
        {
            toast.cancel();
        }
        toast=Toast.makeText(activity, message, timeLength);
        toast.show();
        return getToastDuration(timeLength);
    }

    private static int getToastDuration(int timeLength)
    {
        int duration=0;
        switch(timeLength)
        {
            case Toast.LENGTH_SHORT:
                duration= SHORT_DELAY;
            case Toast.LENGTH_LONG:
                duration= LONG_DELAY;
        }
        return duration;
    }

    public static void showProgressDialog(Activity activity, String message)
    {
        dismissProgressDialog();
        progressDialog = ProgressDialog.show(activity, "", message);
    }

    public static void showProgressDialog(Activity activity)
    {
        dismissProgressDialog();
        progressDialog = ProgressDialog.show(activity, "", "Loading..");
    }



    public static void dismissProgressDialog()
    {
        if(progressDialog!=null)
        {
            progressDialog.dismiss();
            progressDialog=null;
        }
    }

    public static boolean isConnected(Activity activity){
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
        {
            CentralController.showToast(activity, "Internet  not available", Toast.LENGTH_LONG);
            return false;
        }
    }

    private static Boolean checkIfNetworkTimeIsEnabled(Activity activity)
    {
        int automaticTimeSetting=0;
        String errorMessage="Sorry, you will have to enable your Date and Time settings to be network updated";
        try
        {
            automaticTimeSetting=Settings.System.getInt(activity.getContentResolver(), Settings.Global.AUTO_TIME, 0);
        }
        catch (Exception e)
        {
            Toast.makeText(activity,errorMessage , Toast.LENGTH_LONG).show();
        }
        if(automaticTimeSetting>0)
        {
            return true;
        }
        showSettingsAlert(activity);
        showToast(activity, errorMessage, Toast.LENGTH_LONG);
        return false;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public static void showSettingsAlert(Activity activity){
        dismissProgressDialog();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        // Setting Dialog Title
        alertDialog.setTitle("Date and Time settings");

        // Setting Dialog Message
        alertDialog.setMessage(" Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogListener(activity, POSITIVE_BUTTON));
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogListener(activity, NEGATIVE_BUTTON));
        // Showing Alert Message
        alertDialog.show();
    }

    private static class DialogListener implements DialogInterface.OnClickListener
    {
        public int buttonType;
        private Activity activity;
        DialogListener(Activity activity, int buttonType)
        {
            this.buttonType=buttonType;
            this.activity=activity;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch(buttonType)
            {
                case POSITIVE_BUTTON:
                    showDateSettings();
                    break;
                case NEGATIVE_BUTTON:
                    showErrorMessage();
                    break;
            }
        }

        private void showDateSettings()
        {
            Intent intent=new Intent(Settings.ACTION_DATE_SETTINGS);
            activity.startActivity(intent);
        }

        private void showErrorMessage()
        {
            String errorMessage="Sorry, you will have to enable your Date and Time settings to be network updated";
            showToast(activity, errorMessage);
        }
    }



    private static String generateNullDateTime()
    {
        return "00/00/0000 00:00:00";
    }

    public static String getCurrentDateTime(Activity activity)
    {
        if(!checkIfNetworkTimeIsEnabled(activity))
        {
            return generateNullDateTime();
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void saveUserObjectIdInSharedPreferences(Activity activity, String objectId)
    {
        SharedPreferences sharedPreferences=activity.getSharedPreferences(GlobalController.credentialsFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("objectId", objectId);
        editor.commit();
    }

    public static void saveSubscriptionInfo(Activity activity, String subscriptionDate, int subscriptionDays)
    {
        saveStringInSharedPreferences(activity, "subscriptionDate", subscriptionDate);
        saveIntInSharedPreferences(activity, "subscriptionDays", subscriptionDays);
    }

    public static void saveStringInSharedPreferences(Activity activity, String key, String value)
    {
        SharedPreferences sharedPreferences=activity.getSharedPreferences(GlobalController.credentialsFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveIntInSharedPreferences(Activity activity, String key, int value)
    {
        SharedPreferences sharedPreferences=activity.getSharedPreferences(GlobalController.credentialsFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private static long getDateTimeDifferenceInSeconds(Activity activity, String subscriptionDate)
    {
        String currentTime=getCurrentDateTime(activity);
        return getDateTimeDifferenceInSeconds(subscriptionDate, currentTime);
    }

    private static long getDateTimeDifferenceInSeconds(String dateStart, String dateStop)
    {
        //HH converts fhour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        long timeInSeconds=0;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            timeInSeconds = diff / 1000;
//            timeInSeconds = diff / 1000 % 60;
//            long diffMinutes = diff / (60 * 1000) % 60;
//            long diffHours = diff / (60 * 60 * 1000) % 24;
//            long diffDays = diff / (24 * 60 * 60 * 1000);



//            System.out.print(diffDays + " days, ");
//            System.out.print(diffHours + " hours, ");
//            System.out.print(diffMinutes + " minutes, ");
//            System.out.print(timeInSeconds + " seconds.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeInSeconds;
    }

    private static long getSubscriptionDaysInSeconds(Activity activity)
    {
        int days=GlobalController.getSubscriptionDays(activity);
        long seconds=days*24*60*60;
        return seconds;
    }

    private static long getDaysInSeconds(int days)
    {
        long seconds=days*24*60*60;
        return seconds;
    }


    public static Boolean isSubscribed(Activity activity)
    {
        Log.d("date", "subscription date --->  "+GlobalController.getSubscriptionDate(activity));
        String currentDateTIme=getCurrentDateTime(activity);
        Log.d("date", "current date --> "+currentDateTIme);
        long timeSinceSubscription=getDateTimeDifferenceInSeconds(GlobalController.getSubscriptionDate(activity), currentDateTIme);
        long subscriptionSeconds=getSubscriptionDaysInSeconds(activity);
        Log.d("date", "timeSinceSubscription ---> "+timeSinceSubscription);
        Log.d("date", "subscriptionSeconds ---> "+subscriptionSeconds);
        if(subscriptionSeconds> timeSinceSubscription)
        {
            return true;
        }
        showToast(activity, "Please purchase subscription for usage.", LONG_DELAY);
        //TODO
        return true;
//        return false;
    }

    private static SubscriptionInfo getSubscriptionDate(Activity activity, int subjectId)
    {
        DatabaseController databaseController=new DatabaseController(activity);
        return databaseController.fetchSubscriptionInfo(subjectId);
    }

    public static Boolean isSubscribed(Activity activity, int subjectId)
    {
        if(subjectId==0)
        {
            return true;
        }
        SubscriptionInfo subscriptionInfo=getSubscriptionDate(activity, subjectId);
        if(subscriptionInfo==null)
        {
            showToast(activity, "Please purchase subscription for usage.", LONG_DELAY);
            return false;
        }
        long timeSinceSubscription=getDateTimeDifferenceInSeconds(activity, subscriptionInfo.getSubscriptionDate());
        long subscriptionSeconds=getDaysInSeconds(subscriptionInfo.getSubscriptionDays());
        Log.d("date", "timeSinceSubscription ---> "+timeSinceSubscription);
        Log.d("date", "subscriptionSeconds ---> "+subscriptionSeconds);
        if(subscriptionSeconds> timeSinceSubscription)
        {
            return true;
        }
        showToast(activity, "Please purchase subscription for usage.", LONG_DELAY);
        //TODO
        return false;
//        return false;
    }

    public static void hideKeyboard(Activity activity)
    {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void saveDataToSDCard(String fileName, byte[] data)
    {
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e)
        {
                Log.d("error", "exception --> "+e.getMessage());     // handle exception
        } catch (IOException e) {
            // handle exception
            Log.d("error", "exception --> "+e.getMessage());
        }
    }


    public static void filterValidSubscriptions(Activity activity, ArrayList<SubscriptionInfo> subscriptionInfos)
    {
        for(int i=0; i< subscriptionInfos.size(); i++)
        {
            SubscriptionInfo subscriptionInfo=subscriptionInfos.get(i);
            if(!checkIfValidSubscription(activity, subscriptionInfo))
            {
                subscriptionInfos.remove(i);
                i--;
            }
        }
    }



    public static Boolean checkIfValidSubscription(Activity activity, SubscriptionInfo subscriptionInfo)
    {
        String subscriptionDate=subscriptionInfo.getSubscriptionDate();
        int subscriptionDays=subscriptionInfo.getSubscriptionDays();
        long  subscribedSeconds=getDaysInSeconds(subscriptionDays);
        long subscriptionConsumed=getDateTimeDifferenceInSeconds(activity, subscriptionDate);
        if(subscriptionConsumed<subscribedSeconds)
        {
            return true;
        }
        return false;
    }
}
