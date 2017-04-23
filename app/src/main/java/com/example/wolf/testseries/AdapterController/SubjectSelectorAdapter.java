package com.example.wolf.testseries.AdapterController;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wolf.testseries.ParseModelController.TestInfo;
import com.example.wolf.testseries.R;

import java.util.ArrayList;

/**
 * Created by WOLF on 28-04-2015.
 */
public class SubjectSelectorAdapter extends BaseAdapter
{
    private ArrayList<TestInfo>  testInfos;
    private Activity activity;
    private final int UNSELECTED_SUBJECT=0;
    private final int SELECTED_SUBJECT=1;

    public SubjectSelectorAdapter(ArrayList<TestInfo>  testInfos,  Activity activity)
    {
        this.testInfos=testInfos;
        this.activity=activity;
    }

    @Override
    public int getCount()
    {
        return testInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position)
    {
        return getViewType(position);
    }

    private int getViewType(int position)
    {
        Boolean isSelected=testInfos.get(position).isSelected();
        if(isSelected) {
            return SELECTED_SUBJECT;
        }
        return UNSELECTED_SUBJECT;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private static class ViewHolder
    {
        private TextView subjectName;

        private ViewHolder(TextView subjectName, ImageView imageView)
        {
            this.subjectName = subjectName;
        }

        private ViewHolder(TextView subjectName) {
            this.subjectName = subjectName;
        }

        public TextView getSubjectName() {
            return subjectName;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(getViewType(position)==SELECTED_SUBJECT)
        {
            return paintSelectedSubjects(position, convertView, parent);
        }
        return paintUnselectedSubjects(position, convertView, parent);
    }

    private View paintUnselectedSubjects(int position, View convertView, ViewGroup parent)
    {
        TextView subjectName;
        if(convertView==null) {
            LayoutInflater mInflater = (LayoutInflater)
                    activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.subject_unselected_list_item, parent, false);
            subjectName=(TextView)convertView.findViewById(R.id.subjectName);
            convertView.setTag(new ViewHolder(subjectName));
        }
        else
        {
            ViewHolder viewHolder=(ViewHolder)convertView.getTag();
            subjectName=viewHolder.getSubjectName();
        }
        TestInfo testInfo=testInfos.get(position);
        subjectName.setText(testInfo.getTestName());
        return convertView;
    }

    private View paintSelectedSubjects(int position, View convertView, ViewGroup parent)
    {
        TextView subjectName;
        if(convertView==null) {
            LayoutInflater mInflater = (LayoutInflater)
                    activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.subject_selected_list_item, parent, false);
            subjectName=(TextView)convertView.findViewById(R.id.subjectName);
            convertView.setTag(new ViewHolder(subjectName));
        }
        else
        {
            ViewHolder viewHolder=(ViewHolder)convertView.getTag();
            subjectName=viewHolder.getSubjectName();
        }
        TestInfo testInfo=testInfos.get(position);
        subjectName.setText(testInfo.getTestName());
        return convertView;
    }
}
