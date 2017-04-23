package com.example.wolf.testseries.ParseModelController;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by WOLF on 29-03-2015.
 */
@ParseClassName("TestInfo")
public class TestInfo extends ParseObject
{
    private int questionsCount;
    private Boolean isSelected=false;
    public int getQuestionsCount() {
        return questionsCount;
    }

    public void setQuestionsCount(int questionsCount) {
        this.questionsCount = questionsCount;
    }


    public TestInfo()
    {
        super();
    }

    public TestInfo(int testId, int categoryId, String testName)
    {
        super();
        setTestId(testId);
        setCategoryId(categoryId);
        setTestName(testName);
    }

    public TestInfo(int testId, String testName, int questionsCount)
    {
        super();
        setTestId(testId);
        setTestName(testName);
        setQuestionsCount(questionsCount);
    }

    public int getTestId() {
       return getInt("testId");
    }

    public void setTestId(int testId) {
        put("testId", testId);
    }

    public int getCategoryId() {
        return getInt("categoryId");
    }

    public void setCategoryId(int categoryId) {
        put("categoryId", categoryId);
    }

    public String getTestName() {
        return getString("testName");
    }

    public void setTestName(String testName) {
        put("testName", testName);
    }


    public Boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }
}
