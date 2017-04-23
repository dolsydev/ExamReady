package com.example.wolf.testseries.fragmentController;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.wolf.testseries.AudioCache.AudioCache;
import com.example.wolf.testseries.InterfaceController.QuestionPageInterface;
import com.example.wolf.testseries.ParseModelController.QuestionInfo;
import com.example.wolf.testseries.R;
import com.example.wolf.testseries.sqliteController.DatabaseController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageAnswerReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlainAnswerReviewFragment extends Fragment {
    private View rootView;
    private int questionNumber,
            testId,  optionSelected;
    private TextView question,
            explanationText,
            resultStatus;
    private RadioButton[] optionButtons;
    private ImageView[] markViews;
    private Button nextButton,
            previousButton;
    private Button viewExplanation;
    private QuestionInfo questionInfo;
    private QuestionPageInterface questionPageInterface;
    private MediaPlayer mediaPlayer;
    private Button continueButton;
    private  PopupWindow popupWindow;
    private AudioCache audioCache;
    public void setQuestionPageInterface(QuestionPageInterface questionPageInterface)
    {
        this.questionPageInterface = questionPageInterface;
    }



    public static PlainAnswerReviewFragment newInstance(int questionNumber, int testId, int optionSelected) {
        PlainAnswerReviewFragment fragment = new PlainAnswerReviewFragment();
        Bundle args = new Bundle();
        args.putInt("QUESTION_NUMBER", questionNumber);
        args.putInt("TEST_ID", testId);
        args.putInt("OPTION_SELECTED", optionSelected);
        fragment.setArguments(args);
        return fragment;
    }


    public PlainAnswerReviewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionNumber = getArguments().getInt("QUESTION_NUMBER");
            testId=getArguments().getInt("TEST_ID");
            optionSelected=getArguments().getInt("OPTION_SELECTED");
            Log.d("check", "questionNumber --> " + questionNumber);
            Log.d("option", "option selected --> "+optionSelected);
        }
        questionInfo=fetchQuestionInfo(questionNumber, testId);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.plain_answer_review, container, false);
        initialize();
        paintQuestionDetails();
        paintResultStatus(isCurrentSelectionCorrect());
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
        resultStatus=(TextView)rootView.findViewById(R.id.resultStatus);
        optionButtons = new RadioButton[4];
        markViews=new ImageView[4];
        optionButtons[0] = (RadioButton) rootView.findViewById(R.id.radioButtonOption1);
        optionButtons[1] = (RadioButton) rootView.findViewById(R.id.radioButtonOption2);
        optionButtons[2] = (RadioButton) rootView.findViewById(R.id.radioButtonOption3);
        optionButtons[3] = (RadioButton) rootView.findViewById(R.id.radioButtonOption4);
//        optionButtons[0].setActivated(false);
        for(int i=0; i< 4; i++)
        {
            optionButtons[i].setClickable(false);
            optionButtons[i].setEnabled(false);
        }
        optionButtons[0].setClickable(false);
        markViews[0]=(ImageView) rootView.findViewById(R.id.markFirst);
        markViews[1]=(ImageView)rootView.findViewById(R.id.markSecond);
        markViews[2]=(ImageView)rootView.findViewById(R.id.markThird);
        markViews[3]=(ImageView)rootView.findViewById(R.id.markFourth);
        for(int i=0; i< optionButtons.length; i++)
        {
            optionButtons[i].setOnClickListener(new OptionSelectAction());
        }
        previousButton = (Button) rootView.findViewById(R.id.submitButton);
        nextButton = (Button) rootView.findViewById(R.id.buttonNextPage);
        previousButton.setOnClickListener(new PreviousQuestionAction());
        nextButton.setOnClickListener(new NextQuestionAction());
        viewExplanation=(Button)rootView.findViewById(R.id.viewExplanation);
        viewExplanation.setOnClickListener(new ViewExplanationAction());
        audioCache=new AudioCache(getActivity());
        handleVisibilityOfPreviousButton();
    }

    private boolean isCurrentSelectionCorrect()
    {
        int answerCode=questionInfo.getAnswerCode();
        if(answerCode==optionSelected)
        {
            return true;
        }
        return false;
    }

    private void paintResultStatus(boolean isCorrect)
    {
        String statusString="";
        if(isCorrect)
        {
            resultStatus.setBackgroundColor(Color.GREEN);
            resultStatus.setText("Correct");
        }
        else
        {
            resultStatus.setBackgroundColor(Color.RED);
            resultStatus.setText("Wrong");
        }

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
        showCorrectMark();
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

    private void showCorrectMark()
    {
        int correctAnswer=questionInfo.getAnswerCode();
        markViews[correctAnswer-1].setVisibility(View.VISIBLE);
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

    private class ViewExplanationAction implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            showPopupWindowForExplanation();
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
        public void onClick(View v)
        {
            questionPageInterface.moveToPreviousQuestion(questionNumber);
        }
    }

    private void showPopupWindowForExplanation() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            View layout;


            layout=inflater.inflate(R.layout.explanation_popup, null);
            initializeForExplanation(layout);

            popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            playAudioExplanation(questionInfo.getAudioExplanationURL());

//            btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup);
//            btnClosePopup.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeForExplanation(View popupView)
    {
        continueButton=(Button)popupView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new ContinueAction());
        explanationText=(TextView)popupView.findViewById(R.id.explanationText);
        explanationText.setText(questionInfo.getExplanation());
    }

    private class ContinueAction implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
            if(mediaPlayer!=null)
            {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            }
            questionPageInterface.moveToNextQuestion(questionNumber);
        }
    }

    private void playAudioExplanation()
    {
        int resourceId=getResources().getIdentifier("e_"+questionNumber,
                "raw", getActivity().getPackageName());
        mediaPlayer = MediaPlayer.create(getActivity(), resourceId);
        mediaPlayer.start();
    }

    private void playAudioExplanation(String fileName)
    {
        if(fileName==null || fileName.equals(""))
        {
            Log.e("error", "error in playing audio as  audio file name is null or invalid --> "+fileName);
            return;
        }
        mediaPlayer=new MediaPlayer();
        try{
            String audioAbsolutePath=audioCache.getDirectoryAbsolutePath()+"/"+fileName;
            Log.e("audio", "audioAbsolutePath  --> "+audioAbsolutePath);
            mediaPlayer.setDataSource(audioAbsolutePath);//Write your location here
            mediaPlayer.prepare();
            mediaPlayer.start();

        }catch(Exception e)
        {
            Log.e("error", "exception occurs --> "+e.getMessage());
            e.printStackTrace();
        }
    }




}
