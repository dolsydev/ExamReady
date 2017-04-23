package com.example.wolf.testseries.fragmentController;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.Controller.TimeModeController;
import com.example.wolf.testseries.R;

/**
 * Created by parveen on 4/19/2015.
 */
public class Set_Time_Screen extends Fragment
{
    EditText mTextHour,mTextMinute;
    Button mButtonSetTime;
    public TimeModeController timeModeController;

    public Set_Time_Screen()
    {

    }

    public void setParentReference(TimeModeController timeModeController)
    {
        this.timeModeController=timeModeController;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.set_time_screen, container, false);
        initialize(rootView);
        return rootView;
    }

    private void initialize(View rootView) {
        mTextHour=(EditText)rootView.findViewById(R.id.edit_text_hour);
        mTextMinute=(EditText)rootView.findViewById(R.id.edit_text_minute);
        mButtonSetTime=(Button)rootView.findViewById(R.id.button_set_time);
        mButtonSetTime.setOnClickListener(new SetTimeAction());
    }

    private class SetTimeAction implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            CentralController.hideKeyboard(getActivity());
           timeModeController.paintFirstQuestion(getHour(), getMinute());
        }
    }

    private int getMinute()
    {
        return convertToInt(mTextMinute.getText().toString());
    }

    private int getHour()
    {
        return convertToInt(mTextHour.getText().toString());
    }

    private int convertToInt(String string)
    {
        try
        {
            return Integer.parseInt(string);
        }
        catch(Exception e)
        {
            return 0;
        }
    }

}
