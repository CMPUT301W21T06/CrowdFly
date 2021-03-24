package com.cmput301w21t06.crowdfly.Database;

import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.User;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CrowdFlyListeners {
    public interface OnDoneGetIdsListener {
        public void onDoneGetIds(ArrayList<String> ids);
    }

    public interface OnDoneGetUserListener {
        public void onDoneGetUser(User user);
    }

    public interface OnDoneGetExpLogListener {
        public void onDoneGetExperiments();
    }

    public interface OnDoneGetExpListener {
        public void onDoneGetExperiment(Experiment experiment);
    }
}


