package com.example.wolf.testseries.fragmentController;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wolf.testseries.R;

/**
 * Created by parveen on 5/10/2015.
 */
public class Help extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.help, container, false);
        return rootView;
    }
}
