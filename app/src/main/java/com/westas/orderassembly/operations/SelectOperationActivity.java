package com.westas.orderassembly.operations;

import android.content.Intent;
import android.os.Bundle;

import android.view.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.*;

import com.westas.orderassembly.*;
import com.westas.orderassembly.invoice.TypeOperation;
import com.westas.orderassembly.transfers.TransfersActivity;

public class SelectOperationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ListOperation listOperation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_operation);

        InitToolbar();
        listOperation = new ListOperation();
        Operation operation;

        operation = new Operation();
        operation.SetCaption("Расходники");
        Operation finalOperation = operation;
        operation.SetOnClick(view -> {
            Intent intent_activity = new Intent("android.intent.action.ListInvoiceActivity");
            intent_activity.putExtra("caption", "Расходники");
            intent_activity.putExtra("uid_sender", "13821171-b44e-49a3-85f4-c39db30da32f");
            intent_activity.putExtra("type_operation", TypeOperation._accept);
            startActivity(intent_activity);
        });
        listOperation.Add(operation);

        operation = new Operation();
        operation.SetCaption("Заказ 108");
        //finalOperation = operation;
        operation.SetOnClick(view -> {
            Intent intent_activity = new Intent("android.intent.action.ListInvoiceActivity");
            intent_activity.putExtra("caption", "Заказ 108");
            intent_activity.putExtra("uid_sender", "108");
            intent_activity.putExtra("type_operation", TypeOperation._accept);
            startActivity(intent_activity);
        });
        listOperation.Add(operation);

        operation = new Operation();
        operation.SetCaption("Заказ 112");
        operation.SetOnClick(view -> {
            Intent intent_activity = new Intent("android.intent.action.ListInvoiceActivity");
            intent_activity.putExtra("caption", "Заказ 112");
            intent_activity.putExtra("uid_sender", "112");
            intent_activity.putExtra("type_operation", TypeOperation._accept);
            startActivity(intent_activity);
        });
        listOperation.Add(operation);

        operation = new Operation();
        operation.SetCaption("Оптиком");
        //finalOperation = operation;
        operation.SetOnClick(view -> {
            Intent intent_activity = new Intent("android.intent.action.ListInvoiceActivity");
            intent_activity.putExtra("caption", "ОптиКом");
            intent_activity.putExtra("uid_sender", "37e3514c-2ce6-11df-940d-001708578b92");
            intent_activity.putExtra("type_operation", TypeOperation._accept);
            startActivity(intent_activity);
        });
        listOperation.Add(operation);

        operation = new Operation();
        operation.SetCaption("Кофе");
        //finalOperation = operation;
        operation.SetOnClick(view -> {
            Intent intent_activity = new Intent("android.intent.action.ListInvoiceActivity");
            intent_activity.putExtra("caption", "Кофе");
            intent_activity.putExtra("uid_sender", "71");
            intent_activity.putExtra("type_operation", TypeOperation._accept);
            startActivity(intent_activity);
        });
        listOperation.Add(operation);

        operation = new Operation();
        operation.SetCaption("Сборка заказа");
        operation.SetOnClick(view -> {
            Intent ListSubdivisionActivity = new Intent("android.intent.action.ListSubdivisionActivity");
            startActivity(ListSubdivisionActivity);
        });
        listOperation.Add(operation);

        operation = new Operation();
        operation.SetCaption("Посылки");
        operation.SetOnClick(view -> startActivity(new Intent(getApplicationContext(), TransfersActivity.class)));
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
