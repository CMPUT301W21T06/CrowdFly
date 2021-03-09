// Adapted the following example to Hashmap
// Suji (Jul 23 '13) to Chris Schiffhauer,
// User Profile: https://stackoverflow.com/users/1891581/suji
// Licence: CC BY-SA 4.0
// Title: "Android- create JSON Array and JSON Object",
// Website: Stack Overflow
// https://stackoverflow.com/questions/17810044/android-create-json-array-and-json-object

package com.cmput301w21t06.crowdfly.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Model class that captures user information for logged in users retrieved from Firestore
 */

public class User {
    private String userID;
    private ArrayList<Experiment> subscribedExperiments;
    private String phoneNumber;
    private String email;
    private String birthday;
    public void UserProfile(String userID, String phoneNumber, String email, String birthday){
        this.userID = userID;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthday = birthday;
    }

    public void UserProfile(Map<String, Object> userData) {
        this.userID = userData.get("userID").toString();
        this.phoneNumber = userData.get("phoneNumber").toString();
        this.email = userData.get("email").toString();
        this.birthday = userData.get("birthday").toString();
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

    public Map<String, Object> toHashMap() {
        Map<String, Object> user = new HashMap<>();
        user.put("userID", this.userID);
        user.put("phoneNumber", this.phoneNumber);
        user.put("email", this.email);
        user.put("birthday", this.birthday);

        ArrayList subscribedExperimentsJson = new ArrayList();

        for(int i = 0; i < this.subscribedExperiments.size(); i++) {
            subscribedExperimentsJson.add(this.subscribedExperiments.get(i).getExperimentId());
        }

        user.put("subscribedExperiments", subscribedExperimentsJson);


        return user;

    }

}