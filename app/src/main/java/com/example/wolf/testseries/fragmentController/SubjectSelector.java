package com.example.wolf.testseries.fragmentController;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.wolf.testseries.AdapterController.SubjectSelectorAdapter;
import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.ParseController.TestInfoController;
import com.example.wolf.testseries.ParseModelController.TestInfo;
import com.example.wolf.testseries.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubjectSelector#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectSelector extends Fragment implements View.OnClickListener, TestInfoController.TestInfoControllerInterface
 {

    public static String Test_Subject_TAG ="TEST_SUBJECT_TAG";
    private static final String NAVIGATION_TYPE="navigationType";
    ListView listView;
    SubjectSelectorAdapter subjectSelectorAdapter;
    ArrayList<TestInfo> testInfos = new ArrayList<TestInfo>();
    int navigationType;
    private ArrayList<TestInfo> selectedTestInfos = new ArrayList<TestInfo>();
     private Button continuePayment;


public static SubjectSelector newInstance(int navigationType)
{
        SubjectSelector fragment = new SubjectSelector();
        Bundle args = new Bundle();
        args.putInt(NAVIGATION_TYPE, navigationType);
        fragment.setArguments(args);
        return fragment;
        }

        public SubjectSelector()
        {

        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            if (getArguments() != null)
            {
            navigationType =getArguments().getInt(NAVIGATION_TYPE);
            Log.d("track", "got navigation type asd --> "+navigationType);
            }
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState)
    {

//        Intent intent=get
        View rootView = inflater.inflate(R.layout.fragment_subject_selector, container, false);
        initialize(rootView);
        subjectSelectorAdapter = new SubjectSelectorAdapter(testInfos, getActivity());
        listView.setAdapter(subjectSelectorAdapter);
        listView.setOnItemClickListener(new SelectHandler());
        fetchTestNames();
        return rootView;
        }

     private class SelectHandler implements AdapterView.OnItemClickListener
     {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             handleSelectOption(position);
         }
     }

     private void handleSelectOption(int position)
     {
         TestInfo selectedTestInfo=testInfos.get(position);
        if(checkAlreadySelected(selectedTestInfo))
        {
            removeCurrentSubject(selectedTestInfo);
            return;
        }
         if(selectedTestInfos.size()==4)
         {
             CentralController.showToast(getActivity(), "Only four subjects can be purchased at once");
             return;
         }
         addCurrentSubject(selectedTestInfo);
     }

     private Boolean checkAlreadySelected(TestInfo toBeSelectedTestInfo)
     {
         for(int i=0; i< selectedTestInfos.size(); i++)
         {
             TestInfo selectedTestInfo=selectedTestInfos.get(i);
             if(toBeSelectedTestInfo.getTestId()==selectedTestInfo.getTestId())
             {
                 return true;
             }
         }
         return false;
     }

     private void removeCurrentSubject(TestInfo toBeRemovedTestInfo)
     {
         for(int i=0; i< selectedTestInfos.size(); i++)
         {
             TestInfo selectedTestInfo=selectedTestInfos.get(i);
             if(toBeRemovedTestInfo.getTestId()==selectedTestInfo.getTestId())
             {
                 toBeRemovedTestInfo.setIsSelected(false);
                 selectedTestInfos.remove(i);
                 i--;
                 subjectSelectorAdapter.notifyDataSetChanged();
                 break;
             }
         }
     }

     private void addCurrentSubject(TestInfo toBeSelctedTestInfo)
     {
        selectedTestInfos.add(toBeSelctedTestInfo);
         toBeSelctedTestInfo.setIsSelected(true);
        subjectSelectorAdapter.notifyDataSetChanged();
     }

    private void initialize(View rootView) {
        listView = (ListView)rootView.findViewById(R.id.listView);
        continuePayment=(Button)rootView.findViewById(R.id.continueToPayment);
        continuePayment.setOnClickListener(new ContinuePayment());
        }

     private class ContinuePayment  implements View.OnClickListener
     {
         @Override
         public void onClick(View v) {
             if(selectedTestInfos.size()==0)
             {
                 CentralController.showToast(getActivity(), "Please select atleast one subject.");
                 return;
             }
             navigateToPaymentOption();

         }
     }

     private void navigateToPaymentOption()
     {
         Payment_Option paymentOption=new Payment_Option();
         paymentOption.setSelectedSubjects(selectedTestInfos);
         FragmentManager fragmentManager = getFragmentManager();
         FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
         fragmentTransaction.replace(R.id.frame_container, paymentOption, "");
         fragmentTransaction.addToBackStack(null);
         fragmentTransaction.commit();
     }

private void fetchTestNames()
        {
        TestInfoController testInfoController=new TestInfoController(getActivity(), this);
        ArrayList<TestInfo> testInfo=testInfoController.fetchTestInfo();
        updateTestList(testInfo);
        }

     private void filterTest(ArrayList<TestInfo> testInfos)
     {
         for(int i=0; i<testInfos.size(); i++) {
             if (testInfos.get(i).getTestId() == 0) {
                 testInfos.remove(i);
             }
         }
     }

private void updateTestList(ArrayList<TestInfo> testInfo)
        {
            filterTest(testInfo);
            testInfos.clear();
            testInfos.addAll(testInfo);
            subjectSelectorAdapter.notifyDataSetChanged();
        }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub

    }

@Override
public void fetchingTestInfo(ArrayList<TestInfo> testInfos)
{
        updateTestList(testInfos);
        }
   }
