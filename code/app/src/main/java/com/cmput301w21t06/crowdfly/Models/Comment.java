package com.cmput301w21t06.crowdfly.Models;

import java.util.ArrayList;

/**
 * This class will be used to create a forum with comments
 * All issues related to this have yet to be implemented
 */
public class Comment {
    private String comment;
    private String username;
    private  ArrayList<Comment> replies;
    
    private void Comment(String comment,String username){}
    private String getComment(){return "";}
    private String getUsername(){return "";}
    private void addChildComment(String comment, String username){}
}
