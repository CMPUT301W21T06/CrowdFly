package com.cmput301w21t06.crowdfly.Models;

import java.util.ArrayList;

public class Comment {
    private String comment;
    private String username;
    private  ArrayList<Comment> replies;
    
    private void Comment(String comment,String username){}
    private String getComment(){return "";}
    private String getUsername(){return "";}
    private void addChildComment(String comment, String username){}
}
