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

    /***
     * Constructor
     * @param question
     * @param username
     * @param date
     */
    public Question(String question, String username, String date){
        comments = new ArrayList<Comment>();
        this.question = question;
        this.username = username;
        this.date = date;
    }

    /***
     * Constructor using Map.
     * Used for getting data from database.
     * @param data
     */
    public Question(Map<String, Object> data) {
        this.question = (String) data.get("question");
        this.questionID = (String) data.get("qID");
        this.username = (String) data.get("uID");
        this.date = (String) data.get("date");
        this.comments = new ArrayList<>();
    }

    /***
     * Gets questionID
     * @return questionID
     */
    public String getQuestionID(){
        return this.questionID;
    }

    /***
     * gets question description
     * @return question description
     */
    public String getQuestion(){
        return this.question;
    }

    /***
     * Gets userID of question
     * @return userID
     */
    public String getUsername(){ return this.username;}

    /***
     * Gets date of question
     * @return date
     */
    public String getDate() {return this.date;}

    /***
     * Gets number of comments
     * @return number of comments
     */
    public int getNumReplies() {
        if (this.comments == null) return 0;
        return this.comments.size();
    }

    /***
     * Gets all comments
     * @return comments
     */
    public ArrayList<Comment> getComments() {
        return this.comments;
    }

    /***
     * Gets comment controller
     * @return commentController
     */
    public CommentController getCommentController() {
        return commentController;
    }

    /***
     * Sets up questionID and commentController
     * @param eid
     * @param questionID
     */
    public void setupQuestion(String eid, String questionID) {
        setQuestionID(questionID);
        commentController = new CommentController(eid, questionID);
    }

    /***
     * Adds comments
     * @param comments
     */
    public void addComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    /***
     * Adds a comment
     * @param comment
     */
    public void addComment(Comment comment){
        if (comments == null) comments = new ArrayList<>();
        comments.add(comment);
    }

    /***
     * Sets questionID of question
     * @param questionID
     */
    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    /***
     * Converts Question class into HashMap to use in database
     * @return HashMap of Question
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> q = new HashMap<>();
        q.put("qID", this.questionID);
        q.put("uID", this.username);
        q.put("date", this.date);
        q.put("question", this.question);

        return q;
    }
}
