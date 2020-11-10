package com.westas.orderassembly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class SelectOperationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_operation);

        InitToolbar();


    Button button_Calculation = (Button) findViewById(R.id.Calculation);

        button_Calculation.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent ListSubdivisionActivity = new Intent("android.intent.action.CalculationActivity");
            startActivity(ListSubdivisionActivity);


        }
    });



        Button button_OrderAsembly = (Button) findViewById(R.id.OrderAsembly);

        button_OrderAsembly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ListSubdivisionActivity = new Intent("android.intent.action.ListSubdivisionActivity");
                startActivity(ListSubdivisionActivity);

            }
        });



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

        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });

    }

}
