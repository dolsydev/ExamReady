package com.example.wolf.testseries.fragmentController;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.wolf.testseries.NavigationController;
import com.example.wolf.testseries.ParseController.TestInfoController;
import com.example.wolf.testseries.ParseModelController.TestInfo;
import com.example.wolf.testseries.R;


public class Category_Option  extends Fragment implements OnClickListener, TestInfoController.TestInfoControllerInterface
{

    public static String Test_Subject_TAG ="TEST_SUBJECT_TAG";
    private static final String NAVIGATION_TYPE="navigationType";
	ListView listViewCategory;
	Category_Option_Adapter mCategory_List_Adapter;
	ArrayList<TestInfo> mListcategory = new ArrayList<TestInfo>();
    int navigationType;



    public static Category_Option newInstance(int navigationType) {
        Category_Option fragment = new Category_Option();
        Bundle args = new Bundle();
        args.putInt(NAVIGATION_TYPE, navigationType);
        fragment.setArguments(args);
        return fragment;
    }

    public Category_Option()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            navigationType =getArguments().getInt(NAVIGATION_TYPE);
            Log.d("track", "got navigation type asd --> "+navigationType);
        }
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

//        Intent intent=get
        View rootView = inflater.inflate(R.layout.category_option_list, container, false);
        initialize(rootView);
        mCategory_List_Adapter = new Category_Option_Adapter(getActivity(),mListcategory);
		listViewCategory.setAdapter(mCategory_List_Adapter);
		listViewCategory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
               /*
                Log.d("check", "testId ---> "+testInfo.getTestId()+"   questionCount --> "+testInfo.getQuestionsCount());
                if(navigationType== NavigationController.LEARN_MODE)
                {
                    LearnTestController testController=new LearnTestController(getActivity() , testInfo.getTestId(), testInfo.getQuestionsCount());
                }
                else
                {
                    TestController testController=new TestController(getActivity() , testInfo.getTestId(), testInfo.getQuestionsCount());
                }*/
                TestInfo testInfo=mListcategory.get(position);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, TestYearController.newInstance(testInfo.getTestId(), navigationType), Test_Subject_TAG);
                transaction.addToBackStack(Test_Subject_TAG);
                transaction.commit();

            }
		});
        fetchTestNames();
		return rootView;
	}

	private void initialize(View rootView) {
		listViewCategory = (ListView)rootView.findViewById(R.id.listViewcaTegory);
	}

    private void fetchTestNames()
    {
        TestInfoController testInfoController=new TestInfoController(getActivity(), this);
        ArrayList<TestInfo> testInfo=testInfoController.fetchTestInfo();
        updateTestList(testInfo);
    }

    private void updateTestList(ArrayList<TestInfo> testInfo)
    {
        mListcategory.clear();
        mListcategory.addAll(testInfo);
        mCategory_List_Adapter.notifyDataSetChanged();
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void fetchingTestInfo(ArrayList<TestInfo> testInfos)
    {
        updateTestList(testInfos);
    }
}
