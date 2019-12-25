package com.westas.orderassembly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class ListSubdivisionActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView ListSubdivisionRecyclerView;
    private ListSubdivisionAdapter listSubdivisionAdapter;
    private ListSubdivision list_sd;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subdivision);

        InitToolbar();
        InitRecyclerView();
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


        list_sd = new ListSubdivision();

        listSubdivisionAdapter = new ListSubdivisionAdapter(list_sd, this);
        ListSubdivisionRecyclerView.setAdapter(listSubdivisionAdapter);

    }


    //Клик по Item в RecyclerView
    @Override
    public void onClick(View view) {

        int itemPosition = ListSubdivisionRecyclerView.getChildLayoutPosition(view);

        int NumberSubdivision = list_sd.list.get(itemPosition).Number;
        String NameSubdivision = list_sd.list.get(itemPosition).Name;

        Intent intent = new Intent(this, ListTransferInvoiceActivity.class);
        intent.putExtra("NumberSubdivision",NumberSubdivision);
        intent.putExtra("NameSubdivision",NameSubdivision);
        startActivity(intent);

        //Toast.makeText(this, Integer.toString(NumberSubdivision), Toast.LENGTH_LONG).show();
    }


}
