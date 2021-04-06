package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * This class controls all operations related to questions
 */
public class QuestionController {
    private CollectionReference qCollection;
    private ArrayList<Question> questions = new ArrayList<Question>();
    private ExperimentLog expLog;
    private String eid;

    public QuestionController(String eid) {
        this.eid = eid;
        qCollection = GodController.getDb().collection(CrowdFlyFirestorePaths.questions(eid));
        setUp();
    }
    /**
     * This sets up the snapshot listener for questions
     */
    private void setUp(){
        qCollection.orderBy("lastUpdatedAt", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot response, @Nullable FirebaseFirestoreException error) {
                questions.clear();
                if(response != null){
                    for (QueryDocumentSnapshot doc : response){
                        Question question = null;
                        question = new Question(doc.getData());
                        question.setupQuestion(eid, question.getQuestionID());

                        if (question != null) {
                            questions.add(question);
                        }
                        else{
                            Log.e("QuestionController", "Getting questions was a failure");
                        }
                    }
                    expLog = ExperimentLog.getExperimentLog();
                    int expPos = expLog.getExperimentPositionByID(eid);
                    if (expPos != -1) {
                        Experiment exp = expLog.getExperiment(expPos);
                        exp.setQuestions(questions);
                        expLog.set(expPos, exp);
                    }
                }
            }
        });
    }

    public void getQuestionData(CrowdFlyListeners.OnDoneGetQuestionsListener onDoneGetQuestionsListener) {
        setUp();
        onDoneGetQuestionsListener.onDoneGetQuestions(questions);
    }

    /**
     * This gets a specific question
     * @param questionID
     * The question id to look for
     * @param onDoneGetQuestionListener
     * The class that implements a handler for the result of this method
     */
    public void getQuestion(String questionID, CrowdFlyListeners.OnDoneGetQuestionListener onDoneGetQuestionListener){
        Question loopQuestion = null;
        boolean loop = true;
        int i = 0;
        while (loop && i < questions.size()){
            loopQuestion = questions.get(i);
            if (loopQuestion.getQuestionID().matches(questionID)){
                loop = false;
            }
            i++;
        }
        if (loopQuestion != null) {
            onDoneGetQuestionListener.onDoneGetQuestion(loopQuestion);
        }
        else{
            Log.e("QuestionController","Question not found on find!");
        }

    }
    /**
     * This creates a new document in the database for the question with the necessary data
     * @param question
     * The question to store
     * @param experimentID
     * The experiment to reference to get the location of where to store the question
     */
    public void addQuestionData(Question question, String experimentID) {
        questions.add(question);
        qCollection.add(question.toHashMap()).addOnCompleteListener(
                new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            String newId = task.getResult().getId();
                            question.setupQuestion(eid, newId);
                            setQuestionData(question, experimentID);

                            // add questions to experiment
                            expLog = ExperimentLog.getExperimentLog();
                            int expPos = expLog.getExperimentPositionByID(eid);
                            if (expPos != -1) {
                                Experiment exp = expLog.getExperiment(expPos);
                                exp.setQuestions(questions);
                                expLog.set(expPos, exp);
                            }
                        }
                    }
                }
        );
    }

    /**
     * This proceeds to store the question id and timestamp with the new question
     * Can/should be used to update data of an existing question
     * @param question
     * This is the manipulated question
     * @param experimentID
     * Experiment ID used to grab the question path
     */
    public void setQuestionData(Question question, String experimentID) {
        GodController.setDocumentData(CrowdFlyFirestorePaths.question(question.getQuestionID(), experimentID), question.toHashMap());
    }
    /**
     * This proceeds to remove an existing question within an experiment
     * @param questionID
     * This is the question to be removed
     */
    public void removeQuestionData(String questionID){
        Question loopQuestion = null;
        boolean loop = true;
        int i = 0;
        while (loop && i < questions.size()){
            loopQuestion = questions.get(i);
            if (loopQuestion.getQuestionID().matches(questionID)){
                loop = false;
            }

            i++;
        }
        if (loopQuestion != null) {
            questions.remove(loopQuestion);
        }
        else{
            Log.e("questionController","question not found on delete!");
        }

        DocumentReference doc = qCollection.document(String.valueOf(questionID));
        doc.delete();
    }
    /**
     * This deletes any and all questions
     */
    public void removeQuestions(){
        for (Question question : questions){
            Log.e("deletion",question.getQuestionID());
            DocumentReference doc = qCollection.document(String.valueOf(question.getQuestionID()));
            doc.delete();
        }
    }


}
