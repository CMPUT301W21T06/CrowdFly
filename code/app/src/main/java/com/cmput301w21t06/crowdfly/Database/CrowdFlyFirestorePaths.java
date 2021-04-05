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
        return String.format("/Users/%s", userID);
    }

    /**
     * This returns the path to the users collection
     * @return
     * This is the path to the users collection
     */
    public static final String users(){
        return String.format("/Users");
    }

    /**
     * This returns the path to the experiments collection
     * @return
     * This is the path to the experiments collection
     */
    public static final String experiments(){
        return String.format("/Experiments");
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



    public static final String subscription(String experimentID, String userID) {
        return String.format("/Experiments/%s/Subscribers/%s", experimentID, userID);
    }

    public static final String subscriptions(String experimentID) {
        return String.format("/Experiments/%s/Subscribers", experimentID);
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
     * Path for Questions collection
     * @param experimentID
     * @return
     */
    public static final String questions(String experimentID) {
        return String.format("/Experiments/%s/Questions", experimentID);
    }

    /***
     * Path for a single question
     * @param questionID
     * @param experimentID
     * @return
     */
    public static final String question(String questionID, String experimentID) {
        return String.format("/Experiments/%s/Questions/%s", experimentID, questionID);
    }

    /***
     * Path for comment collection
     * @param experimentID 
     * @param qID questionID
     * @return
     */
    public static final String comments(String experimentID, String qID) {
        return String.format("/Experiments/%s/Questions/%s/Comments", experimentID, qID);
    }

    /***
     * Path for single comment
     * @param commentID
     * @param questionID
     * @param experimentID
     * @return
     */
    public static final String comment(String commentID, String questionID, String experimentID) {
        return String.format("/Experiments/%s/Questions/%s/Comments/%s", experimentID, questionID, commentID);
    }

    /**
     * This provides the path to the display ID counter document
     * @return
     * This is the path to the counter document
     */
    public static final String counter(){
        return String.format("/Admin/Admin");
    }

    /**
     * This provides a path to the codes collection for a user.
     * @param userID Code should be registered to an individual user
     * @return
     */
    public static final String codes(String userID) {
        return String.format("/Codes/%s/SavedCodes", userID);
    }


}
