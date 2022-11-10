package com.westas.orderassembly.item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.westas.orderassembly.*;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

public class SkipItemActivity extends AppCompatActivity implements TOnResponce{
    private TextView itemName;
    private EditText text_notes;
    private RadioGroup radio_group;
    private RadioButton radio_option1, radio_option2, radio_option3;
    private Button buttonOK;
    String uid_invoice, uid_item, name;
    float qty, qty_required;
    StatusSkip status = StatusSkip.qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip_item);
        InitToolbar();

        itemName = findViewById(R.id.itemName);
        text_notes = findViewById(R.id.text_notes);
        radio_group = findViewById(R.id.radio_group);
        radio_option1 = findViewById(R.id.radio_option1); // Позиция не пришла
        radio_option2 = findViewById(R.id.radio_option2); // Другое количество
        radio_option3 = findViewById(R.id.radio_option3); // Другое наименование
        buttonOK = findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(view -> buttonOk_click());

        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
            uid_invoice = parametr.getString("uid_invoice");
            uid_item = parametr.getString("uid_item");
            name = parametr.getString("name");
            qty = parametr.getFloat("qty");
            qty_required = parametr.getFloat("qty_required");
        }
        itemName.setText(name+", принято "+qty+" из "+qty_required);

        if(qty==0) {
            radio_option1.setChecked(true);
            radio_option2.setEnabled(false);
        } else {
            radio_option1.setEnabled(false);
            radio_option2.setChecked(true);
            radio_option3.setEnabled(false);
        }
        radio_group.setOnCheckedChangeListener((radioGroup, option_id) -> radio_option_changed(option_id));
    }

    private void InitToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_skip_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // возврат на предыдущий activity
    }

    private void radio_option_changed(int option_id){
        switch (option_id) {
            case R.id.radio_option1:  // Позиция не пришла
                status = StatusSkip.qty;
                break;
            case R.id.radio_option2:  // Другое количество
                status = StatusSkip.qty;
                break;
            case R.id.radio_option3:  // Другое наименование (пересорт)
                status = StatusSkip.peresort;
                text_notes.setText(name);
                break;
        }
        int a = option_id==R.id.radio_option1 ? View.INVISIBLE : View.VISIBLE;
        text_notes.setVisibility(a);
    }

    private void buttonOk_click(){
        String skipNotes=text_notes.getText().toString().trim();
        if(status==StatusSkip.peresort && skipNotes.length()==0) {
            Toast.makeText(this, "Не указано новое наименование", Toast.LENGTH_SHORT).show();
            return;
        }
        MainActivity.GetRestClient().SkipItemInInvoice(uid_invoice, uid_item, status, skipNotes, this);
        buttonOK.setEnabled(false);
    }

    @Override
    public void OnSuccess(TResponce responce) {
        if(responce.Success)
            finish(); // на предыдущую форму
        else
            OnFailure("Не удалось передать данные");
    }

    @Override
    public void OnFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        buttonOK.setEnabled(true);
    }
}
