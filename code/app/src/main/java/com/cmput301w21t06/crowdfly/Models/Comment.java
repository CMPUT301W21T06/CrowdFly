package com.cmput301w21t06.crowdfly.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will be used to create a forum with comments
 * All issues related to this have yet to be implemented
 */
public class Comment {
    private String comment;
    private String username;
    private String commentID;

    /***
     * Constructor
     * @param comment
     * @param username
     */
    public Comment(String comment,String username){
        this.comment = comment;
        this.username = username;
    }

    /***
     * Constructor for getting values from database
     * @param data Map retrieved from database
     */
    public Comment(Map<String, Object> data) {
        this.comment = (String) data.get("comment");
        this.username = (String) data.get("uID");
    }

    /***
     * Gets comment description
     * @return comment description
     */
    public String getComment(){return this.comment;}

    /***
     * Gets userID of comment publisher
     * @return uesrID
     */
    public String getUsername(){return this.username;}

    /***
     * Gets commentID
     * @return commentID
     */
    public String getCommentID(){return this.commentID;}

    /***
     * Sets commentID
     * @param commentID
     */
    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }


    /***
     * Converts Comment into a HaspMap for use in database
     * @return HashMap
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> comment = new HashMap<>();
        comment.put("comment", this.comment);
        comment.put("uID", this.username);

        return comment;
    }
}
