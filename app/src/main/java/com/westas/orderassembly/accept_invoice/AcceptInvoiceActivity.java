package com.westas.orderassembly.accept_invoice;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.invoice.ListTransferInvoice;
import com.westas.orderassembly.invoice.ListTransferInvoiceAdapter;
import com.westas.orderassembly.rest_service.TOnResponceAcceptInvoice;

public class AcceptInvoiceActivity extends AppCompatActivity implements TOnResponceAcceptInvoice {

    private ListAcceptedInvoice list_accepted_invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_invoice);

        InitToolbar();
        GetListAcceptInvoice("205");


    }

    private void InitToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
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
    private void InitRecyclerView()
    {
        RecyclerView RecyclerViewByAcceptInvoices = findViewById(R.id.list_accept_invoice);
        RecyclerViewByAcceptInvoices.setLayoutManager(new LinearLayoutManager(this));

        ListInvoiceAcceptAdapter adapter = new ListInvoiceAcceptAdapter(this, list_accepted_invoice);
        RecyclerViewByAcceptInvoices.setAdapter(adapter);
    }

    private void GetListAcceptInvoice(String uid_subdivision)
    {
        MainActivity.rest_client.SetEventAcceptInvoice(this);
        MainActivity.rest_client.GetAcceptInvoice(uid_subdivision);
    }

    @Override
    public void OnSuccess(ListAcceptedInvoice list_accept_invoice) {
        list_accepted_invoice = list_accept_invoice;
        InitRecyclerView();
    }

    @Override
    public void OnFailure(Throwable t) {
        Toast.makeText(this, "Ошибка при получении списка накладных!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}