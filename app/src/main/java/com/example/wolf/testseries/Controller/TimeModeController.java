package com.example.wolf.testseries.Controller;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.InterfaceController.QuestionPageInterface;
import com.example.wolf.testseries.InterfaceController.TimeModeInterface;
import com.example.wolf.testseries.ParseModelController.QuestionInfo;
import com.example.wolf.testseries.R;
import com.example.wolf.testseries.fragmentController.QuestionCompletionFragment;
import com.example.wolf.testseries.fragmentController.QuestionWithImage;
import com.example.wolf.testseries.fragmentController.Set_Time_Screen;
import com.example.wolf.testseries.fragmentController.TestResultController;
import com.example.wolf.testseries.fragmentController.Test_Page;
import com.example.wolf.testseries.modelController.QuestionModel;
import com.example.wolf.testseries.sqliteController.DatabaseController;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by WOLF on 14-04-2015.
 */
public class TimeModeController implements QuestionPageInterface, QuestionCompletionFragment.QuestionCompletionFragmentInterface, TestResultController.TestResultControllerInterface
{
    private Activity activity;
    private int questionsCount, yearId;
    private ArrayList<QuestionModel> questionInfos;
    public static String RESULT_PAGE_TAG="RESULT_PAGE_TAG";
    public static String QUESTION_COMPLETION_TAG="QUESTION_COMPLETION_TAG";

    private static final String FORMAT = "%02d:%02d:%02d";

    private static final int QUESTION_WITH_IMAGE=1;
    private static final int PLAIN_QUESTION=2;
    private TimeModeInterface timeModeInterface;
    public static CountDownTimer countDownTimer;
    private String  currentTime;
    private boolean isTimerFinished;

    public TimeModeController(Activity activity, int yearId, int questionsCount)
    {
        this.activity=activity;
        this.questionsCount=questionsCount;
        this.yearId = yearId;
        initialization();
    }

    public void setTimeModeInterface(TimeModeInterface timeModeInterface)
    {
        this.timeModeInterface=timeModeInterface;
    }

    private void initialization()
    {
        questionInfos=new ArrayList<QuestionModel>();
        isTimerFinished=false;
        paintTimer();
        /**/
        DatabaseController databaseController=new DatabaseController(activity);
        databaseController.selectAllQuestions();
    }

    private void paintTimer()
    {
        Fragment fragment=null;
        Set_Time_Screen timerFragment=new Set_Time_Screen();
        timerFragment.setParentReference(this);
        fragment=timerFragment;
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment, "Set_Time_Screen");
//        fragmentTransaction.addToBackStack("Set_Time_Screen");
        fragmentTransaction.commit();
    }

    public void paintFirstQuestion(int hours, int minutes)
    {
        moveToNextQuestion(0);
        startTimer(hours, minutes);
    }



    @Override
    public void optionSelected(int questionNumber, int questionId, int optionSelected)
    {
        QuestionModel questionInfo=getSavedResultForQuestionNumber(questionNumber);
        questionInfo.setOptionSelected(optionSelected);
        questionInfo.setQuestionId(questionId);
    }


    private QuestionModel getSavedResultForQuestionNumber(int questionNumber)
    {
        for(QuestionModel questionInfo : questionInfos)
        {
            if(questionInfo.getQuestionNumber()==questionNumber)
            {
                return questionInfo;
            }
        }
        QuestionModel questionInfo=new QuestionModel() ;
        questionInfo.setQuestionNumber(questionNumber);
        questionInfos.add(questionInfo);
        return questionInfo;
    }

    @Override
    public void moveToPreviousQuestion(int currentQuestionNumber) {
        FragmentManager fm = activity.getFragmentManager();
        if (fm.getBackStackEntryCount() > 0)
        {
            Log.d("MainActivity", "popping backstack");
            fm.popBackStack();
        }
    }

    @Override
    public void moveToNextQuestion(int currentQuestionNumber)
    {
        if(currentQuestionNumber==questionsCount)
        {
            //TODO
            Log.d("check", "no more question Available");
            navigateToQuestionCompletionPage();
            return;
        }
        navigateToQuestion(++currentQuestionNumber);
    }

    private void navigateToResultPage()
    {
        double percentage=calculateResult();
        Log.d("percent","percent  ---> "+percentage );
        TestResultController testResultController=TestResultController.newInstance(percentage);
        testResultController.setTestResultInterface(this);
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, testResultController, RESULT_PAGE_TAG);
        fragmentTransaction.addToBackStack(RESULT_PAGE_TAG);
        fragmentTransaction.commit();
    }

    private void navigateToQuestion(int questionNumber)
    {
        Fragment fragment=null;
//        switch(questionType(questionNumber))
//        {
//            case PLAIN_QUESTION:
//                Test_Page questionPage=Test_Page.newInstance(questionNumber, yearId, getSavedResultForQuestionNumber(questionNumber).getOptionSelected(), this);
//                questionPage.setQuestionPageInterface(this);
//                fragment=questionPage;
//                break;
//            case QUESTION_WITH_IMAGE:
//                QuestionWithImage questionWithImage=QuestionWithImage.newInstance(questionNumber, yearId, getSavedResultForQuestionNumber(questionNumber).getOptionSelected(), this);
//                questionWithImage.setQuestionPageInterface(this);
//                fragment=questionWithImage;
//                break;
//        }
        QuestionWithImage questionWithImage=QuestionWithImage.newInstance(questionNumber, yearId, getSavedResultForQuestionNumber(questionNumber).getOptionSelected(), this);
        questionWithImage.setQuestionPageInterface(this);
        fragment=questionWithImage;
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        if(questionNumber==1)
        {
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
//
        fragmentTransaction.replace(R.id.frame_container, fragment, ""+questionNumber);
        fragmentTransaction.addToBackStack(""+questionNumber);
        fragmentTransaction.commit();


            clearTimerScreen();

    }

    private void clearTimerScreen()
    {
        Log.d("clear", "clearing timer");
        Fragment fragment =  activity.getFragmentManager().findFragmentByTag("Set_Time_Screen");
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    private double calculateResult()
    {
        int correctOptions=0, unAttemptedQuestions=0;

        for(int i=0; i< questionsCount; i++)
        {
            int originalQuestionNumber=i+1;
            QuestionInfo questionInfo=fetchQuestionInfo(originalQuestionNumber, yearId);
            QuestionModel questionModel=getSavedResultForQuestionNumber(originalQuestionNumber);
            if(questionModel.getOptionSelected()==0)
            {
                unAttemptedQuestions++;
                continue;
            }
            if(questionModel.getOptionSelected()==questionInfo.getAnswerCode())
            {
                correctOptions++;
            }
        }
        Log.d("result", "correct options --> "+correctOptions+"  unAttemptedQuestions --> "+unAttemptedQuestions);
        return calculatePercentage(correctOptions);
    }

    private double calculatePercentage(int correctQuestions)
    {
        double percentage=((double)(correctQuestions*100)/questionsCount);
        return percentage;
    }

    private QuestionInfo fetchQuestionInfo(int questionNumber, int testId)
    {
        DatabaseController databaseController=new DatabaseController(activity);
        QuestionInfo questionInfo=databaseController.getQuestion(questionNumber, testId);
        return questionInfo;
    }


    private int questionType(int questionNumber)
    {
        QuestionInfo questionInfo=fetchQuestionInfo(questionNumber, yearId);
        if(questionInfo.getImageURL()!=null && !questionInfo.getImageURL().equals(""))
        {
            return QUESTION_WITH_IMAGE;
        }
        return PLAIN_QUESTION;
    }

    private void startTimer(int hour, int minute)
    {

        int milliseconds=hour*60*60*1000+minute*60*1000;

        if(countDownTimer!=null)
        {
            countDownTimer.cancel();
        }
        countDownTimer= new CountDownTimer(milliseconds, 1000) {

            public void onTick(long millisUntilFinished)
            {
//                testing.setText("" + millisUntilFinished / 1000);
               currentTime="" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                updateTime(currentTime);
                Log.d("time", currentTime);
            }

            public void onFinish() {
                updateTime("done!");
                isTimerFinished=true;
                navigateToQuestionCompletionPage();
            }
        }.start();
    }

    private void  updateTime(String time)
    {
        if(timeModeInterface==null)
        {
            return;
        }
        timeModeInterface.updateTime(time);
    }

    public String getCurrentTime()
    {
        return currentTime;
    }

    private void navigateToQuestionCompletionPage()
    {
        QuestionCompletionFragment questionCompletionFragment=QuestionCompletionFragment.newInstance();
        questionCompletionFragment.setQuestionCompletionFragmentInterface(this);
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, questionCompletionFragment, QUESTION_COMPLETION_TAG);
        fragmentTransaction.addToBackStack(QUESTION_COMPLETION_TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void showResult()
    {
        countDownTimer.cancel();
        navigateToResultPage();
    }

    @Override
    public Boolean shouldAllowCrossCheck() {
        if(isTimerFinished)
        {
            CentralController.showToast(activity, "Time has been over!", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }


    @Override
    public void reviewQuiz()
    {
        AnswerReviewController answerReviewController= new AnswerReviewController(activity, yearId, questionsCount, questionInfos);
    }
}
