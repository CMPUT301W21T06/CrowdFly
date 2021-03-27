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
 * This class represents a model for a user
 */

public class User {
    private String userID;
    private String contactInfo;
    private String displayID;



    /**
     * This is the constructor used to create a user from firestore
     * @param userData
     */
    public User(Map<String, Object> userData) {
        this.userID = getOrDefault(userData.get("userID"));
        this.contactInfo = getOrDefault(userData.get("contactInfo"));
        this.displayID = getOrDefault(userData.get("displayId"));
    }

    /**
     * This is the constructor used to create a user in the app
     * @param userID
     * The user id to set
     */
    public User(String userID) {
        this.userID = userID;
    }


    private String getOrDefault(Object val) {
        if (val == null) {
            return null;
        }
        return val.toString();
    }

    /**
     * This allows a display ID to be set - testing purposes
     * @param displayID
     * The display ID to set
     */
    public void setDisplayID(String displayID) { this.displayID = displayID; }

    /**
     * This returns the display ID
     * @return
     * The display ID that is returned
     */
    public String getDisplayID() { return displayID; }

    /**
     * This returns the actual firestore ID for the user
     * @return
     * This is the actual firestore ID for the user
     */
    public String getUserID() { return userID; }

    /**
     * This allows the firestore user ID to be set - testing purposes
     * @param uid
     */
    public void setUserID(String uid) { userID = uid; }

    /**
     * This returns the user's contact information
     * @return
     * This is the user's contact information
     */
    public String getContactInfo() { return contactInfo; }

    /**
     * This allows the contact information to be changed for the user
     * @param contactInfo
     * The contact information to use for the update
     */
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }


    /**
     * This transforms the User to a HashMap that is fed into the database
     * @return Map
     * The hashmap to be fed into the database
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> user = new HashMap<>();
        user.put("userID", this.userID);
        user.put("contactInfo", this.contactInfo);
        user.put("displayID", this.displayID);
        return user;

    }

}