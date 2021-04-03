package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w21t06.crowdfly.Controllers.CodeController;
import com.cmput301w21t06.crowdfly.Controllers.QRCodeController;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestorePaths;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Database.ExperimentController;
import com.cmput301w21t06.crowdfly.Database.GodController;
import com.cmput301w21t06.crowdfly.Models.Barcode;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.QRCode;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;

public class GenerateQRActivity extends AppCompatActivity implements
        CrowdFlyListeners.OnDoneGetExpListener, NewTrialFragment.OnNewTrialListener,
        CodeController.OnDoneRegisteredCodeListener {
    Experiment currentExperiment;
    String userID;
    NewTrialFragment newTrialFragment;
    Button generateQRButton;
    Button saveToCamera;
    ImageView qrCodeImageView;
    Bitmap qrCodeBitmap;

    QRCodeController qrCodeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);
        String experimentID = getIntent().getStringExtra("experimentID");
        userID = FirebaseAuth.getInstance().getUid();
        ExperimentController.getExperimentData(experimentID, this);

    }

    private void setup() {
        generateQRButton = findViewById(R.id.generate);
        saveToCamera = findViewById(R.id.saveToCamera);
        qrCodeImageView = findViewById(R.id.qrCode);
        generateQRButton.setOnClickListener(v -> {
            newTrialFragment.show(getSupportFragmentManager(), "Add Trial Result");
            generateQRButton.setText(R.string.generating);
            generateQRButton.setClickable(false);
        });

        saveToCamera.setOnClickListener(v -> {
            // Convert the bitmap to Uri and pass it into "share" intent
            Intent dataIntent = new Intent(Intent.ACTION_SEND); // Opens send menu
            ByteArrayOutputStream bytesBuffer = new ByteArrayOutputStream();
            qrCodeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytesBuffer);
            String filePath = MediaStore.Images.Media.insertImage(this.getContentResolver(), qrCodeBitmap, "QRCODE", "Scan to add the associated trial data");
            Uri data = Uri.parse(filePath);
            dataIntent.setData(data);

            Intent shareIntent = Intent.createChooser(dataIntent, "Share your QR Code");
            startActivity(shareIntent);

        });

        String codesCollectionPath = CrowdFlyFirestorePaths.codes(userID);
        FirebaseFirestore db = GodController.getDb();
        CollectionReference codesCollectionReference = db.collection(codesCollectionPath);
        qrCodeController = new QRCodeController(codesCollectionReference, userID);
    }

    @Override
    public void onDoneGetExperiment(Experiment experiment) {
        currentExperiment = experiment;
        // Add some logic for view page after loading experiment data
        newTrialFragment = NewTrialFragment.newInstance(currentExperiment.getType(), userID);
        setup();
    }

    @Override
    public void onOkPressed(Trial trial) {
        if (trial != null) {
            qrCodeController.registerCode(trial, this);
        } else {
            generateQRButton.setText(R.string.generate);
            generateQRButton.setClickable(true);
        }
    }

    @Override
    public void onDoneRegisteredCode(Barcode code) {
        QRCode qrCode = (QRCode) code;
        System.out.println(qrCode.getCodeID());
        qrCodeBitmap = qrCodeController.printCode(qrCode);
        qrCodeImageView.setImageBitmap(qrCodeBitmap);
        qrCodeImageView.setVisibility(View.VISIBLE);
        generateQRButton.setText(R.string.generatedSuccess);
        saveToCamera.setVisibility(View.VISIBLE);
    }
}