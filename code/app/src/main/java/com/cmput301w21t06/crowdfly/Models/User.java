// Adapted the following example to Hashmap
// Suji (Jul 23 '13) to Chris Schiffhauer,
// User Profile: https://stackoverflow.com/users/1891581/suji
// Licence: CC BY-SA 4.0
// Title: "Android- create JSON Array and JSON Object",
// Website: Stack Overflow
// https://stackoverflow.com/questions/17810044/android-create-json-array-and-json-object

package com.cmput301w21t06.crowdfly.Models;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;


/**
 * Model class that captures user information for logged in users retrieved from Firestore
 */

public class User {
    private String userID;
    private String contactInfo;
    private String displayID;


    /**
     * this is the general constructor of user
     * @param phoneNumber
     * @param email
     * @param birthday
     * @param userID
     * @param displayID
     */
    public User(String phoneNumber, String email, String birthday, @NonNull String userID, String displayID) {
        this.userID = userID;
        this.contactInfo = contactInfo;
        this.displayID = displayID;
    }

    /***
     * this is the hash map constructor for trial
     * @param userData
     */
    public User(Map<String, Object> userData) {
        this.userID = getOrDefault(userData.get("userID"));
        this.contactInfo = getOrDefault(userData.get("contactInfo"));
        this.displayID = getOrDefault(userData.get("displayId"));
    }

    /**
     * this is another general user constructor
     * @param s2
     * @param s1
     * @param s
     * @param userID
     */
    public User(String s2, String s1, String s, String userID) {
        this.userID = userID;
    }

    /**
     * this prevents null values going in
     * @param val
     */
    public String getOrDefault(Object val) {
        if (val == null) {
            return null;
        }
        return val.toString();
    }


    public void setDisplayID(String displayID) { this.displayID = displayID; }

    public String getDisplayID() { return displayID; }

    public String getUserID() { return userID; }
    public void setUserID(String uid) { userID = uid; }

    public String getContactInfo() { return contactInfo; }

    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }


    /**
     * this transforms the User to a HashMap that is fed into the database
     * @return Map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> user = new HashMap<>();
        user.put("userID", this.userID);
        user.put("contactInfo", this.contactInfo);
        user.put("displayID", this.displayID);
        return user;

    }

}