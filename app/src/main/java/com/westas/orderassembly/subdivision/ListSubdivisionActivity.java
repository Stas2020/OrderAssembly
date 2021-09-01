package com.westas.orderassembly.subdivision;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.westas.orderassembly.invoice.ListTransferInvoiceActivity;
import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

import java.io.IOException;

public class ListSubdivisionActivity extends AppCompatActivity implements View.OnClickListener, TOnResponce<ListSubdivision> {

    private RecyclerView ListSubdivisionRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ListSubdivisionAdapter listSubdivisionAdapter;
    private ListSubdivision list_sd;
    private Toolbar toolbar;
    private int first_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subdivision);

        InitToolbar();
    }
    @Override
    public void onResume() {
        super.onResume();
        GetListSundivision();
    }

    private void GetListSundivision()
    {
        MainActivity.GetRestClient().GetListSubdivision(this);
    }

    @Override
    public void OnSuccess(TResponce<ListSubdivision> responce) {
        String uid ="";
        if(list_sd!= null){
            uid = list_sd.GetUidSelected();
        }

        list_sd = responce.Data_;
        if(!uid.isEmpty()){
            list_sd.SelectedByUid(uid);
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
        getSupportActionBar().setSubtitle("   Подразделения");

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
        linearLayoutManager = new LinearLayoutManager(this);
        ListSubdivisionRecyclerView.setLayoutManager(linearLayoutManager);

        listSubdivisionAdapter = new ListSubdivisionAdapter(list_sd, this);
        ListSubdivisionRecyclerView.setAdapter(listSubdivisionAdapter);

    }

    //Клик по Item в RecyclerView
    @Override
    public void onClick(View view) {

        int itemPosition = ListSubdivisionRecyclerView.getChildLayoutPosition(view);
        first_position = linearLayoutManager.findFirstVisibleItemPosition();

        Log.i("MESSAGE", "itemPosition: " + String.valueOf(itemPosition));


        String uid = list_sd.GetSubdivision(itemPosition).uid;
        String name = list_sd.GetSubdivision(itemPosition).name;

        list_sd.SelectSubdivision(itemPosition);

        Intent intent = new Intent(this, ListTransferInvoiceActivity.class);
        intent.putExtra("uid_subdivision",uid);
        intent.putExtra("name_subdivision",name);
        startActivity(intent);

        //Toast.makeText(this, Integer.toString(NumberSubdivision), Toast.LENGTH_LONG).show();
    }


}
