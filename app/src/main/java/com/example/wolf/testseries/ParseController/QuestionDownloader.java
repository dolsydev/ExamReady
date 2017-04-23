package com.example.wolf.testseries.ParseController;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.wolf.testseries.AudioCache.AudioCache;
import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.ParseModelController.QuestionInfo;
import com.example.wolf.testseries.ParseModelController.TestInfo;
import com.example.wolf.testseries.ParseModelController.TestYearInfo;
import com.example.wolf.testseries.sqliteController.DatabaseController;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WOLF on 12-04-2015.
 */
public class QuestionDownloader
{
    private int yearId;
    private Activity activity;
    private QuestionDownloaderInterface questionDownloaderInterface;
    private DatabaseController databaseController;
    private AudioCache audioCache;

    public QuestionDownloader(int yearId, Activity activity, QuestionDownloaderInterface questionDownloaderInterface)
    {
        this.activity=activity;
        this.yearId=yearId;
        this.questionDownloaderInterface=questionDownloaderInterface;
        initialization();
        fetchQuestion();
    }

    public interface QuestionDownloaderInterface
    {
        public void questionDownloaded(Boolean success, int yearId, int questionCount);
    }

    private void initialization()
    {
        audioCache=new AudioCache(activity);
        databaseController=new DatabaseController(activity);
    }

    private void fetchQuestion()
    {
        ParseQuery<QuestionInfo> query = ParseQuery.getQuery(QuestionInfo.class);
// Define our query conditions
        query.orderByAscending("questionId");
        query.whereEqualTo("yearId", yearId);
// Execute the find asynchronously
        query.findInBackground(new FetchQuestion());
    }

    private class FetchQuestion implements FindCallback<QuestionInfo>
    {
        String tempName="";
        private int audioRequestCount=0;
        private ArrayList<QuestionInfo> questionInfos;
        @Override
        public void done(List<QuestionInfo> questionInfoList, ParseException e) {
            if (e == null)
            {
                // Access the array of results here
                this.questionInfos=(ArrayList<QuestionInfo>)questionInfoList;
                databaseController.insertQuestions((ArrayList<QuestionInfo>) questionInfoList);
                databaseController.updateTestYearStatus(DatabaseController.QUESTION_DOWNLOADED, yearId);
                for (QuestionInfo questionInfo : questionInfoList) {
                    ParseFile image = questionInfo.getImage();
                    if (image != null) {
                        Log.d("check", "URL is --> " + image.getUrl());
                    }
                }
                handleAudioDownload((ArrayList<QuestionInfo>)questionInfoList);
                for (int i = 0; i < questionInfoList.size(); i++) {
                    Log.d("check", "questionInfo ---->  " + questionInfoList.get(i).getQuestion());
                }
            } else {
                questionDownloaderInterface.questionDownloaded(false, yearId, 0);
                Log.d("check", "Exception -->: " + e.getMessage());
            }
        }

        private void handleAudioDownload(ArrayList<QuestionInfo> questionInfos)
        {

            Boolean isAudioDownloadRequested=false;
            for (QuestionInfo questionInfo : questionInfos)
            {
                ParseFile audioExplanation = questionInfo.getAudioExplanation();
                if (audioExplanation != null)
                {
                    isAudioDownloadRequested=true;
                    audioRequestCount++;
                    tempName=audioExplanation.getName();
                    audioExplanation.getDataInBackground(new FetchAudioFile(audioExplanation.getName(), this));
                }
            }
            if(!isAudioDownloadRequested)
            {
                questionDownloaderInterface.questionDownloaded(true, yearId, questionInfos.size());
            }

        }

        /*private void playTemp(String fileName)
        {
            MediaPlayer mp=new MediaPlayer();
            try{
                String audioAbsolutePath=audioCache.getDirectoryAbsolutePath()+"/"+fileName;
                Log.d("audio", "audioAbsolutePath  --->  "+audioAbsolutePath);
                mp.setDataSource(audioAbsolutePath);//Write your location here
                mp.prepare();
                mp.start();

            }catch(Exception e)
            {
                Log.d("error", "exception occurs --> "+e.getMessage());
                e.printStackTrace();
            }
        }*/

        public void responseFromAudioDownload(Boolean success)
        {
            audioRequestCount--;
            if(audioRequestCount==0)
            {
                questionDownloaderInterface.questionDownloaded(true, yearId, questionInfos.size());
//                playTemp(tempName);
            }
        }
    }

    private class FetchAudioFile implements GetDataCallback
    {
        private String fileName;
        private FetchQuestion fetchQuestion;
        FetchAudioFile(String fileName, FetchQuestion fetchQuestion)
        {
            this.fileName=fileName;
            this.fetchQuestion=fetchQuestion;
        }
        @Override
        public void done(byte[] bytes, ParseException e)
        {
            if(e==null)
            {
                fetchQuestion.responseFromAudioDownload(true);
                Log.d("audio", "downloaded succesfully");
                audioCache.saveDataToSDCard(fileName, bytes);

            }
            else
            {
                fetchQuestion.responseFromAudioDownload(false);
                Log.d("audio", "exception with e --->"+e.getMessage());
            }
        }
    }
}
