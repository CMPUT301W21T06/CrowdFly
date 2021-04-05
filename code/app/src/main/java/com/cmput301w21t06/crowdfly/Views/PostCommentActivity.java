package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Models.Comment;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.Question;
import com.cmput301w21t06.crowdfly.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostCommentActivity extends AppCompatActivity {
    final private String userID = FirebaseAuth.getInstance().getUid();
    private ExperimentLog expLog;
    static public String expID;
    static public String qID;

    EditText commentDesc;
    Button btnPostComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        expID = getIntent().getStringExtra("expID");
        qID = getIntent().getStringExtra("qID");
        expLog = ExperimentLog.getExperimentLog();
        int expPos = expLog.getExperimentPositionByID(expID);

        btnPostComment = findViewById(R.id.postCommentButton);
        commentDesc = findViewById(R.id.comment_desc);

        btnPostComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String comment = commentDesc.getText().toString();
//                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//                Date d = new Date();
//                String date = dateFormat.format(d);
                Comment c = new Comment(comment, userID);

                Experiment exp = expLog.getExperiment(expPos);
                Question q = exp.getQuestionByID(qID);
                q.addComment(c);
                exp.setQuestion(q, exp.getQuestionPosByID(qID));
                expLog.set(expPos, exp);

                q.getCommentController().addCommentData(c, qID, expID);

                Intent intent = new Intent(PostCommentActivity.this, QuestionThreadActivity.class);
                intent.putExtra("expID", String.valueOf(expID));
                intent.putExtra("qID", String.valueOf(qID));
                startActivity(intent);
//                finish();
            }
        });

    }
}
