/**
 * Adapted the QRGenerator documentation code by androidmads
 * Link to author: https://github.com/androidmads
 * Link to repository: https://github.com/androidmads/QRGenerator
 */

package com.cmput301w21t06.crowdfly.Controllers;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.cmput301w21t06.crowdfly.Models.Barcode;
import com.cmput301w21t06.crowdfly.Models.QRCode;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;
import com.google.firebase.firestore.CollectionReference;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

import static androidx.core.content.res.ResourcesCompat.getColor;

public class QRCodeController extends CodeController {

    public static final String APP_COLOR_HEX = "#2B547E";
    public static final String WHITE_COLOR_HEX = "#FFFFFF";

    public QRCodeController(CollectionReference codesCollection, String userID) {
        super(codesCollection, userID);
    }

    @Override
    public QRCode generateCode(Trial trial) {
        return new QRCode(trial, this.userID);
    }

    @Override
    public Boolean saveCode(Bitmap bitmap, String path) {
        // Save with location, value, bitmap returned and type of Image(JPG/PNG).
        QRGSaver qrgSaver = new QRGSaver();
        Boolean success = qrgSaver.save(path, "QRCODE_CROWDFLY", bitmap, QRGContents.ImageType.IMAGE_JPEG);
        return success;
    }

    @Override
    public Bitmap printCode(Barcode barcode) {
        Bitmap bitmap;
        // Encode the Firestore document id in qr code
        QRGEncoder qrgEncoder = new QRGEncoder(barcode.getCodeID(),
                null, QRGContents.Type.TEXT, 200);
        qrgEncoder.setColorBlack(Color.parseColor(APP_COLOR_HEX));
        qrgEncoder.setColorWhite(Color.parseColor(WHITE_COLOR_HEX));
        // Getting QR-Code as Bitmap
        bitmap = qrgEncoder.getBitmap();
        return bitmap;
    }
}
