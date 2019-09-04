package com.westas.orderassembly;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerNotClaimedException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scaner extends AppCompatActivity implements BarcodeReader.BarcodeListener,BarcodeReader.TriggerListener, TBarcodeReader.TCallBack{

    private com.honeywell.aidc.BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaner);

        // get bar code instance from MainActivity
        barcodeReader = MainActivity.getBarcodeObject();

        if (barcodeReader != null) {

            try {
                barcodeReader.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
            // register bar code event listener
            barcodeReader.addBarcodeListener(Scaner.this);

            // register trigger state change listener
            //barcodeReader.addTriggerListener(this);

            Toast.makeText(this, "Add Barcode listener", Toast.LENGTH_SHORT).show();
            // set the trigger mode to client control
            try
            {
                barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                        BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
            }
            catch (UnsupportedPropertyException e)
            {
                Toast.makeText(this, "Failed to apply properties", Toast.LENGTH_SHORT).show();

            }

            Map<String, Object> properties = new HashMap<String, Object>();
            // Set Symbologies On/Off
            properties.put(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
            properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, false);
            properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, false);
            properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, false);
            properties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, false);
            // Set Max Code 39 barcode length
            properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 15);
            // Turn on center decoding
            properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
            // Enable bad read response
            properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, true);
            // Apply the settings
            barcodeReader.setProperties(properties);


        }

    }
    @Override
    public void OnBarcode(String code)
    {

    }


    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // update UI to reflect the data
                List<String> list = new ArrayList<String>();
                list.add("Barcode data: " + event.getBarcodeData());
                list.add("Character Set: " + event.getCharset());
                list.add("Code ID: " + event.getCodeId());
                list.add("AIM ID: " + event.getAimId());
                list.add("Timestamp: " + event.getTimestamp());

                TextView textView = findViewById(R.id.textView);
                textView.setText(event.getBarcodeData());
                Toast.makeText(Scaner.this, "Event Barcode", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // When using Automatic Trigger control do not need to implement the
    // onTriggerEvent function
    @Override
    public void onTriggerEvent(TriggerStateChangeEvent event) {
        try {
            // only handle trigger presses
            // turn on/off aimer, illumination and decoding
            barcodeReader.aim(event.getState());
            barcodeReader.light(event.getState());
            barcodeReader.decode(event.getState());

        } catch (ScannerNotClaimedException e) {
            e.printStackTrace();
            Toast.makeText(this, "Scanner is not claimed", Toast.LENGTH_SHORT).show();
        } catch (ScannerUnavailableException e) {
            e.printStackTrace();
            Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent arg0) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(Scaner.this, "No data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
