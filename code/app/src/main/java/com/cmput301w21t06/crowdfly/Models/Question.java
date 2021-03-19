package com.cmput301w21t06.crowdfly.Models;

import java.util.ArrayList;

/**
 * This is the question class, each question is a forum entry with comments
 * Nothing has been implemented for this yet
 */
public class Question {
    private Integer questionID;
    private String question;
    private String username;
    private ArrayList<Comment> comments;
    
    // public void Question(int questionID,String question,String username){}

    public int getQuestionID(){
        return 0;
    }
    public String getQuestion(){
        return "";
    }
    public String getUsername(){ return "";}

    public ArrayList<Comment> getComments() {
        return new ArrayList<Comment>();
    }

    public String addComment(String comment,String username){
        return "";
    }
}
