package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Controllers.QRManager;

import java.util.ArrayList;

public class Experiment {
    // Eventually this class will import and export JSON objects to firebase, so these attributes
    //methods are subject to change
    private String description;
    private String region;
    private int minTrials;
    private Boolean stillRunning;
    private ArrayList<User> subscribedUsers;
    private ArrayList<Trial> trials;
    private ArrayList<Question> questions;
    private User owner;
    private QRManager qrCode;
    private Boolean published;
    
    public void Experiment(String desc,String reg,String minT, ArrayList<User> subscribedU , String owner){}
    public void summarizeTrials(ArrayList<Trial> t){}
    public void subscribe(User user){}
    public void changeType(int type){}
    public void addTrial(Trial trial){}
    public void ignore(ArrayList<User> subscribedUsers ){}
    public void togglePublish(){}
}
