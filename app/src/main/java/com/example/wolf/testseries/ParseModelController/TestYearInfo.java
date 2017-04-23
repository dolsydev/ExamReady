package com.example.wolf.testseries.ParseModelController;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by WOLF on 12-04-2015.
 */
@ParseClassName("TestYearInfo")
public class TestYearInfo extends ParseObject
{

    /*status variable is used in TestYearAdapter to track the status of the test year*/
    private int status=0;
    /**/

    /*questionCount is used by Database Controller for providing the questions count provided with the Test Year Info*/
    private int questionCount=0;
    /**/

    public TestYearInfo(int yearId, int testId,  String year, int questionCount, int status)
    {
        setYearId(yearId);
        setTestId(testId);
        setYear(year);
        setQuestionCount(questionCount);
        setStatus(status);
    }

    public TestYearInfo()
    {

    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public int getYearId() {
        return getInt("yearId");
    }

    public void setYearId(int yearId) {
          put("yearId", yearId);
    }

    public int getTestId() {
        return getInt("testId");
    }

    public void setTestId(int testId) {
        put("testId", testId);
    }

    public String getYear() {
        return getString("year");
    }

    public void setYear(String year) {
        put("year", year);
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}
