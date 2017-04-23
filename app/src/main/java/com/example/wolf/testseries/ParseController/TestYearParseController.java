

package com.example.wolf.testseries.ParseController;

import android.app.Activity;
import android.util.Log;

import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.ParseModelController.TestYearInfo;
import com.example.wolf.testseries.sqliteController.DatabaseController;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WOLF on 29-03-2015.
 */
public class TestYearParseController
{
    Activity activity;
    //TODO
    TestYearInfoControllerInterface testYearInfoInterface;
    private int testId;

    DatabaseController databaseController;

    public interface TestYearInfoControllerInterface
    {
        public void fetchingTestInfo(ArrayList<TestYearInfo> testYearInfos);
    }

    public TestYearParseController(Activity activity, TestYearInfoControllerInterface testYearInfoControllerInterface)
    {
        this.activity=activity;
        this.testYearInfoInterface =testYearInfoControllerInterface;
        initialization();
    }

    private void initialization()
    {
        databaseController=new DatabaseController(activity);
    }


    public ArrayList<TestYearInfo> fetchTestYearInfo(int testId)
    {
        this.testId=testId;
        ArrayList<TestYearInfo> oldTestYearInfos=databaseController.fetchTestYearInfo(testId);
        Log.d("check", "oldTestInfos count" + oldTestYearInfos.size());
        if(CentralController.isConnected(activity))
        {
            fetchTestInfoFromServer(testId, oldTestYearInfos);
        }
        return oldTestYearInfos;
    }

    private void fetchTestInfoFromServer(int testId, ArrayList<TestYearInfo>oldTestYearInfos)
    {
        CentralController.showProgressBar(activity);
        ParseQuery<TestYearInfo> query = ParseQuery.getQuery(TestYearInfo.class);
// Define our query conditions
        query.whereEqualTo("testId", this.testId);
//        query.orderByAscending("year");
// Execute the find asynchronously
        query.findInBackground(new FetchTestYearResponse(oldTestYearInfos));
    }


   private ArrayList<TestYearInfo> filterNewTest(ArrayList<TestYearInfo> oldTestYearInfos, ArrayList<TestYearInfo> newTestYearInfos)
    {
        ArrayList<TestYearInfo> filteredTestInfos=new ArrayList<TestYearInfo>();
        for(TestYearInfo newTestInfo : newTestYearInfos)
        {
            Boolean isFound=false;
            for(TestYearInfo oldTestInfo : oldTestYearInfos)
            {
                if(oldTestInfo.getTestId()==newTestInfo.getTestId())
                {
                    isFound=true;
                    continue;
                }
            }
            if(!isFound)
            {
                filteredTestInfos.add(newTestInfo);
            }
        }
        return filteredTestInfos;
    }



   /* private void fetchNewTestQuestions(ArrayList<TestYearInfo> oldTestYearInfos, ArrayList<TestYearInfo> newTestYearInfos)
    {
        CentralController.showProgressBar(activity);
        ParseQuery<QuestionInfo> query = ParseQuery.getQuery(QuestionInfo.class);
// Define our query conditions
        query.orderByAscending("questionId");
        query.whereContainedIn("testId", filterNewTest(oldTestYearInfos, newTestYearInfos));
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
                testYearInfoInterface.fetchingTestInfo(databaseController.fetchTestNames(testId));
                for(int i=0; i< questionInfoList.size(); i++)
                {
                    Log.d("check", "questionInfo ---->  " + questionInfoList.get(i).getQuestion());
                }
            } else {
                Log.d("check", "Exception -->: " + e.getMessage());
            }
        }
    }*/


    private class FetchTestYearResponse implements FindCallback<TestYearInfo>
    {
        ArrayList<TestYearInfo> oldTestYearInfos;
        private FetchTestYearResponse(ArrayList<TestYearInfo> oldTestYearInfos)
        {
            super();
            this.oldTestYearInfos=oldTestYearInfos;
        }
        @Override
        public void done(List<TestYearInfo> testYearInfoList, ParseException e)
        {
            CentralController.hideProgressBar();
            if (e == null) {
//                CentralController.showProgressBar(activity);
//                fetchNewTestQuestions(oldTestYearInfos, (ArrayList<TestYearInfo>)testYearInfoList);
//                databaseController.updateTests(testYearInfoList);
                ArrayList<TestYearInfo> filteredTestInfo=filterNewTest(oldTestYearInfos, (ArrayList<TestYearInfo>)testYearInfoList);
                databaseController.insertTestYears(filteredTestInfo);
                testYearInfoInterface.fetchingTestInfo(databaseController.fetchTestYearInfo(testId));
                for(int i=0; i< testYearInfoList.size(); i++)
                {
                    Log.d("check", "testInfo ---->  " + testYearInfoList.get(i).getYear());
                }
            } else {
                Log.d("check", "Exception -->: " + e.getMessage());
            }
        }
    }


}
