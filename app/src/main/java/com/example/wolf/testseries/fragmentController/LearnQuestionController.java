package com.example.wolf.testseries.fragmentController;

import android.media.Image;
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
import android.widget.Toast;

import com.example.wolf.testseries.AudioCache.AudioCache;
import com.example.wolf.testseries.ImageLoader.ImageLoader;
import com.example.wolf.testseries.InterfaceController.QuestionPageInterface;
import com.example.wolf.testseries.ParseModelController.QuestionInfo;
import com.example.wolf.testseries.R;
import com.example.wolf.testseries.sqliteController.DatabaseController;


public class LearnQuestionController extends Fragment
{

    private View rootView;
    private int questionNumber,
            testId,  optionSelected;
    private TextView question;
    private RadioButton[] optionButtons;
    private Button submitButton;
    private QuestionInfo questionInfo;
    private Button tryAgain, viewExplanation, continueButton;
    private  PopupWindow popupWindow;
    private ImageView questionImage;
    private ImageView choiceOneImage,
            choiceTwoImage,
            choiceThreeImage,
            choiceFourImage;
    private TextView explanationText;
    private MediaPlayer mediaPlayer;
    private AudioCache audioCache;


    // TODO: Rename and change types and number of parameters


    public LearnQuestionController() {
        // Required empty public constructor
    }



    private void showPopupWindowForResponse(Boolean isCorrect) {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            View layout;
            if(isCorrect)
            {
                layout= inflater.inflate(R.layout.correct_answer_options,
                        null);
                initializeForCorrectAnswerButtons(layout);
            }
            else
            {
                layout=inflater.inflate(R.layout.wrong_answer_options, null);
                initializeForWrongAnswer(layout);
            }
            popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

//            btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup);
//            btnClosePopup.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
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


   private void initializeForCorrectAnswerButtons(View popupView)
   {
        viewExplanation=(Button)popupView.findViewById(R.id.viewExplanation);
        continueButton=(Button)popupView.findViewById(R.id.continueButton);
        viewExplanation.setOnClickListener(new ViewExplanation());
        continueButton.setOnClickListener(new ContinueAction());
   }

    private void initializeForWrongAnswer(View popupView)
    {
        tryAgain=(Button)popupView.findViewById(R.id.buttonTryAgain);
        tryAgain.setOnClickListener(new TryAgainAction());
        initializeForCorrectAnswerButtons(popupView);
    }

    private void initializeForExplanation(View popupView)
    {
        continueButton=(Button)popupView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new ContinueAction());
        explanationText=(TextView)popupView.findViewById(R.id.explanationText);
        explanationText.setText(questionInfo.getExplanation());
        ImageView explanationImage=(ImageView)popupView.findViewById(R.id.explanationImage);
        paintImage(questionInfo.getExplanationImageURL(), explanationImage);
    }

    private class ViewExplanation implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            popupWindow.dismiss();
            showPopupWindowForExplanation();
        }
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

    private class TryAgainAction implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
        }
    }


    public void setQuestionPageInterface(QuestionPageInterface questionPageInterface) {
        this.questionPageInterface = questionPageInterface;
    }

    private QuestionPageInterface questionPageInterface;

    public static LearnQuestionController newInstance(int questionNumber, int testId, int optionSelected) {
        LearnQuestionController fragment = new LearnQuestionController();
        Bundle args = new Bundle();
        args.putInt("QUESTION_NUMBER", questionNumber);
        args.putInt("TEST_ID", testId);
        args.putInt("OPTION_SELECTED", optionSelected);
        fragment.setArguments(args);
        return fragment;
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
        rootView = inflater.inflate(R.layout.learn_question_controller, container, false);
        initialize();
        paintQuestionDetails();
        return rootView;
    }

    private QuestionInfo fetchQuestionInfo(int questionNumber, int testId)
    {
        DatabaseController databaseController=new DatabaseController(getActivity());
        QuestionInfo questionInfo=databaseController.getQuestion(questionNumber, testId);
        return questionInfo;
    }

    private void initialize()
    {
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
        submitButton = (Button) rootView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new SubmitQuestionAction());
        questionImage=(ImageView)rootView.findViewById(R.id.questionImage);
        questionImage=(ImageView)rootView.findViewById(R.id.questionImage);
        choiceOneImage=(ImageView)rootView.findViewById(R.id.choiceOneImage);
        choiceTwoImage=(ImageView)rootView.findViewById(R.id.choiceTwoImage);
        choiceThreeImage=(ImageView)rootView.findViewById(R.id.choiceThreeImage);
        choiceFourImage=(ImageView)rootView.findViewById(R.id.choiceFourImage);
        audioCache=new AudioCache(getActivity());
    }

    private void paintQuestionDetails()
    {
        question.setText(questionInfo.getQuestion());
        optionButtons[0].setText(questionInfo.getChoiceOne());
        optionButtons[1].setText(questionInfo.getChoiceTwo());
        optionButtons[2].setText(questionInfo.getChoiceThree());
        optionButtons[3].setText(questionInfo.getChoiceFour());
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

    private void paintChoiceImages()
    {
        paintImage(questionInfo.getChoiceOneImageURL(), choiceOneImage);
        paintImage(questionInfo.getChoiceTwoImageURL(), choiceTwoImage);
        paintImage(questionInfo.getChoiceThreeImageURL(), choiceThreeImage);
        paintImage(questionInfo.getChoiceFourImageURL(), choiceFourImage);
    }

    private void paintImage(String URL, ImageView imageView)
    {
        Log.d("URL", "URL  --> "+URL);
        if(URL.equals(""))
        {
            return;
        }
        imageView.setVisibility(View.VISIBLE);
        ImageLoader imageLoader=new ImageLoader(getActivity().getApplicationContext());
        imageLoader.DisplayImage(URL, imageView);
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
            LearnQuestionController.this.optionSelected=optionSelected;
            questionPageInterface.optionSelected(questionNumber, questionInfo.getQuestionId(), optionSelected);
        }
    }

    private class SubmitQuestionAction implements View.OnClickListener {
        @Override
        public void onClick(View v)
        {
//            /questionPageInterface.moveToNextQuestion(questionNumber);
            if(!checkQuestionSelected())
            {
                return;
            }
           showPopupWindowForResponse(checkAnswer());
        }
    }

    private Boolean checkAnswer()
    {
        if(optionSelected==questionInfo.getAnswerCode())
        {
            return true;
        }
        return false;
    }


    private Boolean checkQuestionSelected()
    {
        if(optionSelected==0)
        {
            Toast.makeText(getActivity(), "You must complete the question before submitting.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class PreviousQuestionAction implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            questionPageInterface.moveToPreviousQuestion(questionNumber);
        }
    }

}
