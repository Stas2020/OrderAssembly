package com.westas.orderassembly.invoice_items;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;



import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;

import static java.lang.Math.abs;

public class AddItemToInvoiceActivity extends AppCompatActivity implements View.OnTouchListener , TOnSuccessSearchBarcode {
    private String uid_invoice;
    private Fragment fragment_add_item ;
    private Fragment fragment_scan_item;
    private ItemFragment fragment_item;
    private Toolbar toolbar;
    private float x_start;
    private float y_start;
    private float x_end;
    private float y_end;
    private int delta = 100;

    private FloatingActionButton fab_button;
    private InvoiceItem item_to_add;



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_to_invoice);

        InitToolbar();

        fab_button = findViewById(R.id.fab);
        fab_button.setVisibility(View.INVISIBLE);
        fab_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItem();
                Snackbar.make(view, "Добавил новый товар", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        fragment_add_item = AddItemFragment.newInstance("1","2");
        fragment_scan_item = ScanItemFragment.newInstance("1","2");
        fragment_item = ItemFragment.newInstance("1","2");

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, fragment_scan_item)
                .commit();

        View fragment_container_view = findViewById(R.id.fragment_container_view);
        fragment_container_view.setOnTouchListener(this);
    }

    private void InitToolbar()
    {
        toolbar = findViewById(R.id.toolbar_add_item_invoice);
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

    @SuppressLint({"RestrictedApi", "ResourceType"})
    @Override
    public void OnSuccessSearchBarcode(InvoiceItem item) {
        item_to_add = item;
        fab_button.setVisibility(View.VISIBLE);
        fragment_item.ShowItem(item_to_add);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right)
                .replace(R.id.fragment_container_view, fragment_item)
                .commit();

    }

    private void AddItem()
    {
        MainActivity.rest_client.AddItemToInvoice(uid_invoice, item_to_add.barcode);
    }

    @SuppressLint("ResourceType")
    private void TransitFragment()
    {
        if(!fragment_add_item.isVisible())
        {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right)
                    .replace(R.id.fragment_container_view, fragment_add_item)
                    .commit();
        }
        else
        {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right)
                    .replace(R.id.fragment_container_view, fragment_scan_item)
                    .commit();
        }


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                x_start = x;
                y_start = y;
                break;
            case MotionEvent.ACTION_MOVE: // движение
                break;
            case MotionEvent.ACTION_UP: // отпускание
                if(delta < abs(x_start - x))
                {
                    TransitFragment();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }


}