package com.apps.fraisekiwi.crypto_carnet.Pages;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import com.apps.fraisekiwi.crypto_carnet.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Objects;

/**
 * Created by Alexandre Chartrand on 4/14/18.
 */

public class PageGenerateQrCodeFromString extends Activity{

    protected ImageView qrCodeGenerator;
    protected String stringDuQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_qr_code_generator);
        Intent activityCalled = getIntent();

        // On se get les informations de la page des cryptocurrencies selon la crypto
        // choisie
        stringDuQrCode = Objects.requireNonNull(activityCalled.getExtras()).getString("stringToTransform");
        qrCodeGenerator = findViewById(R.id.imageView_qr_code_generator);


        // On genere et change l'image du QrCode ici, à l'aide de la librairie zxing
        // Source de la librairie : https://github.com/zxing/zxing
        // Adapté par Alexandre Chartrand.

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix theQrCode = multiFormatWriter.encode(stringDuQrCode, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(theQrCode);
            qrCodeGenerator.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }


    }
}
