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
    
    public Comment(String comment,String username){
        this.comment = comment;
        this.username = username;
    }

    public Comment(Map<String, Object> data) {
        this.comment = (String) data.get("comment");
        this.username = (String) data.get("uID");
    }

    public String getComment(){return this.comment;}
    public String getUsername(){return this.username;}
    public String getCommentID(){return this.commentID;}

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public Map<String, Object> toHashMap() {
        Map<String, Object> comment = new HashMap<>();
        comment.put("comment", this.comment);
        comment.put("uID", this.username);

        return comment;
    }
}
