package com.example.wolf.testseries.fragmentController;


import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wolf.testseries.R;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Test_Learn extends Fragment {
    ImageView mViewupstick;
    ImageView mViewmiddlestick;
    ImageView mViewdownstick;
    Button mButtonsubmit;
    TextView testing;
    TextView timerText;
    public long milliseconds;
    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    RelativeLayout mLayoutlady;
    RelativeLayout mLayoutdialog;
    String mStringMin, mStringHour;
    public long timer = 60000;
    int i = 0;
    private static final String FORMAT = "%02d:%02d:%02d";
    int seconds, minutes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.test_graph, container, false);
        mViewupstick = (ImageView) rootView.findViewById(R.id.imageViewUpsTick);
        mViewupstick.setVisibility(View.GONE);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
        mProgressBar.setProgress(i);
        mLayoutdialog = (RelativeLayout) rootView.findViewById(R.id.layoutdialog);
        testing = (TextView) rootView.findViewById(R.id.textViewtesting);
        mViewmiddlestick = (ImageView) rootView.findViewById(R.id.imageViewMiDDleStick);
        mViewmiddlestick.setVisibility(View.GONE);
        mViewdownstick = (ImageView) rootView.findViewById(R.id.imageViewDowNsTick);
        mViewdownstick.setVisibility(View.GONE);
        mLayoutlady = (RelativeLayout) rootView.findViewById(R.id.relativelayoutlady);
        mLayoutlady.setVisibility(View.GONE);
        mButtonsubmit = (Button) rootView.findViewById(R.id.buttonSubmitpage);
        mButtonsubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mLayoutdialog.setVisibility(View.VISIBLE);
                mLayoutlady.setVisibility(View.VISIBLE);
                mViewupstick.setVisibility(View.VISIBLE);
                mViewmiddlestick.setVisibility(View.VISIBLE);
                mViewdownstick.setVisibility(View.VISIBLE);
            }
        });

        mStringMin=getArguments().getString("minute");
        mStringHour =getArguments().getString("Hour");
        int Hour=Integer.parseInt(mStringHour);
        int Minute=Integer.parseInt(mStringMin);
        milliseconds=Hour*60*60*1000+Minute*60*1000;


        new CountDownTimer(milliseconds, 1000) {

            public void onTick(long millisUntilFinished) {
//                testing.setText("" + millisUntilFinished / 1000);

                testing.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                i++;
                mProgressBar.setProgress(i);
            }

            public void onFinish() {
                testing.setText("done!");
                i++;
                mProgressBar.setProgress(i);
            }
        }.start();


        return rootView;
    }





//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        getActivity().getMenuInflater().inflate(R.menu.main, menu);
//
//        MenuItem timerItem = menu.findItem(R.id.break_timer);
//        timerText = (TextView) MenuItemCompat.getActionView(timerItem);
//
//        timerText.setPadding(10, 0, 10, 0); //Or something like that...
//
//
//        startTimer(30000, 1000);
//        return;
//    }
//    private void startTimer(long duration, long interval) {
//
//        CountDownTimer timer = new CountDownTimer(duration, interval) {
//
//            @Override
//            public void onFinish() {
//                //TODO Whatever's meant to happen when it finishes
//            }
//
//            @Override
//            public void onTick(long millisecondsLeft) {
//                int secondsLeft = (int) Math.round((millisecondsLeft / (double) 1000));
//                timerText.setText(secondsToString(secondsLeft));
//            }
//        };
//
//        timer.start();
//    }
//
//    private String secondsToString(int improperSeconds) {
//
//        //Seconds must be fewer than are in a day
//
//        android.text.format.Time secConverter = new android.text.format.Time();
//
//        secConverter.hour = 0;
//        secConverter.minute = 0;
//        secConverter.second = 0;
//
//        secConverter.second = improperSeconds;
//        secConverter.normalize(true);
//
//        String hours = String.valueOf(secConverter.hour);
//        String minutes = String.valueOf(secConverter.minute);
//        String seconds = String.valueOf(secConverter.second);
//
//        if (seconds.length() < 2) {
//            seconds = "0" + seconds;
//        }
//        if (minutes.length() < 2) {
//            minutes = "0" + minutes;
//        }
//        if (hours.length() < 2) {
//            hours = "0" + hours;
//        }
//
//        String timeString = hours + ":" + minutes + ":" + seconds;
//        return timeString;
//    }

    //    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        getActivity().getMenuInflater().inflate(R.menu.main, menu);
//
//        final MenuItem counter = menu.findItem(R.id.counter);
//        new CountDownTimer(timer, 60000) {
//
//            public void onTick(long millisUntilFinished) {
//                long millis = millisUntilFinished;
//                String hms = (TimeUnit.MILLISECONDS.toHours(millis)) + ":" + (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))) + ":" + (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
//
//                counter.setTitle(hms);
//                timer = millis;
//
//            }
//
//            public void onFinish() {
//                counter.setTitle("done!");
//            }
//        }.start();
//
////        return  true;
//
//    }

}
