package com.example.wolf.testseries.fragmentController;

import com.example.wolf.testseries.Controller.TimeModeController;
import com.example.wolf.testseries.InterfaceController.QuestionPageInterface;
import com.example.wolf.testseries.InterfaceController.TimeModeInterface;
import com.example.wolf.testseries.ParseModelController.QuestionInfo;
import com.example.wolf.testseries.R;
import com.example.wolf.testseries.sqliteController.DatabaseController;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class Test_Page extends Fragment implements TimeModeInterface {
    private View rootView;
    private int questionNumber,
    testId,  optionSelected;
    private TextView question;
    private RadioButton[] optionButtons;
    private Button nextButton,
            previousButton;
    private QuestionInfo questionInfo;
    private static TimeModeController timeModeController;
    private Boolean isTimeMode=false;
    private LinearLayout watchContainer;
    private TextView timeWatch;

    public void setQuestionPageInterface(QuestionPageInterface questionPageInterface) {
        this.questionPageInterface = questionPageInterface;
    }

    private QuestionPageInterface questionPageInterface;

    public static Test_Page newInstance(int questionNumber, int testId, int optionSelected) {
        Test_Page fragment = new Test_Page();
        Bundle args = new Bundle();
        args.putInt("QUESTION_NUMBER", questionNumber);
        args.putInt("TEST_ID", testId);
        args.putInt("OPTION_SELECTED", optionSelected);
        fragment.setArguments(args);
        return fragment;
    }

    public static Test_Page newInstance(int questionNumber, int testId, int optionSelected, TimeModeController timeModeController) {
        Test_Page fragment = new Test_Page();
        Bundle args = new Bundle();
        args.putInt("QUESTION_NUMBER", questionNumber);
        args.putInt("TEST_ID", testId);
        args.putInt("OPTION_SELECTED", optionSelected);
        Test_Page.timeModeController=timeModeController;
        fragment.isTimeMode=true;
        fragment.setArguments(args);
        return fragment;
    }


    public Test_Page() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionNumber = getArguments().getInt("QUESTION_NUMBER");
            testId=getArguments().getInt("TEST_ID");
            optionSelected=getArguments().getInt("OPTION_SELECTED");
            Log.d("check", "questionNumber --> " + questionNumber);
        }
        questionInfo=fetchQuestionInfo(questionNumber, testId);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.test2, container, false);
        initialize();
        paintQuestionDetails();
        if(isTimeMode)
        {
            timeModeController.setTimeModeInterface(this);
        }

        return rootView;
    }

    private QuestionInfo fetchQuestionInfo(int questionNumber, int testId)
    {
        DatabaseController databaseController=new DatabaseController(getActivity());
        QuestionInfo questionInfo=databaseController.getQuestion(questionNumber, testId);
        return questionInfo;
    }

    private void initialize() {
        question = (TextView) rootView.findViewById(R.id.textViewQuestion
        );
        optionButtons = new RadioButton[4];
        optionButtons[0] = (RadioButton) rootView.findViewById(R.id.radioButtonOption1);
        optionButtons[1] = (RadioButton) rootView.findViewById(R.id.radioButtonOption2);
        optionButtons[2] = (RadioButton) rootView.findViewById(R.id.radioButtonOption3);
        optionButtons[3] = (RadioButton) rootView.findViewById(R.id.radioButtonOption4);
        for(int i=0; i< optionButtons.length; i++)
        {
            optionButtons[i].setOnClickListener(new OptionSelectAction());
        }
        previousButton = (Button) rootView.findViewById(R.id.submitButton);
        nextButton = (Button) rootView.findViewById(R.id.buttonNextPage);
        previousButton.setOnClickListener(new PreviousQuestionAction());
        nextButton.setOnClickListener(new NextQuestionAction());
        watchContainer=(LinearLayout)rootView.findViewById(R.id.watchContainer);
        timeWatch=(TextView)rootView.findViewById(R.id.timeWatch);
        if(isTimeMode)
        {
            watchContainer.setVisibility(View.VISIBLE);
            updateTime(timeModeController.getCurrentTime());
        }
        handleVisibilityOfPreviousButton();
    }

    private void handleVisibilityOfPreviousButton()
    {
          if(questionNumber==1)
          {
              previousButton.setVisibility(View.INVISIBLE);
          }
    }

    private void paintQuestionDetails()
    {
        question.setText(questionInfo.getQuestion());
        optionButtons[0].setText(questionInfo.getChoiceOne());
        optionButtons[1].setText(questionInfo.getChoiceTwo());
        optionButtons[2].setText(questionInfo.getChoiceThree());
        optionButtons[3].setText(questionInfo.getChoiceFour());
        if(optionSelected==0)
        {
            return;
        }
        selectOption(optionSelected);
    }

    private void selectOption(int optionSelected)
    {
        if(optionSelected==0)
        {
            return;
        }
        cleanAllButtonsWithException(optionSelected);
        optionButtons[optionSelected-1].setSelected(true);
        optionButtons[optionSelected-1].setChecked(true);
    }


    private void cleanAllButtonsWithException(int currentlySelected)
    {
        for(int i=0; i< optionButtons.length; i++)
        {
            int originalSelectionCode=i+1;
            if(originalSelectionCode==currentlySelected)
            {
                continue;
            }
            RadioButton button=optionButtons[i];
            button.setSelected(false);
            button.setChecked(false);
        }
    }



    private class OptionSelectAction implements View.OnClickListener {
        int optionSelected;
        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.radioButtonOption1:
                    optionSelected=1;
                    break;
                case R.id.radioButtonOption2:
                    optionSelected=2;
                    break;
                case R.id.radioButtonOption3:
                    optionSelected=3;
                    break;
                case R.id.radioButtonOption4:
                    optionSelected=4;
                    break;
            }
            cleanAllButtonsWithException(optionSelected);
            questionPageInterface.optionSelected(questionNumber, questionInfo.getQuestionId(), optionSelected);
        }
    }

    private class NextQuestionAction implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            questionPageInterface.moveToNextQuestion(questionNumber);
        }
    }

    private class PreviousQuestionAction implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            questionPageInterface.moveToPreviousQuestion(questionNumber);
        }
    }

    @Override
    public void updateTime(String time) {
        timeWatch.setText(time);
    }
}

		
		
