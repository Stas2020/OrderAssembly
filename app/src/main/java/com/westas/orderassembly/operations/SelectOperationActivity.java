package com.westas.orderassembly.operations;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.westas.orderassembly.R;
import com.westas.orderassembly.invoice.TypeInvoice;
import com.westas.orderassembly.invoice.TypeOperation;


public class SelectOperationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ListOperation listOperation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_operation);

        InitToolbar();
        listOperation = new ListOperation();
        Operation operation;


        operation = new Operation();
        operation.SetCaption("Расходники");
        Operation finalOperation = operation;
        operation.SetOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_activity = new Intent("android.intent.action.ListInvoiceActivity");

                intent_activity.putExtra("caption", "Расходники");
                intent_activity.putExtra("type_invoice", TypeInvoice.invoice_1c);
                intent_activity.putExtra("uid_sender", "112");
                intent_activity.putExtra("type_operation", TypeOperation.receive_);
                startActivity(intent_activity);
            }
        });
        listOperation.Add(operation);

        operation = new Operation();
        operation.SetCaption("Заказ 108");
        //finalOperation = operation;
        operation.SetOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_activity = new Intent("android.intent.action.ListInvoiceActivity");
                intent_activity.putExtra("caption", "Заказ 108");
                intent_activity.putExtra("type_invoice", TypeInvoice.invoice_external);
                intent_activity.putExtra("uid_sender", "108");
                intent_activity.putExtra("type_operation", TypeOperation.receive_);
                startActivity(intent_activity);
            }
        });
        listOperation.Add(operation);

        operation = new Operation();
        operation.SetCaption("Заказ 112");
        operation.SetOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_activity = new Intent("android.intent.action.ListInvoiceActivity");

                intent_activity.putExtra("caption", "Заказ 112");
                intent_activity.putExtra("type_invoice", TypeInvoice.invoice_external);
                intent_activity.putExtra("uid_sender", "112");
                intent_activity.putExtra("type_operation", TypeOperation.receive_);
                startActivity(intent_activity);
            }
        });
        listOperation.Add(operation);

        operation = new Operation();
        operation.SetCaption("Оптиком");
        //finalOperation = operation;
        operation.SetOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_activity = new Intent("android.intent.action.ListInvoiceActivity");

                intent_activity.putExtra("caption", "ОптиКом");
                intent_activity.putExtra("type_invoice", TypeInvoice.invoice_1c);
                intent_activity.putExtra("uid_sender", "37e3514c-2ce6-11df-940d-001708578b92");
                intent_activity.putExtra("type_operation", TypeOperation.receive_);
                startActivity(intent_activity);

            }
        });
        listOperation.Add(operation);

        operation = new Operation();
        operation.SetCaption("Кофе");
        //finalOperation = operation;
        operation.SetOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_activity = new Intent("android.intent.action.ListInvoiceActivity");

                intent_activity.putExtra("caption", "Кофе");
                intent_activity.putExtra("type_invoice", TypeInvoice.invoice_external);
                intent_activity.putExtra("uid_sender", "71");
                intent_activity.putExtra("type_operation", TypeOperation.receive_);
                startActivity(intent_activity);

            }
        });
        listOperation.Add(operation);

        operation = new Operation();
        operation.SetCaption("Сборка заказа");
        operation.SetOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ListSubdivisionActivity = new Intent("android.intent.action.ListSubdivisionActivity");
                startActivity(ListSubdivisionActivity);
            }
        });
        listOperation.Add(operation);

        InitOperationRecyclerView(listOperation);


    }

    private void InitOperationRecyclerView(ListOperation listOperation)
    {
        RecyclerView recycler_view_operation = findViewById(R.id.recycler_view_operation);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view_operation.setLayoutManager(linearLayoutManager);

        recycler_view_operation.setItemAnimator(new DefaultItemAnimator());
        OperationAdapter operationAdapter = new OperationAdapter(listOperation);
        recycler_view_operation.setAdapter(operationAdapter);
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

    private void ShowSetting()
    {
        Intent SettingActivity = new Intent("android.intent.action.SettingActivity_");
        startActivity(SettingActivity);
    }

    private void Exit()
    {
        onBackPressed();// возврат на предыдущий activity
    }

    private void InitToolbar()
    {
        toolbar = findViewById(R.id.toolbar_select_operation);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setSubtitle("   Заказы");


        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });

    }

}
