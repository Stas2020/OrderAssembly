package com.westas.orderassembly;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.westas.orderassembly.barcode_reader.TBarcodeReader;
import com.westas.orderassembly.rest_service.RestClient;

public class MainActivity extends Activity implements LoginFragment.TOnClickOk{

    private static TBarcodeReader BR;
    public static RestClient rest_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BR = new TBarcodeReader();
        BR.Init(this);

        rest_client = new RestClient();
        rest_client.shared_preferance = PreferenceManager.getDefaultSharedPreferences(this);
        rest_client.BuildRetrofit();



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



                //rest_client.GetDataSubdivision();
                rest_client.GetInvoiceItem("123211");

            }
        });

    }

    //Event of Login form
    @Override
    public void OnClickOk(String password)
    {
        if (password.equals("0"))
        {
            Intent intent = new Intent(this, SelectOperationActivity.class);
            startActivity(intent);
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
    protected void onResume() {
        super.onResume();
        if(rest_client != null)
        {
            rest_client.BuildRetrofit();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BR != null) {
            BR.Close();
        }
    }
}
