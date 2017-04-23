package com.example.wolf.testseries.fragmentController;

import java.util.ArrayList;

import com.example.wolf.testseries.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Freefom_Fragment_List_Adapter extends BaseAdapter
{
	
	Context context;
	int count;
	ArrayList<String> mListcategory = new ArrayList<String>();
	
	public Freefom_Fragment_List_Adapter(Context context,ArrayList<String> mListcategory, int count) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.count = count;
		this.mListcategory = mListcategory;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
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
		holder.textViewCategoryList.setText(mListcategory.get(position));
		return v;
	}

}
