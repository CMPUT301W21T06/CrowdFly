package com.cmput301w21t06.crowdfly.Controllers;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestore;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.User;

/**
 * Contains methods for managing subscriptions to experiments for users
 */
public class SubscriptionManager {

    /**
     * Subscribes a user by adding uid to sub-collection for experiment
     * @param user
     * @param experiment
     */
    public static void subscribe(User user, Experiment experiment){
        new CrowdFlyFirestore().setSubscribedUser(experiment, user);
    }

    /**
     * Un-subscribes a user by removing uid from sub-collection in experiment
     *
     * @param user
     * @param experiment
     */
    public static void unsubscribe(User user, Experiment experiment){
        new CrowdFlyFirestore().removeSubscribedUser(experiment, user);
    }

    /**
     * Checks if given user is subscribed to experiment
     * @param experiment
     * @param user
     */
    public static void isSubscribed(Experiment experiment, User user, OnDoneGetSubscribedListener OnDoneGetSubscribedListener) {
        new CrowdFlyFirestore().isSubscribed(experiment, user, OnDoneGetSubscribedListener);
    }

    /**
     * Interface for method to be run after finding user subscription status
     */
    public interface OnDoneGetSubscribedListener {
        public void onDoneGetIsSubscribed(Boolean result);
    }
}
