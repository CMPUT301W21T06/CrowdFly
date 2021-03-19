package com.cmput301w21t06.crowdfly;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentContent;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Views.AddExperimentActivity;
import com.cmput301w21t06.crowdfly.Views.ViewExperimentLogActivity;
import com.cmput301w21t06.crowdfly.Views.ViewTrialLogActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class  ExperimentActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<ViewExperimentLogActivity> rule =
            new ActivityTestRule<>(ViewExperimentLogActivity.class, true, true);

    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    @Test
    public void start() throws Exception {
        solo.assertCurrentActivity("Wrong activity", ViewExperimentLogActivity.class);
    }

    @Test
    public void testAddExperiment() {
        solo.assertCurrentActivity("Wrong activity", ViewExperimentLogActivity.class);
        solo.clickOnButton("Add experiment");

        ListView listView = (ListView) solo.getView(R.id.experiment_list);
        ArrayAdapter<Experiment> adapter = (ArrayAdapter<Experiment>) listView.getAdapter();
        int oldCount = adapter.getCount();

        solo.assertCurrentActivity("Wrong activity", AddExperimentActivity.class);
        solo.enterText((EditText) solo.getView(R.id.region_edit_text), "test region");
        solo.enterText((EditText) solo.getView(R.id.min_trial_edit_text), "15");
        solo.clickOnButton("Count");

        solo.assertCurrentActivity("Wrong activity", ViewExperimentLogActivity.class);
        solo.waitForText("test region", 1, 2000);

        listView = (ListView) solo.getView(R.id.experiment_list);
        adapter = (ArrayAdapter<Experiment>) listView.getAdapter();
        int newCount = adapter.getCount();
        assertEquals(true, (oldCount < newCount));
    }

    @Test
    public void testDeleteExperiment() {
        solo.assertCurrentActivity("Wrong activity", ViewExperimentLogActivity.class);
        ListView listView = (ListView) solo.getView(R.id.experiment_list);
        ArrayAdapter<Experiment> adapter = (ArrayAdapter<Experiment>) listView.getAdapter();
        int oldCount = adapter.getCount();

        solo.clickLongInList(0);

        listView = (ListView) solo.getView(R.id.experiment_list);
        adapter = (ArrayAdapter<Experiment>) listView.getAdapter();
        int newCount = adapter.getCount();

        assertEquals(true, (oldCount > newCount));
    }

    @Test
    public void testViewExperiment() {
        solo.assertCurrentActivity("Wrong activity", ViewExperimentLogActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", ViewTrialLogActivity.class);
    }
}
