package com.westas.orderassembly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import com.westas.orderassembly.barcode_reader.TBarcodeReader;
import com.westas.orderassembly.operations.SelectOperationActivity;
import com.westas.orderassembly.rest_service.RestClient;
import com.westas.orderassembly.setting.Settings;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements LoginFragment.TOnClickOk{

    private static final boolean DEVELOPER_MODE = true;
    private static Settings settings;
    private static RestClient rest_client;
    private static TBarcodeReader code_reader;


    private void LoadSetting()
    {
        settings = new Settings();
        SharedPreferences shared_preference = PreferenceManager.getDefaultSharedPreferences(this);
        settings.host = shared_preference.getString("NAME_SERVER","localhost");
        settings.port = parseInt(shared_preference.getString("NAME_PORT","8085"));
        settings.num_term = parseInt(shared_preference.getString("NUM_TERMINAL","-1"));
    }

    public static Settings GetSettings()
    {
        return  settings;
    }

    public static RestClient GetRestClient()
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

        if (DEVELOPER_MODE) {
            /*
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
             */
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    //.detectNonSdkApiUsage()
                    //.detectLeakedSqlLiteObjects()
                    //.detectLeakedClosableObjects()
                    //.penaltyLog()
                    //.penaltyDeath()
                    .build());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetVersion();

        LoadSetting();
        CreateCodeReader();
        CreateRestClient();

        LoginFragment login_fragment = new LoginFragment();
        login_fragment.SetOnClickListern(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView_keyboard_login, login_fragment, null)
                    .commit();
        }



    }

    private void SetVersion()
    {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        versionName += "." + String.valueOf(versionCode);
        TextView tv_version_info = findViewById(R.id.textView_version_info);
        tv_version_info.setText(versionName);
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




}
