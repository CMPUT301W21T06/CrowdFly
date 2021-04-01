package com.cmput301w21t06.crowdfly;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.cmput301w21t06.crowdfly.Database.GodController;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Views.AddExperimentActivity;
import com.cmput301w21t06.crowdfly.Views.AuthActivity;
import com.cmput301w21t06.crowdfly.Views.MainActivity;
import com.cmput301w21t06.crowdfly.Views.ViewExperimentLogActivity;
import com.cmput301w21t06.crowdfly.Views.ViewTrialLogActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExperimentActivityTest {
    private Solo solo;
    public Boolean isEmulated = GodController.useEmulator();

    @Rule
    public ActivityTestRule<AuthActivity> rule =
            new ActivityTestRule<>(AuthActivity.class, true, true);



    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @After
    public void tearDown() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void start() throws Exception {
        solo.assertCurrentActivity("Wrong activity", AuthActivity.class);
//        solo.clickOnButton("Generate");
//        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    }

    @Test
    public void testAddExperiment() {
        solo.clickOnButton("Experiment Log");
        solo.waitForActivity(ViewExperimentLogActivity.class);
        solo.assertCurrentActivity("Wrong activity", ViewExperimentLogActivity.class);
        solo.clickOnButton("Add experiment");

        ListView listView = (ListView) solo.getView(R.id.experiment_list);
        ArrayAdapter<Experiment> adapter = (ArrayAdapter<Experiment>) listView.getAdapter();
        int oldCount = adapter.getCount();

        solo.assertCurrentActivity("Wrong activity", AddExperimentActivity.class);
        solo.enterText((EditText) solo.getView(R.id.region_edit_text), "test region");
        solo.enterText((EditText) solo.getView(R.id.min_trial_edit_text), "15");
        solo.hideSoftKeyboard();
        solo.clickOnButton("Count");

        solo.assertCurrentActivity("Wrong activity", ViewExperimentLogActivity.class);
        solo.waitForText("test region", 1, 2000);

        listView = (ListView) solo.getView(R.id.experiment_list);
        adapter = (ArrayAdapter<Experiment>) listView.getAdapter();
        int newCount = adapter.getCount();
        assertEquals(true, (oldCount < newCount));
    }
}
