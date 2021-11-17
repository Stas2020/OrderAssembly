package com.westas.orderassembly.item;


import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

public class InfoInvoiceActivity extends AppCompatActivity implements TOnResponce {

    private Toolbar toolbar;
    private String uid_invoice ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_invoice);

        InitToolbar();
        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
            uid_invoice = parametr.getString("uid_invoice");
            MainActivity.GetRestClient().GetResultSynchronizedInvoice(uid_invoice,this);
        }
    }

    private void InitToolbar()
    {
        toolbar = findViewById(R.id.toolbar_info_invoice);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });
    }


    @Override
    public void OnSuccess(TResponce responce) {
        TextView info_invoice_txt = findViewById(R.id.infotextView);
        info_invoice_txt.setText(responce.Message);
    }

    @Override
    public void OnFailure(String message) {

    }
}