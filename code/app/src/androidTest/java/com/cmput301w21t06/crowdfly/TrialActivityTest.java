//package com.cmput301w21t06.crowdfly;
//
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ListView;
//
//import androidx.test.platform.app.InstrumentationRegistry;
//import androidx.test.rule.ActivityTestRule;
//
//import com.cmput301w21t06.crowdfly.Models.Experiment;
//import com.cmput301w21t06.crowdfly.Models.NewTrial;
//import com.cmput301w21t06.crowdfly.Models.Trial;
//import com.cmput301w21t06.crowdfly.Models.User;
//import com.cmput301w21t06.crowdfly.Views.AddExperimentActivity;
//import com.cmput301w21t06.crowdfly.Views.EditCountTrialFragment;
//import com.cmput301w21t06.crowdfly.Views.ViewExperimentLogActivity;
//import com.cmput301w21t06.crowdfly.Views.ViewTrialLogActivity;
//import com.robotium.solo.Solo;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//
//public class TrialActivityTest {
//    private Solo solo;
//    private User user;
//
//    @Rule
//    public ActivityTestRule<ViewTrialLogActivity> rule =
//            new ActivityTestRule<>(ViewTrialLogActivity.class, true, true);
//
//    @Before
//    public void setup() throws Exception {
//        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
//        user.setUserID("test_user_id");
//
//    }
//
//    @After
//    public void tearDown() {
//        solo.finishOpenedActivities();
//    }
//
//    @Test
//    public void start() throws Exception {
//        solo.assertCurrentActivity("Wrong activity", ViewTrialLogActivity.class);
//    }
//
//    @Test
//    public void testAddTrial() {
//        solo.assertCurrentActivity("Wrong activity", ViewTrialLogActivity.class);
//        solo.clickOnButton("Subscribe");
//        solo.clickOnButton("Add Trial");
//
//        ListView listView = (ListView) solo.getView(R.id.trialListView);
//        ArrayAdapter<Trial> adapter = (ArrayAdapter<Trial>) listView.getAdapter();
//        int oldCount = adapter.getCount();
//
//        solo.assertCurrentActivity("Wrong activity", NewTrial.class);
//        solo.enterText((EditText) solo.getView(R.id.regionEnforcedEditText), "True");
//        solo.enterText((EditText) solo.getView(R.id.regionTypeEditText), "CANADA");
//        solo.clickOnButton("Count Trial");
//
//        solo.assertCurrentActivity("Wrong Acitivity", EditCountTrialFragment.class);
//        solo.enterText((EditText) solo.getView(R.id.countDescInput), "TestCase");
//        solo.enterText((EditText) solo.getView(R.id.countInput), "4");
//        solo.clickOnButton("Add Trial");
//
//        solo.assertCurrentActivity("Wrong activity", ViewTrialLogActivity.class);
//        solo.waitForText("test region", 1, 2000);
//
//        listView = (ListView) solo.getView(R.id.trialListView);
//        adapter = (ArrayAdapter<Trial>) listView.getAdapter();
//        int newCount = adapter.getCount();
//        assertEquals(true, (oldCount < newCount));
//    }
//}
