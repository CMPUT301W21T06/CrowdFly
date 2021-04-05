package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Models.Comment;
import com.cmput301w21t06.crowdfly.Models.Experiment;
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
 * This class controls all operations related to Trials
 */
public class CommentController {
    private CollectionReference cCollection;
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    private String qID;
    private String eID;

    public CommentController(String eid, String qID) {
        this.qID = qID;
        this.eID = eid;
        cCollection = GodController.getDb().collection(CrowdFlyFirestorePaths.comments(eid, qID));
        setUp();
    }
    /**
     * This sets up the snapshot listener for trials
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
                }
            }
        });
    }

    public void getCommentData(CrowdFlyListeners.OnDoneGetCommentsListener onDoneGetCommentsListener) {
        setUp();
        onDoneGetCommentsListener.onDoneGetComments(comments);
    }

    /**
     * This returns the number of comments
     * @return
     * This is the number of trials
     */
    public int getNumcomments(){
        return comments.size();
    }

    /**
     * This gets a specific comment
     * @param commentID
     * The trial id to look for
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
    /**
     * This creates a new document in the database for the comment with the necessary data
     * @param comment
     * The trial to store
     * @param experimentID
     * The experiment to reference to get the location of where to store the trial
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
                        }
                    }
                }
        );
    }

    /**
     * This proceeds to store the trial id and timestamp with the new trial
     * Can/should be used to update data of an existing trial
     * @param comment
     * This is the manipulated trial
     * @param experimentID
     * Experiment ID used to grab the trial path
     */
    public void setCommentData(Comment comment, String questionID, String experimentID) {
        GodController.setDocumentData(CrowdFlyFirestorePaths.comment(comment.getCommentID(), questionID, experimentID), comment.toHashMap());
    }
    /**
     * This proceeds to remove an existing trial within an experiment
     * @param commentID
     * This is the trial to be removed
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
            Log.e("TrialController","Trial not found on delete!");
        }

        DocumentReference doc = cCollection.document(String.valueOf(commentID));
        doc.delete();
    }
    /**
     * This deletes any and all trials
     */
    public void removeComments(){
        for (Comment comment : comments){
            Log.e("deletion",comment.getCommentID());
            DocumentReference doc = cCollection.document(String.valueOf(comment.getCommentID()));
            doc.delete();
        }
    }


}
