package com.example.wolf.testseries.ParseModelController;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by WOLF on 29-03-2015.
 */
@ParseClassName("QuestionInfo")
public class QuestionInfo extends ParseObject
{

    private String imageURL;

    private String choiceOneImageURL,
    choiceTwoImageURL,
    choiceThreeImageURL,
    choiceFourImageURL,
    explanationImageURL
            ;
    private String audioExplanationURL;
    public QuestionInfo()
    {
        super();
    }

    public QuestionInfo(int yearId, int questionId, String question, String choiceOne, String choiceTwo, String choiceThree, String choiceFour
    , int answerCode, String explanation, String imageURL, String choiceOneImageURL, String choiceTwoImageURL,
                        String choiceThreeImageURL, String choiceFourImageURL, String explanationImageURL)
    {
        super();
        setYearId(yearId);
        setQuestionId(questionId);
        setQuestion(question);
        setChoiceOne(choiceOne);
        setChoiceTwo(choiceTwo);
        setChoiceThree(choiceThree);
        setChoiceFour(choiceFour);
        setAnswerCode(answerCode);
        setExplanation(explanation);
        setImageURL(imageURL);
        setChoiceOneImageURL(choiceOneImageURL);
        setChoiceTwoImageURL(choiceTwoImageURL);
        setChoiceThreeImageURL(choiceThreeImageURL);
        setChoiceFourImageURL(choiceFourImageURL);
        setExplanationImageURL(explanationImageURL);
    }



    public String getQuestion() {
        return getString("question");
    }

    public void setQuestion(String question) {
        put("question", question);
    }

    public String getChoiceOne() {
        return getString("choiceOne");
    }

    public void setChoiceOne(String choiceOne) {
        put("choiceOne", choiceOne);
    }

    public String getChoiceTwo() {
        return getString("choiceTwo");
    }

    public void setChoiceTwo(String choiceTwo) {
        put("choiceTwo", choiceTwo);
    }

    public String getChoiceThree() {
        return getString("choiceThree");
    }

    public void setChoiceThree(String choiceThree) {
        put("choiceThree", choiceThree);
    }

    public String getChoiceFour() {
        return getString("choiceFour");
    }

    public void setChoiceFour(String choiceFour) {
        put("choiceFour", choiceFour);
    }

    public int getAnswerCode() {
        return getInt("answerCode");
    }

    public void setAnswerCode(int answerCode) {
        put("answerCode", answerCode);
    }

    public String getExplanation() {
        return getString("explanation");
    }

    public void setExplanation(String explanation) {
        put("explanation", explanation);
    }

    public int getYearId() {
        return getInt("yearId");
    }

    public void setYearId(int yearId) {
        put("yearId", yearId);
    }

    public int getQuestionId()
    {
        return getInt("questionId");
    }

    public void setQuestionId(int questionId)
    {
        put("questionId", questionId);
    }

    public void setImage(ParseFile image)
    {
        put("image", image);
    }

    public ParseFile getImage()
    {
        return getParseFile("image");
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ParseFile getAudioExplanation()
    {
        return getParseFile("audioExplanation");
    }

    public String getAudioExplanationURL()
    {
        ParseFile audioExplanation=getAudioExplanation();
        if(audioExplanation!=null)
        {
            return audioExplanation.getName();
        }
        else if(audioExplanationURL!=null)
        {
            return audioExplanationURL;
        }
        return "";
    }

    public void setAudioExplanationURL(String audioExplanationURL)
    {
        this.audioExplanationURL=audioExplanationURL;
    }

    public void setChoiceOneImage(ParseFile choiceOneImage)
    {
        put("choiceOneImage", choiceOneImage);
    }

    public String getChoiceOneImage()
    {
        ParseFile image= getParseFile("choiceOneImage");
        if(image!=null)
        {
            return image.getUrl();
        }
        return "";
    }

    public void setChoiceTwoImage(ParseFile choiceTwoImage)
    {
        put("choiceTwoImage", choiceTwoImage);
    }

    public String getChoiceTwoImage()
    {
        ParseFile image= getParseFile("choiceTwoImage");
        if(image!=null)
        {
            return image.getUrl();
        }
        return "";
    }

    public void setChoiceThreeImage(ParseFile choiceThreeImage)
    {
        put("choiceThreeImage", choiceThreeImage);
    }

    public String getChoiceThreeImage()
    {
        ParseFile image= getParseFile("choiceThreeImage");
        if(image!=null)
        {
            return image.getUrl();
        }
        return "";
    }

    public void setChoiceFourImage(ParseFile choiceFourImage)
    {
        put("choiceFourImage", choiceFourImage);
    }

    public String getChoiceFourImage()
    {
        ParseFile image= getParseFile("choiceFourImage");
        if(image!=null)
        {
            return image.getUrl();
        }
        return "";
    }

    public void setExplanationImage(ParseFile explanationImage)
    {
        put("explanationImage", explanationImage);
    }

    public String getExplanationImage()
    {
        ParseFile image= getParseFile("explanationImage");
        if(image!=null)
        {
            return image.getUrl();
        }
        return "";
    }

    public String getChoiceOneImageURL() {
        return choiceOneImageURL;
    }

    public void setChoiceOneImageURL(String choiceOneImageURL) {
        this.choiceOneImageURL = choiceOneImageURL;
    }

    public String getChoiceTwoImageURL() {
        return choiceTwoImageURL;
    }

    public void setChoiceTwoImageURL(String choiceTwoImageURL) {
        this.choiceTwoImageURL = choiceTwoImageURL;
    }

    public String getChoiceThreeImageURL() {
        return choiceThreeImageURL;
    }

    public void setChoiceThreeImageURL(String choiceThreeImageURL) {
        this.choiceThreeImageURL = choiceThreeImageURL;
    }

    public String getChoiceFourImageURL() {
        return choiceFourImageURL;
    }

    public void setChoiceFourImageURL(String choiceFourImageURL) {
        this.choiceFourImageURL = choiceFourImageURL;
    }

    public String getExplanationImageURL() {
        return explanationImageURL;
    }

    public void setExplanationImageURL(String explanationImageURL) {
        this.explanationImageURL = explanationImageURL;
    }
}



