package com.westas.orderassembly.subdivision;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import com.westas.orderassembly.invoice.ListTransferInvoiceActivity;
import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.rest_service.TOnResponceSubDivision;

import java.io.IOException;

public class ListSubdivisionActivity extends AppCompatActivity implements View.OnClickListener, TOnResponceSubDivision {

    private RecyclerView ListSubdivisionRecyclerView;
    private ListSubdivisionAdapter listSubdivisionAdapter;
    private ListSubdivision list_sd;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subdivision);

        InitToolbar();
        GetListSundivision();

    }
    @Override
    public void onResume() {
        super.onResume();
        GetListSundivision();
    }

    private void GetListSundivision()
    {
        MainActivity.rest_client.SetEventSubDivision(this);
        MainActivity.rest_client.GetListSubdivision();
    }

    @Override
    public void OnSuccess(ListSubdivision list_subdivision) {

        list_sd = list_subdivision;
        InitRecyclerView();
    }
    @Override
    public void OnFailure(Throwable t) {
        if (t instanceof IOException) {
            Toast.makeText(this, "Ошибка при получении списка подразделений!", Toast.LENGTH_SHORT).show();
        }
    }

    private void InitToolbar()
    {
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("Подразделения");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });

    }
    private void InitRecyclerView()
    {
        ListSubdivisionRecyclerView = findViewById(R.id.listSubdivision);
        ListSubdivisionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        listSubdivisionAdapter = new ListSubdivisionAdapter(list_sd, this);
        ListSubdivisionRecyclerView.setAdapter(listSubdivisionAdapter);

    }


    //Клик по Item в RecyclerView
    @Override
    public void onClick(View view) {

        int itemPosition = ListSubdivisionRecyclerView.getChildLayoutPosition(view);

        String uid = list_sd.list.get(itemPosition).uid;
        String name = list_sd.list.get(itemPosition).name;

        Intent intent = new Intent(this, ListTransferInvoiceActivity.class);
        intent.putExtra("uid_subdivision",uid);
        intent.putExtra("name_subdivision",name);
        startActivity(intent);

        //Toast.makeText(this, Integer.toString(NumberSubdivision), Toast.LENGTH_LONG).show();
    }


}
