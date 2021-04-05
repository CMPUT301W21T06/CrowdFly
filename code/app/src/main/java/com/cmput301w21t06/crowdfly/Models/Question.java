package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Database.CommentController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the question class, each question is a forum entry with comments
 * Nothing has been implemented for this yet
 */
public class Question {
    private String questionID;
    private String question;
    private String username;
    private String date;
    private ArrayList<Comment> comments;
    private CommentController commentController;
    
    public Question(String question, String username, String date){
        comments = new ArrayList<Comment>();
        this.question = question;
        this.username = username;
        this.date = date;
    }

    public Question(Map<String, Object> data) {
        this.question = (String) data.get("question");
        this.questionID = (String) data.get("qID");
        this.username = (String) data.get("uID");
        this.date = (String) data.get("date");
        this.comments = new ArrayList<>();
    }

    public String getQuestionID(){
        return this.questionID;
    }
    public String getQuestion(){
        return this.question;
    }
    public String getUsername(){ return this.username;}
    public String getDate() {return this.date;}
    public int getNumReplies() {
        if (this.comments == null) return 0;
        return this.comments.size();
    }
    public ArrayList<Comment> getComments() {
        return this.comments;
    }

    public CommentController getCommentController() {
        return commentController;
    }

    public void setupQuestion(String eid, String questionID) {
        setQuestionID(questionID);
        commentController = new CommentController(eid, questionID);
    }

    public void addComment(Comment comment){
        if (comments == null) comments = new ArrayList<>();
        comments.add(comment);
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public Map<String, Object> toHashMap() {
        Map<String, Object> q = new HashMap<>();
        q.put("qID", this.questionID);
        q.put("uID", this.username);
        q.put("date", this.date);
        q.put("question", this.question);

        return q;
    }
}
