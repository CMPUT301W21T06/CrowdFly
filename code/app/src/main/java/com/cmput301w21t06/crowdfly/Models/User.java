package com.cmput301w21t06.crowdfly.Models;

import java.util.ArrayList;


/**
 * Model class that captures user information for logged in users retrieved from UserRepository(TODO)
 */

public class User {
    private String userID;
    private ArrayList<Experiment> subscribedExperiments;
    private String phoneNumber;
    private String email;
    private String birthday;
    private void UserProfile(String userID, String phoneNumber, String email, String birthday){
        this.userID = userID;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthday = birthday;
    }

    public String getUserID(){
        return userID;
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<Experiment> getSubscribedExperiments() {
        return subscribedExperiments;
    }

    public void setSubscribedExperiments(ArrayList<Experiment> subscribedExperiments) {
        this.subscribedExperiments = subscribedExperiments;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

}