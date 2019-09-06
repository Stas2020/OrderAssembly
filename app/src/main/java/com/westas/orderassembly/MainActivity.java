package com.westas.orderassembly;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements LoginFragment.TOnClickOk{

    private static TBarcodeReader BR;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BR = new TBarcodeReader();
        BR.Init(this);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LoginFragment login_fragment = new LoginFragment();
        login_fragment.SetOnClickListern(this);
        fragmentTransaction.add(R.id.keyboard_login, login_fragment);
        fragmentTransaction.commit();


        Button button_show_activity = (Button) findViewById(R.id.button_show_activity);
        button_show_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the intent action string from AndroidManifest.xml
                //Intent barcodeIntent = new Intent("android.intent.action.scaner");

            }
        });

    }

    //Event of Login form
    @Override
    public void OnClickOk(String password)
    {
        if (password.equals("0"))
        {
            Intent ListSubdivisionActivity = new Intent("android.intent.action.ListSubdivisionActivity");
            startActivity(ListSubdivisionActivity);
        }
        else
        {
            Toast.makeText(MainActivity.this, "Не верный пароль!", Toast.LENGTH_SHORT).show();
        }

    }

    static public TBarcodeReader GetBarcodeReader()
    {
        return BR;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (BR != null) {

            BR.Close();
        }

    }
}
