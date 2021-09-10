package com.westas.orderassembly.calculator;

import android.os.Bundle;




import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.barcode_reader.TOnReadBarcode;


public class CalculationActivity extends AppCompatActivity implements TOnReadBarcode {

    private Toolbar toolbar;
    private RecyclerView ListBarcodeRecyclerView;
    private BarcodeAdapter barcodeAdapter;
    private ListBarcode list_barcode;
    private ParseBarcode parseBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);


        parseBarcode = new ParseBarcode();
        list_barcode = new ListBarcode();

        InitToolbar();
        InitRecyclerView();

        MainActivity.GetBarcodeReader().SetListren(this);
    }


    @Override
    public void OnReadCode(final String code)
    {
        final Barcode barcode = parseBarcode.Parse(code);

        if (barcode !=null)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    list_barcode.AddBarcode(barcode);
                    barcodeAdapter.notifyDataSetChanged();
                }
            });
        }
        else
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(CalculationActivity.this, "Не удалось определить баркод!", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void InitRecyclerView()
    {
        ListBarcodeRecyclerView = findViewById(R.id.list_barcode);
        ListBarcodeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        barcodeAdapter = new BarcodeAdapter(list_barcode);
        ListBarcodeRecyclerView.setAdapter(barcodeAdapter);

    }

    private void InitToolbar()
    {
        toolbar = findViewById(R.id.toolbar_calculation);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("Калькулятор");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });

    }

}
