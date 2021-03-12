package com.cmput301w21t06.crowdfly.Models;

public class Trial{
    private String description;
    private Boolean locRequired;
    private String location;
    private String result;
    private Statistics statistics;
    private User recordedBy;

    public Trial(String description) {
        this.description = description;
    }


    //setup getters
    public String getDescription() {
        return description;
    }
    public void specifyLocReq(Boolean locReq){}
    public void specifyLoc(String location){}
    public String getLoc(){return "";}
    private void warnUsers(){}



//    public String getSuccesses() {
//        return successes;
//    }
//
//    public String getFailures() {
//        return failures;
//    }
}

