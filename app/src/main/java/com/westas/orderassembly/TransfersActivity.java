package com.westas.orderassembly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.westas.orderassembly.barcode_reader.TOnReadBarcode;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

public class TransfersActivity extends AppCompatActivity implements TOnReadBarcode {
    // префикс Naumen
    final String prefix="https://coffeemania.itsm365.com/sd/operator/#uuid:";
    TextView txtBarcode, txtStatus;
    LinearLayout statusInfo;
    Button buttonOK;
    String UUID, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfers);
        InitToolbar();
        txtBarcode = findViewById(R.id.txtBarcode);
        txtStatus = findViewById(R.id.txtStatus);
        statusInfo = findViewById(R.id.statusInfo);
        buttonOK = findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(this::buttonOK_Click);

        statusInfo.setVisibility(View.INVISIBLE);
        buttonOK.setVisibility(View.INVISIBLE);
    }

    private void InitToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_transfers);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // возврат на предыдущий activity
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.GetBarcodeReader().SetListener(this);
        // test
        new Thread(() -> {
            try {Thread.sleep(1000);} catch (InterruptedException ignored) {}
            runOnUiThread(() -> ProcessBarcode("https://coffeemania.itsm365.com/sd/operator/#uuid:serviceCall$37401720"));
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.GetBarcodeReader().SetListener(null);
    }

    @Override
    public void OnReadCode(String barcode) {
        runOnUiThread(() -> ProcessBarcode(barcode));
    }

    void ProcessBarcode(String barcode){
        //Пример ссылки в бирке:
        //https://coffeemania.itsm365.com/sd/operator/#uuid:serviceCall$37401720
        //UUID = serviceCall$37401720
        txtBarcode.setText(barcode);
        statusInfo.setVisibility(View.INVISIBLE);
        buttonOK.setVisibility(View.INVISIBLE);

        boolean isValidBarcode = barcode.length()>prefix.length() && barcode.indexOf(prefix)==0;
        if(!isValidBarcode) {
            ShowMessage("Неверный штрихкод");
            return;
        }
        UUID=barcode.substring(prefix.length());

        statusInfo.setVisibility(View.VISIBLE);
        txtStatus.setText("...");
        //MainActivity.GetRestClient().GetTransferStatus(UUID, new TOnResponce<String>() {
        MainActivity.GetRestClient().TestExecuteAsync(this, "inprogress", new TOnResponce<String>() {
            @Override
            public void OnSuccess(TResponce<String> responce) {
                status=responce.Data_;
                txtStatus.setText(GetStatusDescription(status));
                if(status!="inprogress" && status!="transit" && status!="loss")
                    ShowMessage("Статус заявки не может быть изменён");
                else {
                    buttonOK.setVisibility(View.VISIBLE);
                    buttonOK.setEnabled(true);
                }
            }

            @Override
            public void OnFailure(String message) {
                ShowMessage(message);
            }
        });
    }

    String GetStatusDescription(String status){
        switch (status){
            case "inprogress":
            case "transit":
                return "В пути";
            case "loss":
                return "Потеря";
            case "resolved":
                return "Выполнено";
            default:
                return status;
        }
    }

    public void buttonOK_Click(View v){
        buttonOK.setEnabled(false);
        //MainActivity.GetRestClient().SetTransferStatus(UUID, "inprogress", new TOnResponce() {
        MainActivity.GetRestClient().TestExecuteAsync2(this, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                txtStatus.setText("Выполнено");
                buttonOK.setVisibility(View.INVISIBLE);
                ShowMessage("Груз принят");
            }

            @Override
            public void OnFailure(String message) {
                buttonOK.setEnabled(true);
                ShowMessage(message);
            }
        });
    }

    void ShowMessage(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}