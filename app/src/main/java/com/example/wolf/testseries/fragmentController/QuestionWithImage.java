package com.example.wolf.testseries.fragmentController;

import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.wolf.testseries.Controller.TimeModeController;
import com.example.wolf.testseries.ImageLoader.ImageLoader;
import com.example.wolf.testseries.InterfaceController.QuestionPageInterface;
import com.example.wolf.testseries.InterfaceController.TimeModeInterface;
import com.example.wolf.testseries.ParseModelController.QuestionInfo;
import com.example.wolf.testseries.R;
import com.example.wolf.testseries.sqliteController.DatabaseController;



public class QuestionWithImage extends Fragment implements TimeModeInterface {
    private View rootView;
    private int questionNumber,
            testId,  optionSelected;
    private TextView question;
    private RadioButton[] optionButtons;
    private Button nextButton,
            previousButton;
    private QuestionInfo questionInfo;
    private ImageView questionImage;

    private ImageView choiceOneImage,
    choiceTwoImage,
    choiceThreeImage,
    choiceFourImage,
    explanationImage;
    private static TimeModeController timeModeController;
    private Boolean isTimeMode=false;
    private LinearLayout watchContainer;
    private TextView timeWatch;

    public void setQuestionPageInterface(QuestionPageInterface questionPageInterface) {
        this.questionPageInterface = questionPageInterface;
    }

    private QuestionPageInterface questionPageInterface;

    public static QuestionWithImage newInstance(int questionNumber, int testId, int optionSelected) {
        QuestionWithImage fragment = new QuestionWithImage();
        Bundle args = new Bundle();
        args.putInt("QUESTION_NUMBER", questionNumber);
        args.putInt("TEST_ID", testId);
        args.putInt("OPTION_SELECTED", optionSelected);
        fragment.setArguments(args);
        return fragment;
    }

    public static QuestionWithImage newInstance(int questionNumber, int testId, int optionSelected, TimeModeController timeModeController) {
        QuestionWithImage fragment = new QuestionWithImage();
        Bundle args = new Bundle();
        args.putInt("QUESTION_NUMBER", questionNumber);
        args.putInt("TEST_ID", testId);
        args.putInt("OPTION_SELECTED", optionSelected);
        fragment.setArguments(args);
        QuestionWithImage.timeModeController=timeModeController;
        fragment.isTimeMode=true;
        return fragment;

    }


    public QuestionWithImage() {

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
        if(isTimeMode)
        {
            timeModeController.setTimeModeInterface(this);
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("callCheck", "i am called");
        rootView = inflater.inflate(R.layout.question_with_image, container, false);
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
        questionImage=(ImageView)rootView.findViewById(R.id.questionImage);
        choiceOneImage=(ImageView)rootView.findViewById(R.id.choiceOneImage);
        choiceTwoImage=(ImageView)rootView.findViewById(R.id.choiceTwoImage);
        choiceThreeImage=(ImageView)rootView.findViewById(R.id.choiceThreeImage);
        choiceFourImage=(ImageView)rootView.findViewById(R.id.choiceFourImage);
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
        if(!questionInfo.getImageURL().equals(""))
        {
            Log.d("check", "trying to make image visible");
            questionImage.setVisibility(View.VISIBLE);
            paintQuestionImage();
        }
        else
        {
            Log.d("check", "no image");
        }
        paintChoiceImages();
//        paintQuestionImage();
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

    private void paintQuestionImage()
    {
        ImageLoader imageLoader=new ImageLoader(getActivity().getApplicationContext());
        Log.d("image", questionInfo.getImageURL());
        imageLoader.DisplayImage(questionInfo.getImageURL(), questionImage);
    }

    private void paintImage(String URL, ImageView imageView)
    {
        if(URL.equals(""))
        {
            return;
        }
        imageView.setVisibility(View.VISIBLE);
        ImageLoader imageLoader=new ImageLoader(getActivity().getApplicationContext());
        imageLoader.DisplayImage(URL, imageView);
    }

    private void paintChoiceImages()
    {
        paintImage(questionInfo.getChoiceOneImageURL(), choiceOneImage);
        paintImage(questionInfo.getChoiceTwoImageURL(), choiceTwoImage);
        paintImage(questionInfo.getChoiceThreeImageURL(), choiceThreeImage);
        paintImage(questionInfo.getChoiceFourImageURL(), choiceFourImage);
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
    public void updateTime(String time)
    {
        Log.d("time", "called here" +
                "");
        timeWatch.setText(time);
    }
}
