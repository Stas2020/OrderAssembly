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

    }
    private void InitRecyclerView()
    {
        ListSubdivisionRecyclerView = findViewById(R.id.listSubdivision);
        ListSubdivisionRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        list_sd = new ListSubdivision();

        listSubdivisionAdapter = new ListSubdivisionAdapter(list_sd, this);
        ListSubdivisionRecyclerView.setAdapter(listSubdivisionAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.setting:
                ShowSetting();
                return true;
            case R.id.exit:
                Exit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Клик по Item в RecyclerView
    @Override
    public void onClick(View view) {

        int itemPosition = ListSubdivisionRecyclerView.getChildLayoutPosition(view);

        int NumberSubdivision = list_sd.list.get(itemPosition).Number;

        Intent intent = new Intent(this, ListTransferInvoiceActivity.class);
        startActivity(intent);

        Toast.makeText(this, Integer.toString(NumberSubdivision), Toast.LENGTH_LONG).show();
    }

    private void ShowSetting()
    {
        Intent SettingActivity = new Intent("android.intent.action.SettingActivity_");
        startActivity(SettingActivity);
    }

    private void Exit()
    {
        onBackPressed();// возврат на предыдущий activity
    }
}
