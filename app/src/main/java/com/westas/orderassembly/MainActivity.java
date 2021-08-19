package com.westas.orderassembly;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.westas.orderassembly.barcode_reader.TBarcodeReader;
import com.westas.orderassembly.operations.SelectOperationActivity;
import com.westas.orderassembly.rest_service.RestClient;
import com.westas.orderassembly.setting.Settings;

import static java.lang.Integer.parseInt;

public class MainActivity extends Activity implements LoginFragment.TOnClickOk{

    private static Settings settings;
    private static RestClient rest_client;
    private static TBarcodeReader code_reader;


    private void LoadSetting()
    {
        settings = new Settings();
        SharedPreferences shared_preferance = PreferenceManager.getDefaultSharedPreferences(this);
        settings.host = shared_preferance.getString("NAME_SERVER","localhost");
        settings.port = parseInt(shared_preferance.getString("NAME_PORT","8085"));
        settings.num_term = parseInt(shared_preferance.getString("NUM_TERMINAL","-1"));
    }

    public final static Settings GetSettings()
    {
        return  settings;
    }

    public static final RestClient GetRestClient()
    {
        return  rest_client;
    }

    private void CreateRestClient()
    {
        rest_client = new RestClient(settings);
        rest_client.BuildRetrofit();
    }
    private void CreateCodeReader()
    {
        code_reader = new TBarcodeReader();
        code_reader.Init(this);
    }
    static public TBarcodeReader GetBarcodeReader()
    {
        return code_reader;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadSetting();
        CreateCodeReader();
        CreateRestClient();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LoginFragment login_fragment = new LoginFragment();
        login_fragment.SetOnClickListern(this);
        fragmentTransaction.add(R.id.keyboard_login, login_fragment);
        fragmentTransaction.commit();

        Test();

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
        if (code_reader != null)
        {
            code_reader.Close();
        }
    }



    private void Test()
    {
        Button button_show_activity = (Button) findViewById(R.id.button_show_activity);
        button_show_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the intent action string from AndroidManifest.xml
                Intent intent = new Intent("android.intent.action.MainActivity2");
                startActivity(intent);

                //rest_client.GetListSubdivision();
                /*
                rest_client.GetListInvoice1C(new Date(), new TOnResponce() {
                    @Override
                    public void OnSuccess(TResponce responce) {
                        TResponce responce_ = responce;
                    }

                    @Override
                    public void OnFailure(Throwable t) {

                    }
                });
*/
                //rest_client.GetDataSubdivision();
                //rest_client.GetInvoiceItem("123211");

            }
        });
    }
}
