package com.example.wolf.testseries.AdapterController;

import java.util.ArrayList;



import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wolf.testseries.R;
import com.example.wolf.testseries.modelController.IndexMenuItemsModel;

public class IndexMenuAdapter extends BaseAdapter {
	private ArrayList<IndexMenuItemsModel> menuItemsArray;
	 private Context context;

    private final int STANDARD_VIEW=0;
    private final int GIFT_VIEW=1;

    private int viewType;
	
	public IndexMenuAdapter(Context context, ArrayList<IndexMenuItemsModel> menuItemsArray)
	{
		this.menuItemsArray=menuItemsArray;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menuItemsArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return menuItemsArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

    private int getViewType(int position)
    {
        if(position==2)
        {
            return GIFT_VIEW;
        }
        return STANDARD_VIEW;

    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private static class ViewHolder {
	    private ImageView imageView;
	    private TextView textView;

	    public ViewHolder(ImageView imageView, TextView textView) {
	    	setImageView(imageView);
	        setTextView(textView);
	    }

		public ImageView getImageView() {
			return imageView;
		}

		public void setImageView(ImageView imageView) {
			this.imageView = imageView;
		}

		public TextView getTextView() {
			return textView;
		}

		public void setTextView(TextView textView) {
			this.textView = textView;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
        if(getViewType(position)==STANDARD_VIEW)
        {
            return getStandardView(position, convertView, parent);
        }
        if(getViewType(position)==GIFT_VIEW)
        {
            return getGiftView(position, convertView, parent);
        }
        return null;
	}

    private View getStandardView(int position, View convertView, ViewGroup parent)
    {
        ImageView imgIcon;
        TextView txtTitle;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.index_list_item, parent, false);
            imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            txtTitle = (TextView) convertView.findViewById(R.id.title);

            // The tag can be any Object, this just happens to be the ViewHolder
            convertView.setTag(new ViewHolder(imgIcon, txtTitle));
        }
        else
        {
            ViewHolder viewHolder=(ViewHolder)convertView.getTag();
            imgIcon=(ImageView)viewHolder.getImageView();
            txtTitle=(TextView)viewHolder.getTextView();
        }
        imgIcon.setImageResource(menuItemsArray.get(position).getMenuImagePosition());
        txtTitle.setText(menuItemsArray.get(position).getMenuTitle());

        return convertView;
    }

    private View getGiftView(int position, View convertView, ViewGroup parent)
    {
        ImageView imgIcon;
        TextView txtTitle;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.index_list_item, parent, false);
            imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            txtTitle = (TextView) convertView.findViewById(R.id.title);

            // The tag can be any Object, this just happens to be the ViewHolder
            convertView.setTag(new ViewHolder(imgIcon, txtTitle));
        }
        else
        {
            ViewHolder viewHolder=(ViewHolder)convertView.getTag();
            imgIcon=(ImageView)viewHolder.getImageView();
            txtTitle=(TextView)viewHolder.getTextView();
        }


        imgIcon.setImageResource(menuItemsArray.get(position).getMenuImagePosition());
        txtTitle.setText(menuItemsArray.get(position).getMenuTitle());

        return convertView;
    }

}
