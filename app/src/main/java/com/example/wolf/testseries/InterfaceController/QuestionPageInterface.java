package com.example.wolf.testseries.InterfaceController;

/**
 * Created by WOLF on 04-04-2015.
 */
public interface QuestionPageInterface {
    public void moveToPreviousQuestion(int currentQuestionNumber);
    public void moveToNextQuestion(int currentQuestionNumber);
    public void optionSelected(int questionNumber, int questionId, int optionSelected);
}
