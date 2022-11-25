package com.westas.orderassembly.invoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.westas.orderassembly.*;
import com.westas.orderassembly.rest_service.*;

public class InvoiceClaimActivity extends AppCompatActivity {
    private TextView caption_toolbar, date_toolbar, number_invoice_toolbar;

    private TextView lblClaimNum, lblClaimText;
    private Button btnSend;
    private String uid_invoice;
    private TypeOperation type_operation;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_claim);
        lblClaimNum = findViewById(R.id.lblClaimNum);
        lblClaimText = findViewById(R.id.lblClaimText);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setVisibility(View.INVISIBLE);
        btnSend.setOnClickListener(view -> SendClaim());

        InitToolbar();
        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
            caption_toolbar.setText(parametr.getString("caption"));
            date_toolbar.setText(parametr.getString("date_text"));
            number_invoice_toolbar.setText("№ "+parametr.getString("num_doc"));

            uid_invoice = parametr.getString("uid_invoice");
            type_operation = (TypeOperation)parametr.get("type_operation");
            GetInfoClaim();
        }
    }

    private void InitToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_invoice_claim);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // возврат на предыдущий activity

        caption_toolbar = findViewById(R.id.caption_textview);
        date_toolbar = findViewById(R.id.date_textview);
        number_invoice_toolbar = findViewById(R.id.number_textview);
    }

    void GetInfoClaim(){
        lblClaimNum.setText("...");
        //test
        Claim testData = new Claim();
        testData.num_claim_external="555555555";
        testData.description_claim="Test asdasdasd as dasd ad df sdfsdf sd fsdf sdfrwew sdkjnjkvxncvn xcvxjkcv nxc vxnc vxncv xcvj xcv jxvxc xcverfusdikvjbndufofs vx cvxkjv fx cv";
        MainActivity.GetRestClient().TestExecuteAsyncT(this, testData, new TOnResponce<Claim>() { /*/
        MainActivity.GetRestClient().GetInfoClaim(uid_invoice, type_operation, new TOnResponce<Claim>() { //*/
            @Override
            public void OnSuccess(TResponce<Claim> response) {
                Claim c=response.Data_;
                String num = c.num_claim_external;
                if(num==null || num.isEmpty()) {
                    lblClaimNum.setText("не создана");
                    btnSend.setVisibility(View.VISIBLE);
                } else {
                    lblClaimNum.setText(num);
                }
                lblClaimText.setText(c.description_claim);
            }

            @Override
            public void OnFailure(String message) {
                lblClaimNum.setText("ошибка");
                ShowMessage(message);
            }
        });
    }

    void SendClaim(){
        btnSend.setEnabled(false);
        MainActivity.GetRestClient().TestExecuteAsync(this, new TOnResponce() { /*/
        MainActivity.GetRestClient().SendClaim(uid_invoice, type_operation, new TOnResponce() { //*/
            @Override
            public void OnSuccess(TResponce responce) {
                ShowMessage("Претензия создана");
                onBackPressed();
            }

            @Override
            public void OnFailure(String message) {
                btnSend.setEnabled(true);
                ShowMessage(message);
            }
        });
    }

    void ShowMessage(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

}