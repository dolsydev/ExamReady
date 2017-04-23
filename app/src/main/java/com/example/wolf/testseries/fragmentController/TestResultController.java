package com.example.wolf.testseries.fragmentController;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
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

import org.w3c.dom.Text;


public class TestResultController extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PERCENTAGE = "PERCENTAGE";
    private double percentage;

    private ImageView successImage;
    private TextView successMessage,
    percentageText,
    points;
    private View rootView;
    private Button reviewQuiz, retryQuiz;



    private TestResultControllerInterface testResultInterface;


    public interface TestResultControllerInterface
    {
        public void reviewQuiz();
    }


    public void setTestResultInterface(TestResultControllerInterface testResultInterface)
    {
        this.testResultInterface = testResultInterface;
    }

    // TODO: Rename and change types and number of parameters
    public static TestResultController newInstance(double percentage) {
        TestResultController fragment = new TestResultController();
        Bundle args = new Bundle();
        args.putDouble(PERCENTAGE, percentage);
        fragment.setArguments(args);
        return fragment;
    }

    public TestResultController()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            percentage = getArguments().getDouble(PERCENTAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.test_result_controller, container, false);
        initialization();
        paintLayout();
        return rootView;
    }

    private void initialization()
    {
        successImage=(ImageView)rootView.findViewById(R.id.successImage);
        successMessage=(TextView)rootView.findViewById(R.id.successMessage);
        percentageText=(TextView)rootView.findViewById(R.id.percentage);
        points=(TextView)rootView.findViewById(R.id.points);
        reviewQuiz=(Button)rootView.findViewById(R.id.reviewQuiz);
        retryQuiz=(Button)rootView.findViewById(R.id.retryQuiz);
        reviewQuiz.setOnClickListener(new ReviewQuizAction());
        retryQuiz.setOnClickListener(new RetryQuizAction());
    }

    private void paintLayout()
    {
        setSuccessLayout();
        Log.d("percent", "percent in test view --> "+percentage);
        String percentageString=String.format("%.2f", percentage);
        String pointsString=""+(int)percentage;
        Log.d("percent", "string "+percentageString);
        percentageText.setText(percentageString);
        points.setText(pointsString);

    }

    private void setSuccessLayout()
    {
        if(percentage>=50)
        {
            successMessage.setText("Congratulations, you passed.");
            successImage.setImageResource(R.drawable.right);
        }
        else
        {
            successMessage.setText("You didn't pass.");
            successImage.setImageResource(R.drawable.wrong);
        }
    }

    private class RetryQuizAction implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            getActivity().finish();
        }
    }

    private class  ReviewQuizAction implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            testResultInterface.reviewQuiz();
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
