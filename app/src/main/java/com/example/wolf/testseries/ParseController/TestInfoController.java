package com.example.wolf.testseries.ParseController;

import android.app.Activity;
import android.util.Log;

import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.ParseModelController.QuestionInfo;
import com.example.wolf.testseries.ParseModelController.TestInfo;
import com.example.wolf.testseries.sqliteController.DatabaseController;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WOLF on 29-03-2015.
 */
public class TestInfoController
{
    Activity activity;
    TestInfoControllerInterface testInfoInterface;

    DatabaseController databaseController;

    public interface TestInfoControllerInterface
    {
        public void fetchingTestInfo(ArrayList<TestInfo> testInfos);
    }

    public TestInfoController(Activity activity, TestInfoControllerInterface testInfoInterface)
    {
        this.activity=activity;
        this.testInfoInterface=testInfoInterface;
        initialization();
    }

    private void initialization()
    {
        databaseController=new DatabaseController(activity);
    }


    public ArrayList<TestInfo> fetchTestInfo()
    {
        ArrayList<TestInfo> oldTestInfos=databaseController.fetchTestNames();
        Log.d("check", "oldTestInfos count"+oldTestInfos.size());
        if(CentralController.isConnected(activity))
        {
            fetchTestInfoFromServer();
        }
        return oldTestInfos;
    }

    private void fetchTestInfoFromServer()
    {
        CentralController.showProgressBar(activity);
        ParseQuery<TestInfo> query = ParseQuery.getQuery(TestInfo.class);
// Define our query conditions
//        query.whereGreaterThan("testId", 0);
        query.addAscendingOrder("testId");
// Execute the find asynchronously
        query.findInBackground(new FetchResponse());
    }


    private ArrayList<Integer> filterNewTest(ArrayList<TestInfo> oldTestInfos, ArrayList<TestInfo> newTestInfos)
    {
        ArrayList<Integer> newTestIds=new ArrayList<Integer>();
        for(TestInfo newTestInfo : newTestInfos)
        {
            Boolean isFound=false;
            for(TestInfo oldTestInfo : oldTestInfos)
            {
                if(oldTestInfo.getTestId()==newTestInfo.getTestId())
                {
                    isFound=true;
                    continue;
                }
            }
            if(!isFound)
            {
                newTestIds.add(newTestInfo.getTestId());
            }
        }
        return newTestIds;
    }



    private void fetchNewTestQuestions(ArrayList<TestInfo> oldTestInfos, ArrayList<TestInfo> newTestInfos)
    {
        CentralController.showProgressBar(activity);
        ParseQuery<QuestionInfo> query = ParseQuery.getQuery(QuestionInfo.class);
// Define our query conditions
        query.orderByAscending("questionId");
        query.whereContainedIn("testId", filterNewTest(oldTestInfos, newTestInfos));
// Execute the find asynchronously
        query.findInBackground(new FetchQuestion(newTestInfos));
    }

    private class FetchQuestion implements FindCallback<QuestionInfo>
    {
        private ArrayList<TestInfo> testInfoList;

        private FetchQuestion(ArrayList<TestInfo> testInfoList)
        {
            super();
            this.testInfoList=testInfoList;
        }
        @Override
        public void done(List<QuestionInfo> questionInfoList, ParseException e)
        {
            CentralController.hideProgressBar();
            if (e == null) {
                // Access the array of results here
                databaseController.insertQuestions((ArrayList<QuestionInfo>)questionInfoList);
                for(QuestionInfo questionInfo :questionInfoList)
                {
                    ParseFile image=questionInfo.getImage();
                    if(image!=null)
                    {
                        Log.d("check", "URL is --> "+image.getUrl());
                    }
                }
                databaseController.updateTests(testInfoList);
                testInfoInterface.fetchingTestInfo(databaseController.fetchTestNames());
                for(int i=0; i< questionInfoList.size(); i++)
                {
                    Log.d("check", "questionInfo ---->  " + questionInfoList.get(i).getQuestion());
                }
            } else {
                Log.d("check", "Exception -->: " + e.getMessage());
            }
        }
    }


    private class FetchResponse implements FindCallback<TestInfo>
    {

        @Override
        public void done(List<TestInfo> testInfoList, ParseException e)
        {
            CentralController.hideProgressBar();
            if (e == null) {
                // Access the array of results here
//                databaseController.updateCategories((ArrayList<TestInfo>)testInfoList);
//                CentralController.showProgressBar(activity);
//                fetchNewTestQuestions(oldTestInfos, (ArrayList<TestInfo>)testInfoList);
                databaseController.updateTests((ArrayList<TestInfo>) testInfoList);
                testInfoInterface.fetchingTestInfo((ArrayList<TestInfo>)testInfoList);
                for(int i=0; i< testInfoList.size(); i++)
                {
                    Log.d("check", "testInfo ---->  " + testInfoList.get(i).getTestName());
                }
            } else {
                Log.d("check", "Exception -->: " + e.getMessage());
            }
        }
    }

}
