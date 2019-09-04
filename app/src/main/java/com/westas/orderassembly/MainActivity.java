package com.westas.orderassembly;

import android.app.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements TBarcodeReader.TCallBack, TDialogQuantity.TCallBackDialogQuantity{

    private static AidcManager manager;
    private static BarcodeReader reader;
    private Button button_start;
    private Button button_show_activity;
    private static TBarcodeReader BR;


    static public TBarcodeReader GetBarcodeReader()
    {
        return BR;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BR = new TBarcodeReader();
        BR.Init(this);



        /*
        // create the AidcManager providing a Context and an
        // CreatedCallback implementation.
        AidcManager.create(this, new AidcManager.CreatedCallback() {

            @Override
            public void onCreated(AidcManager aidcManager) {
                manager = aidcManager;
                try {

                    reader = manager.createBarcodeReader();
                }
                catch (InvalidScannerNameException e) {
                    Toast.makeText(MainActivity.this, "Invalid Scanner Name Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }




        });

*/

        button_show_activity = (Button) findViewById(R.id.button_show_activity);
        button_show_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the intent action string from AndroidManifest.xml
                //Intent barcodeIntent = new Intent("android.intent.action.scaner");
                Intent ListSubdivisionActivity = new Intent("android.intent.action.ListSubdivisionActivity");
                startActivity(ListSubdivisionActivity);
            }
        });





        button_start = (Button) findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                TDialogQuantity dialog_ = new TDialogQuantity(MainActivity.this,MainActivity.this);
                dialog_.Show("NameGoods",555);

                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Quantity");
                builder.setMessage("Name goods");

                View linearlayout = getLayoutInflater().inflate(R.layout.dialog_quantity, null);
                builder.setView(linearlayout);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });



                AlertDialog dialog = builder.create();
                dialog.show();



                 */
                //Intent SettingActivity = new Intent("android.intent.action.SettingActivity_");
                //startActivity(SettingActivity);

                //List<BarcodeReaderInfo>list_barcode = manager.listBarcodeDevices();
                //String name_dev = list_barcode.get(0).getName();
                //Toast.makeText(MainActivity.this, name_dev, Toast.LENGTH_SHORT).show();
            }
        });





    }
    @Override
    public void OnChangeQuantity(double quantity)
    {
        Toast.makeText(MainActivity.this, "Код: " + Double.toString(quantity), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void OnBarcode(final String code)
    {
        runOnUiThread(new Runnable() {
        @Override
        public void run() {
            // update UI to reflect the data
            TextView textView = findViewById(R.id.TextOut);
            textView.setText(code);
            Toast.makeText(MainActivity.this, "Код: " + code, Toast.LENGTH_SHORT).show();
        }
    });

    }

    static BarcodeReader getBarcodeObject() {
        return reader;
    }
    static AidcManager getAidcManagerObject() {
        return manager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (BR != null) {

            //BR.Close();
        }

    }
}
