package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Models.Comment;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.Question;
import com.cmput301w21t06.crowdfly.Models.User;
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
 * This class controls all operations related to comments
 */
public class CommentController {
    private CollectionReference cCollection;
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    private ExperimentLog expLog;
    private String qID;
    private String eID;

    public CommentController(String eid, String qID) {
        this.qID = qID;
        this.eID = eid;
        cCollection = GodController.getDb().collection(CrowdFlyFirestorePaths.comments(eid, qID));
        setUp();
    }
    /**
     * This sets up the snapshot listener for comments
     */
    private void setUp(){
        cCollection.orderBy("lastUpdatedAt", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot response, @Nullable FirebaseFirestoreException error) {
                comments.clear();
                if(response != null){
                    for (QueryDocumentSnapshot doc : response){
                        Comment comment = null;
                        comment = new Comment(doc.getData());

                        if (comment != null) {
                            comments.add(comment);
                        }
                        else{
                            Log.e("commentController", "Getting comments was a failure");
                        }
                    }

                    // add the comments to question
                    expLog = ExperimentLog.getExperimentLog();
                    int expPos = expLog.getExperimentPositionByID(eID);
                    if (expPos != -1) {
                        Experiment exp = expLog.getExperiment(expPos);
                        int qPos = exp.getQuestionPosByID(qID);
                        Question q = exp.getQuestionByID(qID);
                        q.addComments(comments);
                        exp.setQuestion(q, qPos);
                        expLog.set(expPos, exp);
                    }
                }
            }
        });
    }

    /***
     * Gets the collection of comments from the database
     * @param onDoneGetCommentsListener handler for receiving comment data
     */
    public void getCommentData(CrowdFlyListeners.OnDoneGetCommentsListener onDoneGetCommentsListener) {
        setUp();
        onDoneGetCommentsListener.onDoneGetComments(comments);
    }

    /**
     * This gets a specific comment
     * @param commentID
     * The comment id to look for
     * @param onDoneGetCommentListener
     * The class that implements a handler for the result of this method
     */
    public void getComment(String commentID, CrowdFlyListeners.OnDoneGetCommentListener onDoneGetCommentListener){
        Comment loopComment = null;
        boolean loop = true;
        int i = 0;
        while (loop && i < comments.size()){
            loopComment = comments.get(i);
            if (loopComment.getCommentID().matches(commentID)){
                loop = false;
            }
            i++;
        }
        if (loopComment != null) {
            onDoneGetCommentListener.onDoneGetComment(loopComment);
        }
        else{
            Log.e("commentController","comment not found on find!");
        }

    }

    /***
     * Adds a new comment data to the database
     * @param comment comment to add to database
     * @param questionID questionID used for comment path in database
     * @param experimentID experimentID used for comment path in database
     */
    public void addCommentData(Comment comment, String questionID, String experimentID) {
        comments.add(comment);
        cCollection.add(comment.toHashMap()).addOnCompleteListener(
                new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            String newId = task.getResult().getId();
                            comment.setCommentID(newId);
                            setCommentData(comment, questionID, experimentID);

                            expLog = ExperimentLog.getExperimentLog();
                            int expPos = expLog.getExperimentPositionByID(eID);
                            if (expPos != -1) {
                                Experiment exp = expLog.getExperiment(expPos);
                                int qPos = exp.getQuestionPosByID(qID);
                                Question q = exp.getQuestionByID(qID);
                                q.addComments(comments);
                                exp.setQuestion(q, qPos);
                                expLog.set(expPos, exp);
                            }
                        }
                    }
                }
        );
    }

    /***
     * Sets the data of comment in the database
     * @param comment comment to add to database
     * @param questionID questionID used for comment path in database
     * @param experimentID experimentID used for comment path in database
     */
    public void setCommentData(Comment comment, String questionID, String experimentID) {
        GodController.setDocumentData(CrowdFlyFirestorePaths.comment(comment.getCommentID(), questionID, experimentID), comment.toHashMap());
    }

    /**
     * This proceeds to remove an existing comment within question
     * @param commentID
     * This is the comment to be removed
     */
    public void removeCommentData(String commentID){
        Comment loopComment = null;
        boolean loop = true;
        int i = 0;
        while (loop && i < comments.size()){
            loopComment = comments.get(i);
            if (loopComment.getCommentID().matches(commentID)){
                loop = false;
            }

            i++;
        }
        if (loopComment != null) {
            comments.remove(loopComment);
        }
        else{
            Log.e("commentController","comment not found on delete!");
        }

        DocumentReference doc = cCollection.document(String.valueOf(commentID));
        doc.delete();
    }
    /**
     * This deletes any and all comments
     */
    public void removeComments(){
        for (Comment comment : comments){
            Log.e("deletion",comment.getCommentID());
            DocumentReference doc = cCollection.document(String.valueOf(comment.getCommentID()));
            doc.delete();
        }
    }


}
