package com.westas.orderassembly.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.westas.orderassembly.invoice_items.ItemsInvoiceActivity;
import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.rest_service.TOnResponceListInvoice;

import java.util.Date;

public class ListTransferInvoiceActivity extends AppCompatActivity implements View.OnClickListener, TOnResponceListInvoice {

    private RecyclerView ListInvoiceRecyclerView;
    private ListTransferInvoiceAdapter listInvoiceAdapter;
    private ListTransferInvoice list_invoice;
    private Toolbar toolbar;
    private TextView subdivision_name;

    String uid_subdivision;
    String name_subdivision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transfer_invoice);

        InitToolbar();

        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
             uid_subdivision = parametr.getString("uid_subdivision");
             name_subdivision = parametr.getString("name_subdivision");

            SetNameSubdivision(name_subdivision);
            GetListInvoice(uid_subdivision);
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

        listInvoiceAdapter = new ListTransferInvoiceAdapter(list_invoice, this);
        ListInvoiceRecyclerView.setAdapter(listInvoiceAdapter);
    }

    //Клик по Item в RecyclerView
    @Override
    public void onClick(View view) {

        int itemPosition = ListInvoiceRecyclerView.getChildLayoutPosition(view);

        Date date_invoice = list_invoice.list.get(itemPosition).date;
        String uid_invoice = list_invoice.list.get(itemPosition).uid;

        Intent intent = new Intent(this, ItemsInvoiceActivity.class);
        intent.putExtra("uid_subdivision",uid_subdivision);
        intent.putExtra("name_subdivision",name_subdivision);
        intent.putExtra("uid_invoice",uid_invoice);
        intent.putExtra("date_invoice",date_invoice);

        startActivity(intent);
    }

    private void GetListInvoice(String uid_sundivision)
    {
        MainActivity.rest_client.SetEvent(this);
        MainActivity.rest_client.GetInvoice(uid_sundivision);
    }
    @Override
    public void OnSuccess(ListTransferInvoice list_transfer_invoice) {
        list_invoice = list_transfer_invoice;
        InitRecyclerView();
    }

    @Override
    public void OnFailure(Throwable t) {
        Toast.makeText(this, "Ошибка при получении списка накладных!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
