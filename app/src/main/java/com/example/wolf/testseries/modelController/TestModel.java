package com.example.wolf.testseries.modelController;
/**
 * Created by WOLF on 29-03-2015.
 */
public class TestModel
{
    public TestModel(int testId, String testName, int questionsCount) {
        setTestId(testId);
        setTestName(testName);
        setQuestionsCount(questionsCount);
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    private int testId;

    public int getQuestionsCount() {
        return questionsCount;
    }

    public void setQuestionsCount(int questionsCount) {
        this.questionsCount = questionsCount;
    }

    private int questionsCount;
    private String testName;
}
