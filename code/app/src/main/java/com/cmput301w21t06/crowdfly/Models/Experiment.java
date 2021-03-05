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
    
    // no users in place at the moment, just gonna comment it out for now
    // public Experiment(String desc, String reg, int minT, ArrayList<User> subscribedU, String owner){}
    
    public Experiment(String desc,String reg, int minT){
        this.description = desc;
        this.region = reg;
        this.minTrials = minT;
        this.stillRunning = true;
    }

    // methods
    public void summarizeTrials(ArrayList<Trial> t){}
    public void subscribe(User user){}
    public void changeType(int type){}
    public void addTrial(Trial trial){}
    public void ignore(ArrayList<User> subscribedUsers ){}
    public void togglePublish(){}

    // getters
    public String getDescription() {return description;}
    public String getRegion() {return region;}
    public int getMinTrials() {return minTrials;}
    public Boolean getStatus() {return stillRunning;}
    public int getNumTrials() {return trials.size();}
    public ArrayList<User> getSubscribedUsers() {return subscribedUsers;}
    public ArrayList<Trial> getTrials() {return trials;}
    public ArrayList<Question> getQuestions() {return questions;}
    public User getOwner() {return owner;}
    public QRManager getQRManager() {return qrCode;}
    public Boolean getPublished() {return published;}
}
