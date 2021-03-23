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

    /**
     * Returns path to User ID counter
     * @return
     */
    public static final String displayId(){
        return "/users/Admin";
    }

    /***
     * Path for an experiment
     * @param experimentID
     * @return
     */
    public static final String experiment(String experimentID) {
        return String.format("/Experiments/%s", experimentID);
    }

    /***
     * Path for the experiments collection
     * @return
     */
    public static final String experimentsCollection() {
        return "/Experiments";
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

    // comments will be a subcollection of questions, but will figure this out later on.
//    public static final String comments(int questionID, int commentID) {
//        return String.format("Questions/{}/{}", questionID, commentID);
//    }


}
