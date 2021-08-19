package com.westas.orderassembly.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.westas.orderassembly.invoice_items.ItemsInvoiceActivity;
import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.invoice_items.TypeView;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

import java.util.Date;

public class ListTransferInvoiceActivity extends AppCompatActivity implements View.OnClickListener, TOnResponce<ListInvoice> {

    private RecyclerView ListInvoiceRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ListInvoiceAdapter listInvoiceAdapter;
    private ListInvoice list_invoice;
    private Toolbar toolbar;
    private TextView subdivision_name;
    private int first_position = 0;

    String uid_subdivision;
    String name_subdivision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transfer_invoice);

        InitToolbar();
    }

    @Override
    public void onResume() {
        super.onResume();

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
        subdivision_name = findViewById(R.id.date_invoice);
        toolbar = findViewById(R.id.toolbar_of_invoice);
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
        ListInvoiceRecyclerView = findViewById(R.id.list_invoice1C);
        linearLayoutManager = new LinearLayoutManager(this);
        ListInvoiceRecyclerView.setLayoutManager(linearLayoutManager);

        listInvoiceAdapter = new ListInvoiceAdapter(this, list_invoice, this);
        ListInvoiceRecyclerView.setAdapter(listInvoiceAdapter);
    }

    //Клик по Item в RecyclerView
    @Override
    public void onClick(View view) {

        int itemPosition = ListInvoiceRecyclerView.getChildLayoutPosition(view);

        Date date_invoice = list_invoice.GetInvoice(itemPosition).date;
        String uid_invoice = list_invoice.GetInvoice(itemPosition).uid;
        String num_doc = list_invoice.GetInvoice(itemPosition).num_doc;

        list_invoice.SelectInvoice(itemPosition);

        Intent intent = new Intent(this, ItemsInvoiceActivity.class);
        intent.putExtra("uid_subdivision",uid_subdivision);
        intent.putExtra("name_subdivision",name_subdivision);
        intent.putExtra("uid_invoice",uid_invoice);
        intent.putExtra("num_doc",num_doc);
        intent.putExtra("date_invoice",date_invoice);
        intent.putExtra("type_invoice", TypeInvoice.invoice_external);
        intent.putExtra("type_view", TypeView.not_group);

        startActivity(intent);
    }

    private void GetListInvoice(String uid_sundivision)
    {
        MainActivity.GetRestClient().GetInvoice(uid_sundivision, this);
    }


    @Override
    public void OnSuccess(TResponce<ListInvoice> responce) {
        String uid ="";
        if(list_invoice!= null){
            uid = list_invoice.GetUidSelected();
        }

        list_invoice = responce.Data_;
        if(!uid.isEmpty()){
            list_invoice.SelectedByUid(uid);
        }

        InitRecyclerView();
        if(linearLayoutManager!= null)
        {
            linearLayoutManager.scrollToPosition(first_position);
            Log.i("MESSAGE", "first_position: " + String.valueOf(first_position));
        }
    }

    @Override
    public void OnFailure(Throwable t) {
        Toast.makeText(this, "Ошибка при получении списка накладных!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
