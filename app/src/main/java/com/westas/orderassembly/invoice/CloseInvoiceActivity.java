package com.westas.orderassembly.invoice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import com.westas.orderassembly.*;
import com.westas.orderassembly.rest_service.*;

public class CloseInvoiceActivity extends AppCompatActivity implements TOnResponce {
    TextView information;
    EditText claimText;
    Button buttonOK;
    TypeOperation type_operation;
    String caption, uid_sender, uid_invoice, num_doc, date_text;
    Integer items_count=0, items_skipped=0;
    AlertDialog alert_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_invoice);

        information = findViewById(R.id.information);
        claimText = findViewById(R.id.claimText);
        buttonOK = findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(view -> buttonOk_click());

        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
            caption = parametr.getString("caption");
            date_text = parametr.getString("date_text");
            type_operation = (TypeOperation)parametr.getSerializable("type_operation");
            uid_sender = parametr.getString("uid_sender");
            uid_invoice = parametr.getString("uid_invoice");
            num_doc = parametr.getString("num_doc");
            items_count = parametr.getInt("items_count", 0);
            items_skipped = parametr.getInt("items_skipped", 0);
        }
        InitToolbar();
        information.setText(String.format("Всего позиций: %d\nНесоответствий: %d", items_count, items_skipped));
    }

    private void InitToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_items_of_invoice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // возврат на предыдущий activity

        TextView caption_toolbar = findViewById(R.id.caption_textview);
        TextView date_toolbar = findViewById(R.id.date_textview);
        TextView number_invoice_toolbar = findViewById(R.id.number_textview);

        caption_toolbar.setText(caption);
        number_invoice_toolbar.setText("№ "+num_doc);
    }

    private void buttonOk_click(){
        ShowAlert("Закрытие накладной", "Идет передача данных...", false);
        String claim_text=claimText.getText().toString().trim();
        MainActivity.GetRestClient().CloseInvoice(uid_sender, uid_invoice, claim_text, type_operation, this);
    }

    @Override
    public void OnSuccess(TResponce responce) {
        //HideAlert();
        if(responce.Message!=null && responce.Message.length()>0)
            Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Накладная закрыта", Toast.LENGTH_SHORT).show();

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