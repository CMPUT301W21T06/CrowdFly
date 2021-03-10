package com.cmput301w21t06.crowdfly.Models;

public class Trial {
    private String description;
    private String successes;
    private String failures;

    private Boolean locRequired;
    private String location;
    private String result;
    private Statistics statistics;
    private User recordedBy;

    //constructors
    public Trial(String description, String successes, String failures) {
        this.description = description;
        this.successes = successes;
        this.failures = failures;
    }

    public Trial(String successes, String failures) {
        this.successes = successes;
        this.failures = failures;
    }

    //setup getters
    public String getDescription() {
        return description;
    }

    public String getSuccesses() {
        return successes;
    }

    public String getFailures() {
        return failures;
    }

    public void specifyLocReq(Boolean locReq){}
    public void specifyLoc(String location){}
    public String getLoc(){return "";}
    private void warnUsers(){}
}
