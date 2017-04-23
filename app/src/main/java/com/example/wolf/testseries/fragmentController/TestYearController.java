package com.example.wolf.testseries.fragmentController;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wolf.testseries.AdapterController.TestYearAdapter;
import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.Controller.LearnTestController;
import com.example.wolf.testseries.Controller.TestController;
import com.example.wolf.testseries.Controller.TimeModeController;
import com.example.wolf.testseries.NavigationController;
import com.example.wolf.testseries.ParseController.QuestionDownloader;
import com.example.wolf.testseries.ParseController.TestInfoController;
import com.example.wolf.testseries.ParseController.TestYearParseController;
import com.example.wolf.testseries.ParseModelController.TestInfo;
import com.example.wolf.testseries.ParseModelController.TestYearInfo;
import com.example.wolf.testseries.R;
import com.example.wolf.testseries.sqliteController.DatabaseController;

import java.util.ArrayList;


public class TestYearController extends Fragment implements TestYearParseController.TestYearInfoControllerInterface, QuestionDownloader.QuestionDownloaderInterface
{
    private ListView listView;
    private TestYearAdapter adapter;
    private static final String NAVIGATION_TYPE="navigationType";
    ArrayList<TestYearInfo> testYearInfos=new ArrayList<TestYearInfo>();
    DatabaseController databaseController;

    private View rootView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TEST_ID = "testId";


    // TODO: Rename and change types of parameters
    private int testId;
    private int navigationType;


    // TODO: Rename and change types and number of parameters
    public static TestYearController newInstance(int testId, int navigationType) {
        TestYearController fragment = new TestYearController();
        Bundle args = new Bundle();
        args.putInt(TEST_ID, testId);
        args.putInt(NAVIGATION_TYPE, navigationType);
        fragment.setArguments(args);
        return fragment;
    }

    public TestYearController() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            testId = getArguments().getInt(TEST_ID);
            navigationType=getArguments().getInt(NAVIGATION_TYPE);
            Log.d("track", "have recieved navigation type in test Yera controller "+navigationType);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.test_year_controller, container, false);
        initialization();
        fetchTestNames();
        return rootView;
    }

    private void initialization()
    {
        listView=(ListView)rootView.findViewById(R.id.yearsList);
        listView.setOnItemClickListener(new ListSelectListener());
        adapter=new TestYearAdapter(this, testYearInfos);
        listView.setAdapter(adapter);
        databaseController=new DatabaseController(getActivity());
    }

    private void execution()
    {


    }

    @Override
    public void fetchingTestInfo(ArrayList<TestYearInfo> testYearInfos) {
        updateTestList(testYearInfos);
    }



    private class ListSelectListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(!CentralController.isSubscribed(getActivity(), testId))
            {
                return;
            }
            TestYearInfo testYearInfo=testYearInfos.get(position);
            Log.d("check", "testId ---> " + testYearInfo.getTestId() + "   questionCount --> " + testYearInfo.getQuestionCount());
            if(testYearInfo.getStatus()!=TestYearAdapter.STATUS_DOWNLOADED)
            {
                generateQuestionDownloadRequest(position);
                return;
            }
            if(navigationType== NavigationController.LEARN_MODE)
            {
                Log.d("track", "i am calling LearnTestController in Test Year Controller");
                LearnTestController testController=new LearnTestController(getActivity() , testYearInfo.getYearId(), testYearInfo.getQuestionCount());
            }
            else if(navigationType==NavigationController.TIME_MODE)
            {
                Log.d("track", "i am calling Time Mode Controller in Test Year Controller");
                TimeModeController timeModeController=new TimeModeController(getActivity() , testYearInfo.getYearId(), testYearInfo.getQuestionCount());
            }
            else
            {
                Log.d("track", "iam calling Free Form Controller  in Test Year controller");
                TestController testController=new TestController(getActivity() , testYearInfo.getYearId(), testYearInfo.getQuestionCount());
            }
        }
    }

    private void generateQuestionDownloadRequest(int position)
    {
        TestYearInfo testYearInfo=testYearInfos.get(position);
        new QuestionDownloader(testYearInfo.getYearId(), getActivity(), TestYearController.this);
        testYearInfo.setStatus(TestYearAdapter.STATUS_DOWNLOAD_IN_PROGRESS);
        adapter.notifyDataSetChanged();
    }

    private void fetchTestNames()
    {
        TestYearParseController testYearParseController=new TestYearParseController(getActivity(), this);
        ArrayList<TestYearInfo> testYearInfo=testYearParseController.fetchTestYearInfo(testId);
        updateTestList(testYearInfo);
    }

    private void updateTestList(ArrayList<TestYearInfo> testYearInfo)
    {
        testYearInfos.clear();
        testYearInfos.addAll(testYearInfo);
        adapter.updateTestYearInfos(testYearInfos);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void questionDownloaded(Boolean success, int yearId, int questionCount) {
        if(!success)
        {
            updateStatus(yearId, TestYearAdapter.STATUS_NOT_DOWNLOADED, questionCount);
        }
        else
        {
            updateStatus(yearId, TestYearAdapter.STATUS_DOWNLOADED, questionCount);
        }
        adapter.notifyDataSetChanged();
    }

    private void updateStatus(int yearId, int status, int questionCount)
    {
        for(TestYearInfo testYearInfo : testYearInfos)
        {
            if(testYearInfo.getYearId()==yearId)
            {
                testYearInfo.setStatus(status);
                testYearInfo.setQuestionCount(questionCount);
                break;
            }
        }
    }


    public void reDownloadTest(int position)
    {
        if(!CentralController.isSubscribed(getActivity(), testId))
        {
            return;
        }
        TestYearInfo testYearInfo=testYearInfos.get(position);
        databaseController.deleteTestYearQuestions(testYearInfo.getYearId());
        generateQuestionDownloadRequest(position);
        return;
//        Log.d("check", "testId ---> " + testYearInfo.getTestId() + "   questionCount --> " + testYearInfo.getQuestionCount());
//        if(testYearInfo.getStatus()!=TestYearAdapter.STATUS_DOWNLOADED)
//        {
//
//        }
    }


}
