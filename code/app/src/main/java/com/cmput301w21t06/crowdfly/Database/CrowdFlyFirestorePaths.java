package com.cmput301w21t06.crowdfly.Database;

/**
 * Class to contain all paths to collections and documents in firebase
 */
public class CrowdFlyFirestorePaths {

    /**
     * Returns path to data associated with user ID
     * @param userID
     * This is the user id that will be used to construct the path
     * @return
     * This returns the completed path
     */
    public static final String userProfile(String userID){
        return String.format("/users/%s", userID);
    }

    /***
     * Path to a particular experiment
     * @param experimentID
     * The experiment that is being accessed
     * @return
     * The path to that experiment in the database
     */
    public static final String experiment(String experimentID) {
        return String.format("/Experiments/%s", experimentID);
    }


    /***
     * Path for subscriptions sub collection for experiment
     * @param experimentID
     * @param userID
     * @return
     */
    public static final String subscriptions(String experimentID, String userID) {
        return String.format("/Experiments/%s/Subscribers/%s", experimentID, userID);
    }

    /***
     * Path for Trials collection
     * @param experimentID
     * @return
     */
    public static final String trials(String experimentID) {
        return String.format("/Experiments/%s/Trials", experimentID);
    }

    /***
     * Path for a single trial
     * @param trialID
     * @param experimentID
     * @return
     */
    public static final String trial(String trialID, String experimentID) {
        return String.format("/Experiments/%s/Trials/%s", experimentID, trialID);
    }

    /***
     * Path for a single statistic
     * @param statisticID
     * @param experimentID
     * @return
     */
    public static final String statistic(int statisticID, String experimentID) {
        return String.format("/Trials/{}/Statistics/{}", experimentID, statisticID);
    }

    /***
     * Path for a question
     * @param questionID
     * @return
     */
    public static final String questions(int questionID) {
        return String.format("/Questions/{}", questionID);
    }




}
