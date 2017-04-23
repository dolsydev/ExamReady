package com.example.wolf.testseries.sqliteController;

/**
 * Created by WOLF on 26-03-2015.
 */
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.wolf.testseries.AdapterController.TestYearAdapter;
import com.example.wolf.testseries.ParseModelController.CategoriesInfo;
import com.example.wolf.testseries.ParseModelController.QuestionInfo;
import com.example.wolf.testseries.ParseModelController.SubscriptionInfo;
import com.example.wolf.testseries.ParseModelController.TestInfo;
import com.example.wolf.testseries.ParseModelController.TestYearInfo;
import com.parse.ParseFile;

public class DatabaseController
{
    private final String DATABASE = "test_series";
    private final String QUESTIONS = "questions";
    private final String TEST_PROGRAMS = "test_programs";
    private final String TEST_CATEGORIES="test_categories";
    private final String TEST_YEAR="test_year";
    private final String SUBSCRIPTION_INFO="subscription_info";

    public static final int QUESTION_NOT_DOWNLOADED=0;
    public static final int QUESTION_DOWNLOADED=1;


    Context context;
    SQLiteDatabase database = null;
    ArrayList<String> results = new ArrayList<String>();

    public DatabaseController(Context context)
    {

        this.context = context;

        try
        {
            database = context.openOrCreateDatabase(DATABASE, context.MODE_PRIVATE,
                    null);

            database.execSQL("CREATE TABLE IF NOT EXISTS " + QUESTIONS
                    + " (question_id INTEGER PRIMARY KEY   AUTOINCREMENT, year_id INT,question TEXT,"
                    + " choice_one TEXT,choice_two TEXT,"
                    + " choice_three TEXT, choice_four TEXT, answer_code INT, explanation TEXT, "
                    + " image_url TEXT, audio_explanation_url TEXT, " +
                    " choice_one_image_url TEXT, choice_two_image_url TEXT, " +
                    " choice_three_image_url TEXT, choice_four_image_url TEXT, " +
                    " explanation_image_url TEXT) ;");

            database.execSQL("CREATE TABLE IF NOT EXISTS " + TEST_CATEGORIES
                    + " (category_id INTEGER PRIMARY KEY   AUTOINCREMENT," +
                    " category_name TEXT);");
            database.execSQL("CREATE TABLE IF NOT EXISTS " + TEST_PROGRAMS
                    + " (test_id INTEGER PRIMARY KEY   AUTOINCREMENT, category_id INT,"
                    + " test_name TEXT,date_uploaded DATETIME) ;");
            Log.d("check", "database created successfully");

            database.execSQL("CREATE TABLE IF NOT EXISTS " + TEST_YEAR
                    + " (year_id INTEGER PRIMARY KEY   AUTOINCREMENT, test_id int, " +
                    " year TEXT, question_status INT);");
            database.execSQL("CREATE TABLE IF NOT EXISTS " + SUBSCRIPTION_INFO
                    + " (subject_id int, " +
                    " subscription_date TEXT, subscription_days INT);");

        }
        catch (SQLiteException se)
        {
            Log.d("check",  "exception in creating the database -----> "+se);
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }
        finally
        {
// if (sampleDB != null)
// sampleDB.execSQL("DELETE FROM " + SAMPLE_TABLE_NAME);
//sampleDB.close();
        }
    }

    public void updateCategories(ArrayList<CategoriesInfo> categoriesInfos)
    {
        truncateTable(TEST_CATEGORIES);
        for(CategoriesInfo categoriesInfo : categoriesInfos)
        {
            Log.d("check", "updating --> "+categoriesInfo.getCategoryName());
            insertCategory(categoriesInfo.getCategoryId(), categoriesInfo.getCategoryName());
        }
    }

    public void insertQuestions(ArrayList<QuestionInfo> questionInfos)
    {
        for(QuestionInfo questionInfo : questionInfos)
        {
            ParseFile image=questionInfo.getImage();
            String imageURL="";
            if(image!=null)
            {
                imageURL=image.getUrl();
            }
            Log.d("check", "updating --> "+questionInfo.getQuestion());
            Log.d("audio", "audio Explanation --> "+questionInfo.getAudioExplanationURL());
            insertQuestion(questionInfo.getYearId(), questionInfo.getQuestion(), questionInfo.getChoiceOne(),
                    questionInfo.getChoiceTwo(), questionInfo.getChoiceThree(), questionInfo.getChoiceFour(),
                    questionInfo.getAnswerCode(), questionInfo.getExplanation(), imageURL, questionInfo.getAudioExplanationURL(),
                    questionInfo.getChoiceOneImage(), questionInfo.getChoiceTwoImage(), questionInfo.getChoiceThreeImage(),
                    questionInfo.getChoiceFourImage(), questionInfo.getExplanationImage());
        }
    }

    public void updateTests(ArrayList<TestInfo> testInfos)
    {
//        truncateTable(QUESTIONS);
        for(TestInfo testInfo : testInfos)
        {
            insertTestPrograms(testInfo.getTestId(), testInfo.getTestName(), testInfo.getCategoryId());
        }
    }

    public void insertSubscriptionInfos(ArrayList<SubscriptionInfo> subscriptionInfos)
    {
        truncateTable(SUBSCRIPTION_INFO);
        for(SubscriptionInfo subscriptionInfo : subscriptionInfos)
        {
            insertSubscriptionInfo(subscriptionInfo.getSubjectId(), subscriptionInfo.getSubscriptionDate(), subscriptionInfo.getSubscriptionDays());
        }
    }

    public void insertTestYears(ArrayList<TestYearInfo> testYearInfos)
    {
        for(TestYearInfo testYearInfo : testYearInfos)
        {
            insertTestYear(testYearInfo.getYearId(), testYearInfo.getYear(), testYearInfo.getTestId());
        }
    }


    private void truncateTable(String tableName)
    {
        try
        {
            database.execSQL("DELETE FROM " + tableName);
            database.execSQL("VACUUM");
        }
        catch(Exception e)
        {
            Log.d("check", "exeption while truncating the categoriesInfo ---> "+e);
        }

    }

    public void insertSubscriptionInfo(int subjectId, String subscriptionDate, int subscriptionDays)
    {
        String query="INSERT INTO " + SUBSCRIPTION_INFO
                +" (subject_id, subscription_date, subscription_days)"
                +" Values ("+subjectId+" ,'"+subscriptionDate+"',"+subscriptionDays+");";
        try
        {
            database.execSQL(query);

            Log.d("check", "---------insert_item_into_db()-----------" + query);
        }
        catch(SQLException e)
        {
            Log.d("check", "----------SQLException------------"+query+"  ---->  "+ e);
        }
    }

    public void insertQuestion(int yearId, String question, String choiceOne, String choiceTwo, String choiceThree, String choiceFour, int answerCode,
                               String explanation, String imageURL, String audioExplanationURL, String choiceOneImageURL, String choiceTwoImageURL,
                               String choiceThreeImageURL, String choiceFourImageURL, String explanationImageURL)
    {

        /*String query="INSERT INTO " + QUESTIONS
                +" (year_id, question, choice_one, choice_two, choice_three, choice_four, answer_code, explanation, image_url)"
                +" Values ('"+yearId+"','"+question+"','"+choiceOne+"','"+choiceTwo+"','"+choiceThree+"','"+
                choiceFour+"', "+answerCode+", '"+explanation+"', '"+imageURL+"');";*/

      String query="INSERT INTO " + QUESTIONS
                +" (year_id, question, choice_one, choice_two, choice_three, choice_four, answer_code," +
              " explanation, image_url, audio_explanation_url, choice_one_image_url, choice_two_image_url, " +
              " choice_three_image_url, choice_four_image_url, explanation_image_url)"
                +" Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try
        {
            database.beginTransactionNonExclusive();
            // db.beginTransaction();

            SQLiteStatement stmt = database.compileStatement(query);
            stmt.bindLong(1, yearId);
            stmt.bindString(2, question);
            stmt.bindString(3, choiceOne);
            stmt.bindString(4, choiceTwo);
            stmt.bindString(5, choiceThree);
            stmt.bindString(6, choiceFour);
            stmt.bindLong(7, answerCode);
            stmt.bindString(8, explanation);
            stmt.bindString(9, imageURL);
            stmt.bindString(10, audioExplanationURL);
            stmt.bindString(11, choiceOneImageURL);
            stmt.bindString(12, choiceTwoImageURL);
            stmt.bindString(13, choiceThreeImageURL);
            stmt.bindString(14, choiceFourImageURL);
            stmt.bindString(15, explanationImageURL);

            stmt.execute();
            stmt.clearBindings();
            database.setTransactionSuccessful();
            database.endTransaction();
//            database.execSQL(query);
//
            Log.d("check", "---------insert_item_into_db()-----------"+ query);
        }
        catch(SQLException e)
        {
            Log.d("check", "----------SQLException------------"+query+"  ---->  "+ e);
        }
    }

    public void insertCategory(int categoryId, String categoryName)
    {

        String query="INSERT INTO " + TEST_CATEGORIES
                +" (category_id, category_name)"
                +" Values ("+categoryId+" ,'"+categoryName+"');";
        try
        {
            database.execSQL(query);

            Log.d("check", "---------insert_item_into_db()-----------"+ query);
        }
        catch(SQLException e)
        {
            Log.d("check", "----------SQLException------------"+query+"  ---->  "+ e);
        }
    }



    public void insertTestPrograms(int testId, String testName, int categoryId)
    {

        String query="INSERT INTO " + TEST_PROGRAMS
                +" (test_id, test_name, category_id)"
                +" Values ("+testId+", '"+testName+"',"+categoryId+");";
        try
        {
            database.execSQL(query);

            Log.d("check", "---------insert_item_into_db()-----------"+ query);
        }
        catch(SQLException e)
        {
            Log.d("check", "----------SQLException------------"+query+"  ---->  "+ e);
        }
    }

    public void insertTestYear(int yearId, String year, int testId)
    {

        String query="INSERT INTO " + TEST_YEAR
                +" (year_id, year, test_id)"
                +" Values ("+yearId+", '"+year+"',"+testId+");";
        try
        {
            database.execSQL(query);

            Log.d("check", "---------insert_item_into_db()-----------"+ query);
        }
        catch(SQLException e)
        {
            Log.d("check", "----------SQLException------------"+query+"  ---->  "+ e);
        }
    }

    public void deleteTestYearQuestions(int yearId)
    {
        String query="DELETE FROM " + QUESTIONS
                +" WHERE year_id="+yearId;
        try
        {
            database.execSQL(query);

            Log.d("check", "---------DELETE FROM DB-----------"+ query);
        }
        catch(SQLException e)
        {
            Log.d("check", "---------DELETE -SQLException------------"+query+"  ---->  "+ e);
        }
    }



    public Cursor selectAllQuestions()
    {
        Log.d("check", "select method entered");
        try
        {
            Cursor cursor = database.rawQuery("SELECT * FROM "
                    + QUESTIONS , null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String question = cursor.getString(cursor
                                .getColumnIndex("question"));
                        String choiceOne = cursor.getString(cursor.getColumnIndex("choice_one"));
                        String choiceTwo = cursor.getString(cursor.getColumnIndex("choice_two"));
                        String choiceThree = cursor.getString(cursor.getColumnIndex("choice_three"));
                        String choiceFour = cursor.getString(cursor.getColumnIndex("choice_four"));
                        String testId=cursor.getString(cursor.getColumnIndex("year_id"));
                        int id=cursor.getInt(cursor.getColumnIndex("question_id"));
                        Log.d("check", "testId --> "+testId+
                                    "---------question --> "+question+
                                "----------- choiceOne "+choiceOne+
                        "   choiceTwo --> "+choiceTwo+
                        "  choiceThree--> "+choiceThree+
                        "   choiceFour  ---> "+choiceFour+
                        " id ---> "+id
                        );

//                        results.add("" + firstName + ",Age: " + age);
                    } while (cursor.moveToNext());
                }
            }
            else
            {
                Log.d("check", "cursor came null");
            }
            Log.d("check", "---------select_item_from_db()-----------"+" ");

            return cursor;
        }
        catch(Exception e)
        {
            Log.d("check", "exception is --> "+e);
            return null;
        }
    }

    public Cursor selectAllCategories()
    {
        Log.d("check", "select method entered");
        try
        {
            Cursor cursor = database.rawQuery("SELECT * FROM "
                    + TEST_CATEGORIES , null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String categoryName = cursor.getString(cursor
                                .getColumnIndex("category_name"));
                        String categoryType = cursor.getString(cursor.getColumnIndex("category_type"));
                        String categoryId = cursor.getString(cursor.getColumnIndex("category_id"));

                        Log.d("check", "---------categoryName --> "+categoryName+
                                        "----------- categoryType "+categoryType+
                                        "   categoryId --> "+categoryId

                        );

//                        results.add("" + firstName + ",Age: " + age);
                    } while (cursor.moveToNext());
                }
            }
            else
            {
                Log.d("check", "cursor came null");
            }
            Log.d("check", "---------select_item_from_db()-----------"+" ");

            return cursor;
        }
        catch(Exception e)
        {
            Log.d("check", "exception is --> "+e);
            return null;
        }
    }

    public Cursor selectAllTests()
    {
        Log.d("check", "select method entered");
        try
        {
            Cursor cursor = database.rawQuery("SELECT * FROM "
                    + TEST_PROGRAMS , null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String testName = cursor.getString(cursor
                                .getColumnIndex("test_name"));
                        String categoryId = cursor.getString(cursor.getColumnIndex("category_id"));
                        String testId = cursor.getString(cursor.getColumnIndex("test_id"));
                        Log.d("check", "---------testName --> "+testName+
                                        "   categoryId --> "+categoryId+
                                        "testId ----. "+testId

                        );

//                        results.add("" + firstName + ",Age: " + age);
                    } while (cursor.moveToNext());
                }
            }
            else
            {
                Log.d("check", "cursor came null");
            }
            Log.d("check", "---------select_item_from_db()-----------"+" ");

            return cursor;
        }
        catch(Exception e)
        {
            Log.d("check", "exception is --> "+e);
            return null;
        }
    }


    public Cursor selectJoint()
    {
        Log.d("check", "select method entered");
        try
        {
            Cursor cursor = database.rawQuery("SELECT t.test_name AS testName, q.question AS question FROM "
                    + TEST_PROGRAMS +" AS t INNER JOIN "+QUESTIONS+" AS q ON t.test_id=q.test_id" , null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String testName = cursor.getString(cursor
                                .getColumnIndex("testName"));
                        String question = cursor.getString(cursor.getColumnIndex("question"));
                        Log.d("check", "---------testName --> "+testName+
                                        "   question --> "+question

                        );


//                        results.add("" + firstName + ",Age: " + age);
                    } while (cursor.moveToNext());
                }
            }
            else
            {
                Log.d("check", "cursor came null");
            }
            Log.d("check", "---------select_item_from_db()-----------"+" ");

            return cursor;
        }
        catch(Exception e)
        {
            Log.d("check", "exception is --> "+e);
            return null;
        }
    }

    public QuestionInfo getQuestion(int questionNumber, int yearId)
    {
        QuestionInfo questionInfo=null;
        try
        {
            String query=generateQueryForFetchingQuestion(questionNumber, yearId);
            Log.d("query", query);
            Cursor cursor = database.rawQuery(query , null);
            Log.d("query", "cursor count --> "+cursor.getCount());
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int questionId=cursor.getInt(cursor.getColumnIndex("questionId"));
                    String question = cursor.getString(cursor
                            .getColumnIndex("question"));
                    String choiceOne=cursor.getString(cursor.getColumnIndex("choiceOne"));
                    String choiceTwo=cursor.getString(cursor.getColumnIndex("choiceTwo"));
                    String choiceThree=cursor.getString(cursor.getColumnIndex("choiceThree"));
                    String choiceFour=cursor.getString(cursor.getColumnIndex("choiceFour"));
                    int answerCode=cursor.getInt(cursor.getColumnIndex("answerCode"));
                    String explanation=cursor.getString(cursor.getColumnIndex("explanation"));
                    String imageURL=cursor.getString(cursor.getColumnIndex("imageURL"));
                    String audioExplanationURL=cursor.getString(cursor.getColumnIndex("audioExplanationURL"));
                    String choiceOneImageURL=cursor.getString(cursor.getColumnIndex("choiceOneImageURL"));
                    String choiceTwoImageURL=cursor.getString(cursor.getColumnIndex("choiceTwoImageURL"));
                    String choiceThreeImageURL=cursor.getString(cursor.getColumnIndex("choiceThreeImageURL"));
                    String choiceFourImageURL=cursor.getString(cursor.getColumnIndex("choiceFourImageURL"));
                    String explanationImageURL=cursor.getString(cursor.getColumnIndex("explanationImageURL"));
                    Log.d("check", "question --> "+question+"-----------"+choiceOne
                    +choiceTwo+choiceThree+choiceFour+"answer Code --> "+answerCode+"audio explaanation  -->"+audioExplanationURL);
                    questionInfo=new QuestionInfo(yearId, questionId, question, choiceOne, choiceTwo, choiceThree, choiceFour, answerCode,
                            explanation, imageURL, choiceOneImageURL, choiceTwoImageURL, choiceThreeImageURL, choiceFourImageURL,
                            explanationImageURL);
                    questionInfo.setAudioExplanationURL(audioExplanationURL);
                }
            }
            else
            {
                Log.d("check", "cursor came null");
            }
        }
        catch(Exception e)
        {
            Log.d("check", "exception is coming in getQuestion --> "+e);
        }
        return questionInfo;
    }

    private String generateQueryForFetchingQuestion(int questionNumber, int yearId)
    {
        int offset=questionNumber-1;
        String query="SELECT q.question_id AS questionId, " +
                "q.question AS question, " +
                " q.choice_one AS choiceOne, " +
                " q.choice_two AS choiceTwo, " +
                " q.choice_three AS choiceThree, " +
                " q.choice_four AS choiceFour, " +
                " q.answer_code AS answerCode, " +
                " q.explanation, "+
                " q.image_url AS imageURL, "+
                " q.audio_explanation_url AS audioExplanationURL, " +
                " q.choice_one_image_url AS choiceOneImageURL, " +
                " q.choice_two_image_url AS choiceTwoImageURL, " +
                " q.choice_three_image_url AS choiceThreeImageURL, " +
                " q.choice_four_image_url AS choiceFourImageURL, " +
                " q.explanation_image_url AS explanationImageURL "+
                " FROM "+QUESTIONS+" AS q "+
                " WHERE year_id = "+yearId +" ORDER BY question_id LIMIT "+ offset +" , 1 ";
        return query;
    }

    private String generateQueryForFetchingCategories()
    {
        String query="SELECT category_id AS categoryId, "+
                " category_name AS categoryName " +
                " FROM "+TEST_CATEGORIES;
        return query;
    }

    public ArrayList<CategoriesInfo> fetchCategories()
    {
        ArrayList<CategoriesInfo> categoriesInfoList=new ArrayList<CategoriesInfo>();
        try
        {
            Cursor cursor = database.rawQuery(generateQueryForFetchingCategories() , null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do
                    {
                        int categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
                        String categoryName = cursor.getString(cursor
                                .getColumnIndex("categoryName"));
                        CategoriesInfo categoriesInfo=new CategoriesInfo(categoryId, categoryName);
                        categoriesInfoList.add(categoriesInfo);
                        Log.d("check", "categoryId --> "+categoryId+" categoryName--> "+categoryName);
                    } while (cursor.moveToNext());
                }
            }
            else
            {
                Log.d("check", "cursor came null");
            }
        }
        catch(Exception e)
        {
            Log.d("check", "exception is --> "+e);
        }

        return categoriesInfoList;
    }

    public ArrayList<SubscriptionInfo> fetchSubscriptionInfo()
    {
        ArrayList<SubscriptionInfo> subscriptionInfos=new ArrayList<SubscriptionInfo>();
        try
        {
            Cursor cursor = database.rawQuery(generateQueryForFetchSubscriptionInfo() , null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do
                    {
                        int subjectId = cursor.getInt(cursor.getColumnIndex("subjectId"));
                        String subscriptionDate = cursor.getString(cursor
                                .getColumnIndex("subscriptionDate"));
                        int subscriptionDays=cursor.getInt(cursor.getColumnIndex("subscriptionDays"));
                        SubscriptionInfo subscriptionInfo=new SubscriptionInfo(subscriptionDate, subscriptionDays, subjectId);
                        subscriptionInfos.add(subscriptionInfo);
                        Log.d("check", "subjectId --> "+subjectId+" subscriptionDays--> "+subscriptionDays);
                    } while (cursor.moveToNext());
                }
            }
            else
            {
                Log.d("check", "cursor came null");
            }
        }
        catch(Exception e)
        {
            Log.d("check", "exception is --> "+e);
        }

        return subscriptionInfos;
    }

    public SubscriptionInfo fetchSubscriptionInfo(int subjectId)
    {
        SubscriptionInfo subscriptionInfo=null;
        try
        {
            Cursor cursor = database.rawQuery(generateQueryForFetchSubscriptionInfo(subjectId) , null);
            if (cursor != null) {
                if (cursor.moveToFirst())
                {
                        String subscriptionDate = cursor.getString(cursor
                                .getColumnIndex("subscriptionDate"));
                        int subscriptionDays=cursor.getInt(cursor.getColumnIndex("subscriptionDays"));
                        subscriptionInfo=new SubscriptionInfo(subscriptionDate, subscriptionDays, subjectId);
                        Log.d("check", "subjectId --> "+subjectId+" subscriptionDays--> "+subscriptionDays);
                }
            }
            else
            {
                Log.d("check", "cursor came null");
            }
        }
        catch(Exception e)
        {
            Log.d("check", "exception is --> "+e);
        }

        return subscriptionInfo;
    }

    private String generateQueryForFetchTestNames()
    {
        String query="SELECT test_id AS testId, "+
                " test_name AS testName " +
                " FROM "+TEST_PROGRAMS;
        return query;
    }



    private String generateQueryForFetchTestYears(int testId)
    {
        String query="SELECT year_id AS yearId, "+
                " year , question_status AS questionStatus " +
                " FROM "+TEST_YEAR +" WHERE test_id = "+testId;
        return query;
    }

    private String generateQueryForFetchSubscriptionInfo()
    {
        String query="SELECT subject_id AS subjectId, "+
                " subscription_date AS subscriptionDate, " +
                " subscription_days AS subscriptionDays "+
                "FROM "+SUBSCRIPTION_INFO;
        return query;
    }

    private String generateQueryForFetchSubscriptionInfo(int subjectId)
    {
        String query="SELECT subject_id AS subjectId, "+
                " subscription_date AS subscriptionDate, " +
                " subscription_days AS subscriptionDays "+
                "FROM "+SUBSCRIPTION_INFO +
                " WHERE subject_id = "+subjectId+" ;";
        return query;
    }

    private String generateQueryForCountingQuestions(int yearId)
    {
        String query="SELECT COUNT(question_id) AS questionsCount FROM " +
                QUESTIONS +" WHERE year_id = "+yearId;
        return query;
    }

    private int countQuestionsAvailableForTest(int yearId)
    {
        int questionsCount=0;
        try
        {
            Cursor cursor=database.rawQuery(generateQueryForCountingQuestions(yearId), null);
            if(cursor==null || !cursor.moveToFirst())
            {
                return questionsCount;
            }
            questionsCount= cursor.getInt(cursor.getColumnIndex("questionsCount"));
        }
        catch(Exception e)
        {
            Log.d("check", "exception catched --- > "+e);
        }
        Log.d("questionCount", "questionCount -->"+questionsCount+" for yearId --> "+yearId);
        selectAllQuestions();
        return questionsCount;
    }

    public ArrayList<TestInfo> fetchTestNames()
    {
        ArrayList<TestInfo> testInfoList=new ArrayList<TestInfo>();
        try
        {
            Cursor cursor=database.rawQuery(generateQueryForFetchTestNames(), null);
            if(cursor==null)
            {
                return testInfoList;
            }
            if(!cursor.moveToFirst())
            {
                return testInfoList;
            }
            do
            {
                int testId=cursor.getInt(cursor.getColumnIndex("testId"));
                String testName=cursor.getString(cursor.getColumnIndex("testName"));
                Log.d("check", "testId --> "+testId+" testName --> "+testName);
                TestInfo testInfo=new TestInfo(testId, testName, countQuestionsAvailableForTest(testId));
                testInfoList.add(testInfo);
            }
            while (cursor.moveToNext());
        }
        catch (Exception e)
        {
            Log.d("check", "exception is --> "+e);
        }
        return testInfoList;
    }

    public ArrayList<TestYearInfo> fetchTestYearInfo(int testId)
    {
        ArrayList<TestYearInfo> testYearInfoList=new ArrayList<TestYearInfo>();
        try
        {
            Cursor cursor=database.rawQuery(generateQueryForFetchTestYears(testId), null);
            if(cursor==null)
            {
                return testYearInfoList;
            }
            if(!cursor.moveToFirst())
            {
                return testYearInfoList;
            }
            do
            {
                int yearId=cursor.getInt(cursor.getColumnIndex("yearId"));
                String year=cursor.getString(cursor.getColumnIndex("year"));
                int questionStatus=cursor.getInt(cursor.getColumnIndex("questionS" +
                        "tatus"));
                Log.d("check", "yearId --> "+yearId+" year --> "+year);
                int questionCount=countQuestionsAvailableForTest(yearId);
                TestYearInfo testYearInfo=new TestYearInfo(yearId, testId, year, questionCount, questionStatus);
                if(questionCount>0)
                {
                    testYearInfo.setStatus(TestYearAdapter.STATUS_DOWNLOADED);
                }
                testYearInfoList.add(testYearInfo);
            }
            while (cursor.moveToNext());
        }
        catch (Exception e)
        {
            Log.d("check", "exception is --> "+e);
        }
        return testYearInfoList;
    }

    public void updateTestYearStatus(int questionStatus, int yearId)
    {
        String query="UPDATE " +
                TEST_YEAR +" SET question_status = "+questionStatus+
                " WHERE  year_id = "+yearId;
        try
        {
            database.execSQL(query);

            Log.d("check", "---------update-----------"+ query);
        }
        catch(SQLException e)
        {
            Log.d("check", "----------SQLException------------"+query+"  ---->  "+ e);
        }
    }




}
