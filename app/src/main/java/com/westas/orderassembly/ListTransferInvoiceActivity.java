package com.westas.orderassembly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ListTransferInvoiceActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView ListInvoiceRecyclerView;
    private ListTransferInvoiceAdapter listInvoiceAdapter;
    private ListTransferInvoice list_invoice;
    private Toolbar toolbar;
    private TextView subdivision_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transfer_invoice);

        InitToolbar();
        SetNameSubdivision("Кофейня на Рождественке");
        InitRecyclerView();

    }

    private void SetNameSubdivision(String name)
    {
        subdivision_name.setText(name);
    }
    private void InitToolbar()
    {
        subdivision_name = findViewById(R.id.subdivision_name);
        toolbar = findViewById(R.id.toolbar);
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
        ListInvoiceRecyclerView = findViewById(R.id.list_invoice);
        ListInvoiceRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        list_invoice = new ListTransferInvoice();

        listInvoiceAdapter = new ListTransferInvoiceAdapter(list_invoice, this);
        ListInvoiceRecyclerView.setAdapter(listInvoiceAdapter);

    }


    //Клик по Item в RecyclerView
    @Override
    public void onClick(View view) {

        int itemPosition = ListInvoiceRecyclerView.getChildLayoutPosition(view);


        Intent intent = new Intent(this, ItemsInvoiceActivity.class);
        startActivity(intent);
    }
}
