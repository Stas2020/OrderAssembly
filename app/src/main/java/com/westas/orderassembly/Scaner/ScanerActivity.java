package com.westas.orderassembly.Scaner;



import android.os.Bundle;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.barcode_reader.TOnReadBarcode;

import java.util.Calendar;

public class ScanerActivity extends AppCompatActivity implements TOnReadBarcode {

    private Toolbar toolbar;
    private ScanerItems scan_items;
    private RecyclerView rw_scan_item;
    private ScanerAdapter scan_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaner);

        InitToolbar();
        scan_items = new ScanerItems();
        MainActivity.GetBarcodeReader().SetListener(this);

        InitRecyclerView();
    }

    private void InitToolbar()
    {
        toolbar = findViewById(R.id.toolbar_scaner);
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
        rw_scan_item = findViewById(R.id.rw_scan_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rw_scan_item.setLayoutManager(linearLayoutManager);


        rw_scan_item.setItemAnimator(new DefaultItemAnimator());
        scan_adapter = new ScanerAdapter(scan_items);
        rw_scan_item.setAdapter(scan_adapter);

    }

    @Override
    public void OnReadCode(String code) {
        ItemScan it = new ItemScan();
        it.code_value = code;
        it.time = Calendar.getInstance().getTime();

        scan_items.Add(it);
        runOnUiThread(() -> {
            scan_adapter.notifyDataSetChanged();
        });

    }
}