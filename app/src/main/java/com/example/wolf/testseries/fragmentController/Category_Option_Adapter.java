package com.example.wolf.testseries.fragmentController;

import java.util.ArrayList;

import com.example.wolf.testseries.ParseModelController.TestInfo;
import com.example.wolf.testseries.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Category_Option_Adapter extends BaseAdapter
{
	
	Context context;
	ArrayList<TestInfo> mListcategory = new ArrayList<TestInfo>();
	
	public Category_Option_Adapter(Context context,ArrayList<TestInfo> mListcategory) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mListcategory = mListcategory;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListcategory.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	class ViewHolder {
		TextView textViewCategoryList;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		View v = convertView;
		
		if(v == null)
		{
			holder = new ViewHolder();
			LayoutInflater vi;
			vi = LayoutInflater.from(context);
			v = vi.inflate(R.layout.category_option_custom, null);
			holder.textViewCategoryList = (TextView)v.findViewById(R.id.textViewcaTegory);
			v.setTag(holder);
		} else{
	    	  holder = (ViewHolder) v.getTag();
	      }
		holder.textViewCategoryList.setText(mListcategory.get(position).getTestName());
		return v;
	}

}
