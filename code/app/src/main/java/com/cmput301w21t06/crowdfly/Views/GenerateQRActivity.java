package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.cmput301w21t06.crowdfly.Models.TrialFactory;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class GenerateQRActivity extends AppCompatActivity
        implements CrowdFlyListeners.OnDoneGetExpListener, CodeController.OnDoneRegisteredCodeListener {
    private Experiment currentExperiment;
    private String userID;
    private Button generateQRButton;
    private Button saveToCamera;
    private ImageView qrCodeImageView;
    private Bitmap qrCodeBitmap;
    private QRCodeController qrCodeController;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);
        drawerLayout = findViewById(R.id.drawer_gen_qr);
        navigationView = findViewById(R.id.nav_view_gen_qr);
        toolbar = findViewById(R.id.toolbar_gen_qr);
        toolbar.setTitle("CrowdFly");

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });
        String experimentID = getIntent().getStringExtra("experimentID");
        userID = FirebaseAuth.getInstance().getUid();
        ExperimentController.getExperimentData(experimentID, this);

    }

    private void setup() {
        generateQRButton = findViewById(R.id.generate);
        saveToCamera = findViewById(R.id.saveToCamera);
        qrCodeImageView = findViewById(R.id.qrCode);
        generateQRButton.setClickable(true);
        generateQRButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewTrialActivity.class);
            intent.putExtra("trialType", currentExperiment.getType());
            intent.putExtra("expID", currentExperiment.getExperimentId());
            startActivityForResult(intent, 0);
            generateQRButton.setText(R.string.generating);
            generateQRButton.setClickable(false);
        });

        saveToCamera.setOnClickListener(v -> {
            // Convert the bitmap to Uri and pass it into "share" intent
            Intent dataIntent = new Intent(Intent.ACTION_SEND); // Opens send menu
            ByteArrayOutputStream bytesBuffer = new ByteArrayOutputStream();
            qrCodeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytesBuffer);
            String filePath = MediaStore.Images.Media.insertImage(this.getContentResolver(), qrCodeBitmap, "QRCODE",
                    "Scan to add the associated trial data");
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
        setup();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                HashMap<String, Object> result = (HashMap<String, Object>) data.getSerializableExtra("trialData");
                try {
                    Trial trialAdd = new TrialFactory().getTrialInferType(result);
                    qrCodeController.registerCode(trialAdd, this);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toaster.makeToast(this, "Something went wrong. Please try again.");
                    generateQRButton.setText(R.string.generate);
                    generateQRButton.setClickable(true);
                }
            }
            if (resultCode == RESULT_CANCELED) {
                // Reset
                generateQRButton.setText(R.string.generate);
                generateQRButton.setClickable(true);
            }
        }
    }

    @Override
    public void onDoneRegisteredCode(Barcode code) {
        QRCode qrCode = (QRCode) code;
        Log.d("QR Code", qrCode.getCodeID());
        qrCodeBitmap = qrCodeController.printCode(qrCode);
        qrCodeImageView.setImageBitmap(qrCodeBitmap);
        qrCodeImageView.setVisibility(View.VISIBLE);
        generateQRButton.setText(R.string.generatedSuccess);
        saveToCamera.setVisibility(View.VISIBLE);
    }
}