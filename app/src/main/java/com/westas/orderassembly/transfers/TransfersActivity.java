package com.westas.orderassembly.transfers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.barcode_reader.TOnReadBarcode;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

public class TransfersActivity extends AppCompatActivity implements TOnReadBarcode {
    // префикс Naumen
    final String prefix="https://coffeemania.itsm365.com/sd/operator/?anchor=uuid:serviceCall$";
    TextView txtBarcode, txtTransferNum, txtSender, txtReceiver, txtStatus;
    Button buttonOK;
    String UUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfers);
        InitToolbar();
        txtBarcode = findViewById(R.id.txtBarcode);
        txtTransferNum = findViewById(R.id.txtTransferNum);
        txtSender = findViewById(R.id.txtSender);
        txtReceiver = findViewById(R.id.txtReceiver);
        txtStatus = findViewById(R.id.txtStatus);
        buttonOK = findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(this::buttonOK_Click);

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
        //https://coffeemania.itsm365.com/sd/operator/?anchor=uuid:serviceCall$37401720
        //UUID = 37401720
        txtBarcode.setText(barcode);
        buttonOK.setVisibility(View.INVISIBLE);
        CleanInfo();

        boolean isValidBarcode = barcode.length()>prefix.length() && barcode.indexOf(prefix)==0;
        if(!isValidBarcode) {
            ShowMessage("Неверный штрихкод");
            return;
        }
        UUID=barcode.substring(prefix.length());
        txtBarcode.setText(UUID);
        txtTransferNum.setText("...");
        MainActivity.GetRestClient().GetInfoTransferNaumen(UUID, new TOnResponce<info_transfer>() {
            @Override
            public void OnSuccess(TResponce<info_transfer> responce) {
                info_transfer data=responce.Data_;
                if(data==null) {
                    txtTransferNum.setText("-");
                    ShowMessage("Заявка не найдена: "+UUID);
                    return;
                }
                //String status="inprogress";
                String status=data.state;
                txtTransferNum.setText(String.valueOf(data.num_transfer));
                txtSender.setText(data.sender_name);
                txtReceiver.setText(data.receiver_name);
                txtStatus.setText(GetStatusDescription(status));
                if("inprogress".equals(status) || "transit".equals(status) || "loss".equals(status)) {
                    buttonOK.setVisibility(View.VISIBLE);
                    buttonOK.setEnabled(true);
                } else
                    ShowMessage("Статус заявки не может быть изменён");
            }

            @Override
            public void OnFailure(String message) {
                CleanInfo();
                ShowMessage(message);
            }
        });
    }

    void CleanInfo(){
        txtTransferNum.setText("");
        txtSender.setText("");
        txtReceiver.setText("");
        txtStatus.setText("");
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
            case "closed":
                return "Выполнено";
            default:
                return status;
        }
    }

    public void buttonOK_Click(View v){
        buttonOK.setEnabled(false);
        MainActivity.GetRestClient().TestExecuteAsync(this, new TOnResponce() {
        //MainActivity.GetRestClient().ClosedTransferNaumen(UUID, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                txtStatus.setText("Выполнено");
                txtBarcode.setText("Отсканируйте следующую бирку");
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