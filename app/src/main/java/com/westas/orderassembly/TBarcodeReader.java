package com.westas.orderassembly;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.ScannerNotClaimedException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TBarcodeReader implements BarcodeReader.BarcodeListener,BarcodeReader.TriggerListener{

    interface TCallBack
    {
        public void OnBarcode(String code);
    }

    private static AidcManager manager;
    private static BarcodeReader reader;
    private TCallBack calback_event;

    public void Init(Context context)
    {

        CreateAidcManager(context);
    }

    public void SetListren(TCallBack calback)
    {
        calback_event = calback;
    }
    public void Close()
    {
        if (reader != null) {
            // close BarcodeReader to clean up resources.
            // once closed, the object can no longer be used.
            reader.close();
        }
        if (manager != null) {
            // close AidcManager to disconnect from the scanner service.
            // once closed, the object can no longer be used.
            manager.close();
        }
    }

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {

        calback_event.OnBarcode(event.getBarcodeData());
    }



    @Override
    public void onTriggerEvent(TriggerStateChangeEvent event) {


    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent arg0) {


    }

    private void CreateAidcManager(Context context)
    {

        AidcManager.create(context, new AidcManager.CreatedCallback() {
            @Override
            public void onCreated(AidcManager aidcManager) {

                manager = aidcManager;
                try
                {
                    reader = manager.createBarcodeReader();
                    CreateBarcodeReader(reader);
                }
                catch (InvalidScannerNameException e) {

                }
                catch (Exception e){

                }
            }

        });
    }

    private void CreateBarcodeReader(BarcodeReader reader)
    {

        if (reader != null) {

            try
            {
                reader.claim();
            }
            catch (ScannerUnavailableException e) {
                e.printStackTrace();
            }


            // register bar code event listener
            reader.addBarcodeListener(this);

            //register trigger state change listener
            //reader.addTriggerListener(this);


            // set the trigger mode to client control
            try
            {
                reader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                        BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
            }
            catch (UnsupportedPropertyException e)
            {


            }

            Map<String, Object> properties = new HashMap<String, Object>();
            // Set Symbologies On/Off
            properties.put(BarcodeReader.PROPERTY_RSS_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_RSS_EXPANDED_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
            properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, false);
            properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, false);
            properties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, false);
            // Set Max Code 39 barcode length
            properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 55);
            //properties.put(BarcodeReader.PROPERTY_GS1_128_MAXIMUM_LENGTH, 155);
            // Turn on center decoding
            properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
            // Enable bad read response
            properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, true);
            // Apply the settings
            reader.setProperties(properties);


        }
    }


}
