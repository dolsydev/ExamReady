package com.example.wolf.testseries.ParseController;

import android.app.Activity;
import android.util.Log;

import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.LoginController;
import com.example.wolf.testseries.ParseModelController.CategoriesInfo;
import com.example.wolf.testseries.sqliteController.DatabaseController;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WOLF on 29-03-2015.
 */
public class CategoriesInfoController
{
    Activity activity;
    DatabaseController databaseController;
    LoginController loginController;
    CategoriesInfoControllerInterface categoriesInfoInterface;

    public interface CategoriesInfoControllerInterface{
        public void fetchingServerCategoriesInfo(ArrayList<CategoriesInfo> categoriesInfoList);
    }

    public CategoriesInfoController(Activity activity, CategoriesInfoControllerInterface categoriesInfoInterface)
    {
        this.activity=activity;
        this.categoriesInfoInterface=categoriesInfoInterface;
        initialization();
    }

    private void initialization()
    {
        databaseController=new DatabaseController(activity);
    }

    public ArrayList<CategoriesInfo> fetchCategories()
    {
        if(CentralController.isConnected(activity))
        {
            fetchCategoriesFromServer();
        }
        return databaseController.fetchCategories();
    }

    private void fetchCategoriesFromServer()
    {
        CentralController.showProgressBar(activity);
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
            CentralController.hideProgressBar();
            if (e == null) {
                // Access the array of results here
                databaseController.updateCategories((ArrayList<CategoriesInfo>)categoriesInfoList);
                categoriesInfoInterface.fetchingServerCategoriesInfo((ArrayList<CategoriesInfo>)categoriesInfoList);
                for(int i=0; i< categoriesInfoList.size(); i++)
                {
                    Log.d("check", "categoriesInfo ---->  " + categoriesInfoList.get(i).getCategoryName());
                }
            } else {
                Log.d("check", "Exception -->: " + e.getMessage());
            }
        }
    }
}
