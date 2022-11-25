package com.westas.orderassembly.invoice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import com.google.gson.internal.LinkedTreeMap;
import com.westas.orderassembly.*;
import com.westas.orderassembly.rest_service.*;

public class CloseInvoiceActivity extends AppCompatActivity implements TOnResponce {
    private TextView caption_toolbar, date_toolbar, number_invoice_toolbar;

    private TextView information;
    private EditText claimText;
    private Button buttonOK;
    private TypeOperation type_operation;
    private String uid_sender, uid_invoice;
    private AlertDialog alert_dialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_invoice);

        information = findViewById(R.id.information);
        claimText = findViewById(R.id.claimText);
        buttonOK = findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(view -> buttonOk_click());

        InitToolbar();
        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
            caption_toolbar.setText(parametr.getString("caption"));
            date_toolbar.setText(parametr.getString("date_text"));
            number_invoice_toolbar.setText("№ "+parametr.getString("num_doc"));

            uid_sender = parametr.getString("uid_sender");
            uid_invoice = parametr.getString("uid_invoice");
            type_operation = (TypeOperation)parametr.getSerializable("type_operation");
        }

        information.setText("...");
        buttonOK.setEnabled(false);
        MainActivity.GetRestClient().GetDescriptionIncorrectItems(uid_invoice, type_operation, new TOnResponce<DescriptionIncorrectItems>() {
            @Override
            public void OnSuccess(TResponce<DescriptionIncorrectItems> responce) {
                information.setText(responce.Data_.description.trim());
                buttonOK.setEnabled(true);
            }

            @Override
            public void OnFailure(String message) {
                information.setText(message);
                buttonOK.setEnabled(true);
            }
        });

    }

    private void InitToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_items_of_invoice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // возврат на предыдущий activity

        caption_toolbar = findViewById(R.id.caption_textview);
        date_toolbar = findViewById(R.id.date_textview);
        number_invoice_toolbar = findViewById(R.id.number_textview);
    }

    private void buttonOk_click(){
        ShowAlert("Закрытие накладной", "Идет передача данных...", false);
        String claim_text=claimText.getText().toString().trim();
        MainActivity.GetRestClient().TestExecuteAsync(this, this);
        //MainActivity.GetRestClient().CloseInvoice(uid_sender, uid_invoice, claim_text, type_operation, this);
    }

    @Override
    public void OnSuccess(TResponce responce) {
        //HideAlert();
        String msg="Накладная закрыта";
        if(responce.Message!=null && responce.Message.length()>0)
            msg=responce.Message;
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        //Возврат к списку накладных
        Intent intent = new Intent(this, ListInvoiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void OnFailure(String message) {
        //Toast.makeText(getApplicationContext(), "Ошибка!  " + message, Toast.LENGTH_SHORT).show();
        ShowAlert("Ошибка! ", message, true);
    }

    private void ShowAlert(String title, String message, boolean show_cancel) {
        if(alert_dialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(message);
            alert_dialog = builder.create();
        }
        else{
            alert_dialog.setTitle(title);
            alert_dialog.setMessage(message);
        }
        alert_dialog.setCancelable(show_cancel);
        alert_dialog.show();
    }

    private void HideAlert() {
        alert_dialog.hide();
    }


}