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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import com.example.wolf.testseries.NavigationController;
import com.example.wolf.testseries.ParseController.CategoriesInfoController;
import com.example.wolf.testseries.ParseModelController.CategoriesInfo;
import com.example.wolf.testseries.R;

public class Learn_Fragment extends Fragment implements OnClickListener, CategoriesInfoController.CategoriesInfoControllerInterface
{
	
	ListView listViewCategory;
    public static String Test_INFO_TAG="TEST_INFO_TAG";
	Learn_Fragment_List_Adapter mCategory_List_Adapter;
	ArrayList<CategoriesInfo> mListcategory = new ArrayList<CategoriesInfo>();
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initialize(rootView);
        mCategory_List_Adapter = new Learn_Fragment_List_Adapter(getActivity(),mListcategory);
		listViewCategory.setAdapter(mCategory_List_Adapter);
		listViewCategory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
//				 transaction.replace(R.id.frame_container, Category_Option.newInstance(mListcategory.get(position).getCategoryId(), NavigationController.LEARN_MODE), Test_INFO_TAG);
                transaction.addToBackStack(Test_INFO_TAG);
                transaction.commit();
			}
		});
        Log.d("check", "Learn fragment");
        fetchCategories();
		return rootView;
	}

    private void fetchCategories()
    {
        CategoriesInfoController categoriesInfoController=new CategoriesInfoController(getActivity(), this);
        ArrayList<CategoriesInfo> categoriesInfo=categoriesInfoController.fetchCategories();
        updateCategoriesList(categoriesInfo);
    }

    private void updateCategoriesList(ArrayList<CategoriesInfo> categoriesInfo)
    {
        mListcategory.clear();
        mListcategory.addAll(categoriesInfo);
        mCategory_List_Adapter.notifyDataSetChanged();
    }

    private void initialize(View rootView) {
        listViewCategory = (ListView)rootView.findViewById(R.id.listViewcaTegory);
    }



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void fetchingServerCategoriesInfo(ArrayList<CategoriesInfo> categoriesInfoList) {
        updateCategoriesList(categoriesInfoList);
    }
}
