package com.cmput301w21t06.crowdfly.Database;

/**
 * Class to contain all paths to collections and documents in firebase
 */
public class CrowdFlyFirestorePaths {

    /**
     * Returns path to data associated with user ID
     * @param userID
     * @return
     */
    public static final String userProfile(String userID){
        return String.format("/users/%s", userID);
    }
}
