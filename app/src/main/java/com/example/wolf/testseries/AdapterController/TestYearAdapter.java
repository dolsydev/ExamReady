package com.example.wolf.testseries.AdapterController;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wolf.testseries.ParseModelController.TestYearInfo;
import com.example.wolf.testseries.R;
import com.example.wolf.testseries.fragmentController.TestYearController;
import com.example.wolf.testseries.sqliteController.DatabaseController;

import java.util.ArrayList;

/**
 * Created by WOLF on 12-04-2015.
 */
public class TestYearAdapter extends BaseAdapter {
    private TestYearController testYearController;
    private Activity activity;
    private ArrayList<TestYearInfo> testYearInfos;

    private final int EMPTY_NOTIFICATION_VIEW = 0;
    private final int DOWNLOADING_VIEW = 1;
    private final int STATUS_VIEW = 2;

    public final static int STATUS_NOT_DOWNLOADED = 0;
    public final static int STATUS_DOWNLOADED = 1;
    public final static int STATUS_DOWNLOAD_IN_PROGRESS = 2;
    public final static int STATUS_UPDATE_AVAILABLE = 3;

    /*To check for first time data loaded, So as not to show empty notification while fetching the data*/
    private Boolean isFirstLoaded=false;


    public TestYearAdapter(TestYearController testYearController, ArrayList<TestYearInfo> testYearInfos) {
        this.testYearInfos = testYearInfos;
        this.testYearController = testYearController;
        activity = testYearController.getActivity();
    }

    public void updateTestYearInfos(ArrayList<TestYearInfo> testYearInfos)
    {
        isFirstLoaded=true;
        this.testYearInfos=testYearInfos;
    }

    @Override
    public boolean isEnabled(int position) {
        if (getViewType(position) == STATUS_VIEW) {
            return true;
        }
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }


    private int getViewType(int position) {
        TestYearInfo testYearInfo = testYearInfos.get(position);
        if (testYearInfos.size() == 0) {
            return EMPTY_NOTIFICATION_VIEW;
        }
        if (testYearInfo.getStatus() == STATUS_DOWNLOADED || testYearInfo.getStatus() == STATUS_NOT_DOWNLOADED
                || testYearInfo.getStatus() == STATUS_UPDATE_AVAILABLE) {
            return STATUS_VIEW;
        }
        if (testYearInfo.getStatus() == STATUS_DOWNLOAD_IN_PROGRESS) {
            return DOWNLOADING_VIEW;
        }
        return EMPTY_NOTIFICATION_VIEW;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getCount() {
        return getRequiredCount();
    }

    private int getRequiredCount()
    {
        if(!isFirstLoaded)
        {
            return 0;
        }
        if(testYearInfos.size()==0)
        {
            return 0;
        }
        return testYearInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return testYearInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class StatusViewHolder
    {
        private ImageView statusImage;
        private TextView testYear;

        private StatusViewHolder(ImageView statusImage, TextView testYearName) {
            this.statusImage = statusImage;
            this.testYear = testYearName;
        }

        private StatusViewHolder(TextView testYearName) {
            this.testYear = testYearName;
        }

        public ImageView getStatusImage() {
            return statusImage;
        }

        public TextView getTestYear() {
            return testYear;
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        switch(getViewType(position))
        {
            case STATUS_VIEW:
                convertView=paintStatusView(position, convertView, parent);
                break;
            case DOWNLOADING_VIEW:
                convertView=paintDownloadInProgressView(position, convertView, parent);
                break;
        }
        return convertView;
    }

    private View paintDownloadInProgressView(int position, View convertView, ViewGroup parent)
    {
        TextView testYear;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.test_year_processing_list_item, parent, false);
            testYear = (TextView) convertView.findViewById(R.id.testYear);
            convertView.setTag(new StatusViewHolder(testYear));
        } else {
            StatusViewHolder statusViewHolder = (StatusViewHolder) convertView.getTag();
            testYear = statusViewHolder.getTestYear();
        }
        TestYearInfo testYearInfo = testYearInfos.get(position);
        testYear.setText(testYearInfo.getYear());
        return convertView;
    }

    private View paintStatusView(int position, View convertView, ViewGroup parent) {

        ImageView statusImage;
        TextView testYear;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.test_year_status_list_item, parent, false);
            statusImage = (ImageView) convertView.findViewById(R.id.testYearStatusImage);
            testYear = (TextView) convertView.findViewById(R.id.testYear);
            convertView.setTag(new StatusViewHolder(statusImage, testYear));
        } else {
            StatusViewHolder statusViewHolder = (StatusViewHolder) convertView.getTag();
            statusImage = statusViewHolder.getStatusImage();
            testYear = statusViewHolder.getTestYear();
        }
        TestYearInfo testYearInfo = testYearInfos.get(position);
        testYear.setText(testYearInfo.getYear());
        statusImage.setImageResource(getStatusImage(testYearInfo.getStatus()));
        statusImage.setOnClickListener(new ReDownloadListener(position));
        return convertView;
    }

    private class ReDownloadListener implements View.OnClickListener
    {
        int position;

        public ReDownloadListener(int position)
        {
            this.position=position;
        }
        @Override
        public void onClick(View v)
        {
            TestYearInfo testYearInfo=testYearInfos.get(position);
            if(testYearInfo.getStatus()==DatabaseController.QUESTION_DOWNLOADED)
            {
                testYearController.reDownloadTest(position);
            }
        }
    }

    private int getStatusImage(int status)
    {
        switch (status)
        {
            case DatabaseController.QUESTION_DOWNLOADED:
                return R.drawable.refresh;
            case DatabaseController.QUESTION_NOT_DOWNLOADED:
            case STATUS_UPDATE_AVAILABLE:
            default:
                return R.drawable.download_icon;
        }
    }
}
