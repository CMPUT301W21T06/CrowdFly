package com.cmput301w21t06.crowdfly.Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * This is the question class, each question is a forum entry with comments
 * Nothing has been implemented for this yet
 */
public class Question {
    private String questionID;
    private String question;
    private String username;
    private Date date;
    private ArrayList<Comment> comments;
    
    public void Question(String question, String username, Date date){
        comments = new ArrayList<>();
        this.question = question;
        this.username = username;
        this.date = date;
    }

    public String getQuestionID(){
        return this.questionID;
    }
    public String getQuestion(){
        return this.question;
    }
    public String getUsername(){ return this.username;}
    public ArrayList<Comment> getComments() {
        return this.comments;
    }
    public String addComment(String comment,String username){
        return "";
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }
}
