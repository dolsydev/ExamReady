package com.example.wolf.testseries.fragmentController;

import java.util.ArrayList;

import com.example.wolf.testseries.ParseModelController.CategoriesInfo;
import com.example.wolf.testseries.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Learn_Fragment_List_Adapter extends BaseAdapter
{
	
	Context context;
	ArrayList<CategoriesInfo> mListcategory = new ArrayList<CategoriesInfo>();
	
	public Learn_Fragment_List_Adapter(Context context,ArrayList<CategoriesInfo> mListcategory) {
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
			v = vi.inflate(R.layout.custom_list_adapter, null);
			holder.textViewCategoryList = (TextView)v.findViewById(R.id.textViewcaTegory);
			v.setTag(holder);
		} else{
	    	  holder = (ViewHolder) v.getTag();
	      }
		holder.textViewCategoryList.setText(mListcategory.get(position).getCategoryName());
		return v;
	}

}
