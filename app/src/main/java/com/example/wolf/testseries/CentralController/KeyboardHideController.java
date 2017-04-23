package com.example.wolf.testseries.CentralController;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.wolf.testseries.InterfaceController.KeyboardHideInterface;


/**
 * Created by sumanthanda on 23/01/15.
 */
public class KeyboardHideController {

    private Activity activity;
    private KeyboardHideInterface keyboardHideInterface;
    public KeyboardHideController(Activity activity, View parentView)
    {
        this.activity=activity;
        setupUI(parentView);
    }

    public KeyboardHideController(Activity activity, View parentView, KeyboardHideInterface keyboardHideInterface)
    {
        this.activity=activity;
        this.keyboardHideInterface=keyboardHideInterface;
        setupUI(parentView);
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    handleCallBack();
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void handleCallBack()
    {
        if(keyboardHideInterface!=null)
        {
            keyboardHideInterface.tappedOutsideEditText();
        }
    }

}
