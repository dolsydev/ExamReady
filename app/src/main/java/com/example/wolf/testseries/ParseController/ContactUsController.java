package com.example.wolf.testseries.ParseController;

import android.app.Activity;
import android.util.Log;

import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.ParseModelController.CategoriesInfo;
import com.example.wolf.testseries.ParseModelController.ContactUs;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WOLF on 13-05-2015.
 */
public class ContactUsController
{
    private Activity activity;
    public ContactUsController(Activity activity)
    {
        this.activity=activity;
    }

    private void fetchCategoriesFromServer()
    {
        CentralController.showProgressBar(activity);
        ParseQuery<ContactUs> query = ParseQuery.getQuery(ContactUs.class);
// Define our query conditions
        query.whereContains("objectId", "uqvnYI5u9s");
// Execute the find asynchronously
        query.findInBackground(new FetchResponse());
    }





    private class FetchResponse implements FindCallback<ContactUs>
    {
        @Override
        public void done(List<ContactUs> contactUsInfo, ParseException e)
        {
            CentralController.hideProgressBar();
            if (e == null) {
                // Access the array of results here
                ArrayList<ContactUs> contactUsList=(ArrayList<ContactUs>)contactUsInfo;
                if(contactUsList.size()>0)
                {
//                    return contactUsList.get(0);
                }

            } else {
                Log.d("check", "Exception -->: " + e.getMessage());
            }
        }
    }
}
