package com.westas.orderassembly.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;


public class SettingActivity_ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Настройки");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                onBackPressed();// возврат на предыдущий activity
            }
        });

        // Display the fragment as the FrameLayout.
        getFragmentManager().beginTransaction().add(R.id.fragment,new SettingsFragment()).commit();



    }
}
