package com.cmput301w21t06.crowdfly;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.cmput301w21t06.crowdfly.Database.GodController;
import com.cmput301w21t06.crowdfly.Views.NewTrialActivity;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.Views.AddExperimentActivity;
import com.cmput301w21t06.crowdfly.Views.AuthActivity;
import com.cmput301w21t06.crowdfly.Views.ViewExperimentLogActivity;
import com.cmput301w21t06.crowdfly.Views.ViewTrialLogActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrialActivityTest {
    private Solo solo;
    public Boolean isEmulated = GodController.useEmulator();

    @Rule
    public ActivityTestRule<AuthActivity> rule =
            new ActivityTestRule<>(AuthActivity.class, true, true);



    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnButton("Experiment Log");
        solo.waitForActivity(ViewExperimentLogActivity.class);
        solo.assertCurrentActivity("Wrong activity", ViewExperimentLogActivity.class);
        solo.clickOnButton("Add experiment");
        solo.assertCurrentActivity("Wrong activity", AddExperimentActivity.class);
        solo.enterText((EditText) solo.getView(R.id.region_edit_text), "test region");
        solo.enterText((EditText) solo.getView(R.id.min_trial_edit_text), "15");
        solo.hideSoftKeyboard();
        solo.clickOnButton("Count");
        solo.assertCurrentActivity("Wrong activity", ViewExperimentLogActivity.class);
        solo.waitForText("test region", 1, 2000);
        solo.clickOnText("test region");
    }

    @After
    public void tearDown() {
        FirebaseAuth.getInstance().signOut();
    }

//    @Test
//    public void start() throws Exception {
//        solo.assertCurrentActivity("Wrong activity", ViewTrialLogActivity.class);
//    }

//    @Test
//    public void testAddTrial() throws InterruptedException {
//
//        solo.assertCurrentActivity("Wrong activity", ViewTrialLogActivity.class);
//        solo.clickOnButton("Subscribe");
//
//        ListView listView = (ListView) solo.getView(R.id.trialListView);
//        ArrayAdapter<Trial> adapter = (ArrayAdapter<Trial>) listView.getAdapter();
//        int oldCount = adapter.getCount();
//
//        solo.clickOnButton("Add Trial");
//
//
//        solo.assertCurrentActivity("Wrong activity", NewTrialActivity.class);
//        solo.enterText((EditText) solo.getView(R.id.regionEnforcedEditText), "True");
//        solo.enterText((EditText) solo.getView(R.id.regionTypeEditText), "CANADA");
//        solo.hideSoftKeyboard();
//        solo.clickOnButton("Count Trial");
//
//        solo.enterText((EditText) solo.getView(R.id.countDescInput), "TestCase");
//        solo.enterText((EditText) solo.getView(R.id.countInput), "4");
//        solo.hideSoftKeyboard();
//        solo.clickOnButton("OK");
//        solo.clickOnButton("Add Trial");
//
//        solo.assertCurrentActivity("Wrong activity", ViewTrialLogActivity.class);
//
//        listView = (ListView) solo.getView(R.id.trialListView);
//        adapter = (ArrayAdapter<Trial>) listView.getAdapter();
//        int newCount = adapter.getCount();
//        assertEquals(true, (oldCount < newCount));
//    }

}
