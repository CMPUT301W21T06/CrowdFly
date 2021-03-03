package com.cmput301w21t06.crowdfly.Models;

public class Trial {
    private Boolean locRequired;
    private String location;
    private String result;
    private Statistics statistics;
    private User recordedBy;
    
    public void specifyLocReq(Boolean locReq){}
    public void specifyLoc(String location){}
    public String getLoc(){return "";}
    private void warnUsers(){}
}
