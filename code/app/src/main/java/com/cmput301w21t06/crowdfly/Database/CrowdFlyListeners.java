package com.cmput301w21t06.crowdfly.Database;

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.User;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * This class holds interfaces that have methods to implement and call if you use a certain database functionality
 */
public class CrowdFlyListeners {
    /**
     * Interface to implement if you access all users
     */
    public interface OnDoneGetIdsListener {
        public void onDoneGetIds(ArrayList<String> ids);
    }

    /**
     * Interface to implement if you access a particular user
     */
    public interface OnDoneGetUserListener {
        public void onDoneGetUser(User user);
    }

    /**
     * Interface to implement after setting up an experiment log
     */
    public interface OnDoneGetExpLogListener {
        public void onDoneGetExperiments();
    }

    /**
     * Interface to implement after getting a particular experiment
     */
    public interface OnDoneGetExpListener {
        public void onDoneGetExperiment(Experiment experiment);
    }

    public interface OnDoneGetTrialsListener {
        public void onDoneGetTrials(TrialLog trialList);
    }
}


