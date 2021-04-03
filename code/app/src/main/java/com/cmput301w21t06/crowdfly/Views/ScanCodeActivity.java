/*
  Referenced the following codelab from google to understand how to use camerax
  https://codelabs.developers.google.com/codelabs/camerax-getting-started#1
  Author: Google Open Source
 */

package com.cmput301w21t06.crowdfly.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.cmput301w21t06.crowdfly.Controllers.BarcodeController;
import com.cmput301w21t06.crowdfly.Controllers.CodeController;
import com.cmput301w21t06.crowdfly.Controllers.QRCodeController;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestorePaths;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Database.ExperimentController;
import com.cmput301w21t06.crowdfly.Database.GodController;
import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class ScanCodeActivity extends AppCompatActivity implements NewTrialFragment.OnNewTrialListener, EditBinomialTrialFragment.OnFragmentInteractionListener, EditCountTrialFragment.OnFragmentInteractionListener {
    public static final int GALLERY_REQUEST_CODE = 100;
    public static final String QR_CODE = "QR Code";
    public static final String BARCODE = "Barcode";
    public static final String BINOMIAL = "binomial";
    public static final String COUNT = "count";
    public static final String CANCEL = "Cancel";
    public static final String PROCEED = "Proceed";
    private final BarcodeScanner scanner = BarcodeScanning.getClient(); // Library to parse barcodes from images
    String userID;
    CollectionReference codesCollectionReference;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private Experiment currentExperiment;
    private com.cmput301w21t06.crowdfly.Models.Barcode recentlyScannedBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        Button uploadButton = findViewById(R.id.uploadButton);
        previewView = findViewById(R.id.viewFinder);
        String codesCollectionPath = CrowdFlyFirestorePaths.codes(userID);
        FirebaseFirestore db = GodController.getDb();
        codesCollectionReference = db.collection(codesCollectionPath);
        userID = FirebaseAuth.getInstance().getUid();
        String experimentID = getIntent().getStringExtra("experimentID");
        ExperimentController.getExperimentData(experimentID, new CrowdFlyListeners.OnDoneGetExpListener() {
            @Override
            public void onDoneGetExperiment(Experiment experiment) {
                currentExperiment = experiment;
            }
        });
        getPermissionToOpenCamera();

        uploadButton.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI); // Opens the media gallery
            startActivityForResult(gallery, GALLERY_REQUEST_CODE);
        });
    }


    void getPermissionToOpenCamera() {
        // Prompts user to get the camera and gallery permissions if not accepted
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 20);

        } else {
            cameraProviderFuture = ProcessCameraProvider.getInstance(this);
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (Exception e) {
                    // Prompt User to upload instead as Camera may not be available
                    Toaster.makeToast(this, "Camera unavailable, please try uploading the barcode");
                }
            }, ContextCompat.getMainExecutor(this));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageData = data.getData(); // Gallery activity returns data as URI that needs to parsed to the correct format
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputImage inputImage =
                        InputImage.fromBitmap(bitmap, 0); // Unsure about the correct rotation value here

                scanner.process(inputImage)
                        .addOnSuccessListener(barcodes -> {
                            if (barcodes.size() == 0) {
                                Toaster.makeToast(ScanCodeActivity.this, "No barcode detected, please upload another image.");
                            } else {
                                Barcode barcode = barcodes.get(0);
                                String type = getCodeType(barcode);
                                AlertDialog.Builder builder = getDetectedAlertDialogBuilder(barcode, type);
                                builder.setNegativeButton(CANCEL, (dialog, which) -> dialog.cancel()).setPositiveButton(PROCEED, (dialog, which) -> {
                                    if (type.equals(QR_CODE)) {
                                        QRCodeController qrCodeController = new QRCodeController(codesCollectionReference, userID);
                                        qrCodeController.getBarcode(barcode.getRawValue(), barcodeScanned -> {
                                            processCode(barcodeScanned, new OnFinishProcessListener() {
                                                @Override
                                                public void onFinishProcess() {

                                                }
                                            });
                                        });
                                    } else {
                                        BarcodeController barcodeController = new BarcodeController(codesCollectionReference, userID);
                                        barcodeController.getBarcode(barcode.getRawValue(), barcodeScanned -> {
                                            processCode(barcodeScanned, new OnFinishProcessListener() {
                                                @Override
                                                public void onFinishProcess() {

                                                }
                                            });
                                        });
                                    }

                                });
                                builder.show();
                            }
                        })
                        .addOnFailureListener(Throwable::printStackTrace);
            }
        }
    }

    private String getCodeType(Barcode barcode) {
        String type = BARCODE;
        if (barcode.getFormat() == Barcode.FORMAT_QR_CODE) {
            type = QR_CODE;
        }
        return type;
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT) // TODO change to use back camera. Only using front camera for testing
                .build();

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(new BarcodeExecutor(), imageProxy -> {
            int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();

            @SuppressLint("UnsafeExperimentalUsageError") // Apparently getting image like this is deprecated. Will explore newer methods later.
                    Image image = imageProxy.getImage();
            if (image != null) {

                InputImage inputImage =
                        InputImage.fromMediaImage(image, rotationDegrees);

                onDoneGetBarcode(scanner.process(inputImage), imageProxy);

            }
        });

        preview.setSurfaceProvider(previewView.createSurfaceProvider());
        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
    }

    public void onDoneGetBarcode(Task<List<Barcode>> barcodesTask, ImageProxy imageProxy) {
        barcodesTask.addOnSuccessListener(barcodes -> {
            if (barcodes.size() > 0) {
                Barcode barcode = barcodes.get(0);

                String type = getCodeType(barcode);

                String userID = FirebaseAuth.getInstance().getUid();
                AlertDialog.Builder builder = getDetectedAlertDialogBuilder(barcode, type);
                builder.setNegativeButton(CANCEL, (dialog, which) -> {
                    imageProxy.close(); // Continue detection
                }).setPositiveButton(PROCEED, (dialog, which) -> {
                    if (type.equals(QR_CODE)) {
                        QRCodeController qrCodeController = new QRCodeController(codesCollectionReference, userID);
                        qrCodeController.getBarcode(barcode.getRawValue(), barcodeScanned -> {
                            processCode(barcodeScanned, new OnFinishProcessListener() {
                                @Override
                                public void onFinishProcess() {
                                    imageProxy.close();
                                }
                            });
                        });
                    } else {
                        BarcodeController barcodeController = new BarcodeController(codesCollectionReference, userID);
                        barcodeController.getBarcode(barcode.getRawValue(), barcodeScanned -> {
                            processCode(barcodeScanned, new OnFinishProcessListener() {
                                @Override
                                public void onFinishProcess() {
                                    imageProxy.close();
                                }
                            });
                        });
                    }

                });

                builder.show();
            } else {
                imageProxy.close(); // Must close imageProxy to process next frame
            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            imageProxy.close();
        });
    }

    private void processCode(com.cmput301w21t06.crowdfly.Models.Barcode barcodeScanned, OnFinishProcessListener listener) {
        Map<String, Object> storedTrial = barcodeScanned.getTrialData();
        if (storedTrial != null) {
            if (currentExperiment.getType().equals(BINOMIAL)) {
                if (storedTrial.containsKey("successes")) {
                    EditBinomialTrialFragment.newInstance(new BinomialTrial(storedTrial)).show(getSupportFragmentManager(), "Add a Binomial Trial");
                } else {
                    incompatibleTrialToast();
                }
            } else if (currentExperiment.getType().equals(COUNT)) {
                if (storedTrial.containsKey(COUNT)) {
                    EditCountTrialFragment.newInstance(new CountTrial(storedTrial)).show(getSupportFragmentManager(), "Add a Count Trial");
                } else {
                    incompatibleTrialToast();
                }
            }

            listener.onFinishProcess();
        } else {
            Toaster.makeToast(ScanCodeActivity.this, "This code does not exist!");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder = builder
                    .setTitle(String.format("This code is not associated with a Trial!"))
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onFinishProcess();
                        }
                    })
                    .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            recentlyScannedBarcode = barcodeScanned;
                            NewTrialFragment.newInstance(currentExperiment.getType(), userID).show(getSupportFragmentManager(), "Register Trial to Code");
                        }
                    });
            builder.show();
        }
    }

    private void incompatibleTrialToast() {
        Toaster.makeToast(ScanCodeActivity.this, "Trial Stored is Incompatible");
    }

    private AlertDialog.Builder getDetectedAlertDialogBuilder(Barcode barcode, String type) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_scanner_code_code_detected_fragment, null);
        TextView value = view.findViewById(R.id.value);
        value.setText(barcode.getRawValue());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder = builder
                .setTitle(String.format("%s Detected!", type))
                .setView(view);
        return builder;
    }

    @Override
    public void onOkPressed(BinomialTrial trial) {
        currentExperiment.getTrialController().addTrialData(trial, currentExperiment.getExperimentId());
    }

    @Override
    public void onOkPressed(CountTrial trial) {
        currentExperiment.getTrialController().addTrialData(trial, currentExperiment.getExperimentId());
    }

    @Override
    public void onOkPressed(Trial trial) {
        if (trial != null) {

            new BarcodeController(codesCollectionReference, userID).registerOldCode(trial, recentlyScannedBarcode.getCodeID(), new CodeController.OnDoneRegisteredCodeListener() {
                @Override
                public void onDoneRegisteredCode(com.cmput301w21t06.crowdfly.Models.Barcode barcode) {
                    Toaster.makeToast(ScanCodeActivity.this, "Successfully registered!");
                }
            });
        } else {
            Toaster.makeToast(ScanCodeActivity.this, "Cancelled");
        }
    }

    public interface OnFinishProcessListener {
        void onFinishProcess();
    }

    /**
     * Executorer to run the barcode analysis async task.
     */
    static class BarcodeExecutor implements Executor {
        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }


}