package com.westas.orderassembly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class ListTransferInvoiceActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView ListInvoiceRecyclerView;
    private ListTransferInvoiceAdapter listInvoiceAdapter;
    private ListTransferInvoice list_invoice;
    private Toolbar toolbar;
    private TextView subdivision_name;

    int num_subdivision = -1;
    String name_subdivision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transfer_invoice);

        InitToolbar();
        InitRecyclerView();

        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
            num_subdivision = parametr.getInt("NumberSubdivision");
            name_subdivision = parametr.getString("NameSubdivision");

            SetNameSubdivision(name_subdivision);
        }


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

        Date DateInvoice = list_invoice.list.get(itemPosition).DateInvoice;
        String Number = list_invoice.list.get(itemPosition).Number;
        int toSubdivision = list_invoice.list.get(itemPosition).toSubdivision;

        Intent intent = new Intent(this, ItemsInvoiceActivity.class);
        intent.putExtra("NumberSubdivision",num_subdivision);
        intent.putExtra("NameSubdivision",name_subdivision);
        intent.putExtra("NumberInvoice",Number);
        intent.putExtra("DateInvoice",DateInvoice);

        startActivity(intent);
    }
}
