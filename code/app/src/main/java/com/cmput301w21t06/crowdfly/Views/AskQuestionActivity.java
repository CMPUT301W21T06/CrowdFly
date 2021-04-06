package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Database.QuestionController;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.Question;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/***
 * Ask questions about an experiment
 */
public class AskQuestionActivity extends AppCompatActivity {
    final private String userID = FirebaseAuth.getInstance().getUid();
    private ExperimentLog expLog;
    static public String expID;

    EditText questionDesc;
    Button btnPostQuestion;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        expID = getIntent().getStringExtra("expID");
        expLog = ExperimentLog.getExperimentLog();
        int expPos = expLog.getExperimentPositionByID(expID);

        drawerLayout = findViewById(R.id.drawer_ask_question);
        navigationView = findViewById(R.id.nav_view_ask_question);
        toolbar = findViewById(R.id.toolbar_ask_question);
        toolbar.setTitle("CrowdFly");

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });

        btnPostQuestion = findViewById(R.id.postQuestionButton);
        questionDesc = findViewById(R.id.ask_question_desc);

        btnPostQuestion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String question = questionDesc.getText().toString();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                Date d = new Date();
                String date = dateFormat.format(d);
                Question q = new Question(question, userID, date);

                Experiment exp = expLog.getExperiment(expPos);
//                exp.addQuestion(q);
//                expLog.set(expPos, exp);

                exp.getQuestionController().addQuestionData(q, expID);

                Intent intent = new Intent(AskQuestionActivity.this, ViewQuestionLogActivity.class);
                intent.putExtra("expID", String.valueOf(expID));
                startActivity(intent);
            }
        });

    }
}