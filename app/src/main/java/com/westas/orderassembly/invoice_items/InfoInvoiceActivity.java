package com.westas.orderassembly.invoice_items;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
            MainActivity.rest_client.SetEventResponce(this);
            MainActivity.rest_client.GetResultSynchronizedInvoice(uid_invoice);
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
    public void OnSuccessResponce(TResponce responce) {
        TextView info_invoice_txt = findViewById(R.id.infotextView);
        info_invoice_txt.setText(responce.Message);
    }

    @Override
    public void OnFailureResponce(Throwable t) {

    }
}