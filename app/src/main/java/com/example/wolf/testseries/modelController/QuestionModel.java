package com.example.wolf.testseries.modelController;

/**
 * Created by WOLF on 28-03-2015.
 */
public class QuestionModel {

    private int optionSelected;
    private int questionId;
    private int questionNumber;


    public QuestionModel()
    {

    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public QuestionModel(int questionId, int optionSelected)
    {
        setQuestionId(questionId);
        setOptionSelected(optionSelected);
    }


    public int getOptionSelected() {
        return optionSelected;
    }

    public void setOptionSelected(int optionSelected) {
        this.optionSelected = optionSelected;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }



}
