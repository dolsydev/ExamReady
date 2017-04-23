package com.example.wolf.testseries.fragmentController;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wolf.testseries.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionCompletionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionCompletionFragment extends Fragment
{
    private QuestionCompletionFragmentInterface questionCompletionFragmentInterface;
    public interface QuestionCompletionFragmentInterface
    {
        public void showResult();
        public Boolean shouldAllowCrossCheck();
    }



    private View rootView;
    private Button crossCheck, viewResult;


    // TODO: Rename and change types and number of parameters
    public static QuestionCompletionFragment newInstance()
    {
        QuestionCompletionFragment fragment = new QuestionCompletionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setQuestionCompletionFragmentInterface(QuestionCompletionFragmentInterface questionCompletionFragmentInterface)
    {
        this.questionCompletionFragmentInterface=questionCompletionFragmentInterface;
    }

    public QuestionCompletionFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.question_completion, container, false);
        initialization();
        return rootView;
    }

    private void initialization()
    {
        crossCheck=(Button)rootView.findViewById(R.id.crossCheck);
        viewResult=(Button)rootView.findViewById(R.id.viewResult);
        crossCheck.setOnClickListener(new CrossCheckAction());
        viewResult.setOnClickListener(new ViewResultAction());
    }



    private class ViewResultAction implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            questionCompletionFragmentInterface.showResult();
        }
    }

    private class CrossCheckAction implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if(questionCompletionFragmentInterface.shouldAllowCrossCheck())
            {
                navigateToFirstQuestion();
            }
        }
    }

    private void navigateToFirstQuestion()
    {
        FragmentManager fragmentManager=getFragmentManager();
        Fragment fragment=fragmentManager.findFragmentByTag(""+1);
        if(fragment==null)
        {
            Log.d("check", "null coming");
            return;
        }
        Log.d("check", "pop");
        fragmentManager.popBackStack(""+1, 0);
    }


}
