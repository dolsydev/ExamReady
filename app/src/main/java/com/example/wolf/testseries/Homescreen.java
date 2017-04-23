package com.example.wolf.testseries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Homescreen extends ActionBarActivity {
    Button buttonLearn, buttonTime, buttonFreeForm, buttonPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);


        buttonLearn = (Button) findViewById(R.id.buttonLearn);
        buttonLearn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(Homescreen.this,
//						Test_Page.class);
//				startActivity(intent);
                navigateToHomePage(NavigationController.LEARN_MODE);

            }
        });



        buttonTime = (Button) findViewById(R.id.buttonTimeMode);
        buttonTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(Homescreen.this,
//						Test_Page.class);
//				startActivity(intent);
                navigateToHomePage(NavigationController.TIME_MODE);

            }
        });

        buttonPayment = (Button) findViewById(R.id.buttonFreeForm);
        buttonPayment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(Homescreen.this,
//						Test_Page.class);
//				startActivity(intent);
                navigateToHomePage(2);

            }
        });

        buttonFreeForm = (Button) findViewById(R.id.buttonPayment);
        buttonFreeForm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(Homescreen.this,
//						Test_Page.class);
//				startActivity(intent);
                navigateToHomePage(3);

            }
        });

    }

    private void navigateToHomePage(int displayOption)
    {
        Intent intent=new Intent(Homescreen.this,  NavigationController.class);
        intent.putExtra("displayOption", displayOption);
//        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
