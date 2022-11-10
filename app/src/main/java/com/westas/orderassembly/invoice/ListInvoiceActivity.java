package com.westas.orderassembly.invoice;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.barcode_reader.TOnReadBarcode;
import com.westas.orderassembly.item.ItemsActivity;
import com.westas.orderassembly.item.TypeView;
import com.westas.orderassembly.menu_helper.MenuHelper;

import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListInvoiceActivity extends AppCompatActivity implements TOnReadBarcode {
    private Toolbar toolbar;
    private TextView caption_of_list_invoice;
    private TextView current_date_toolbar;

    private RecyclerView ListInvoiceRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ListInvoiceAdapter listInvoiceAdapter;
    private ListBoxAdapter listBoxAdapter;
    private ListInvoice list_invoice;
    private ListBox list_box;

    private static Date current_date;
    private String caption;

    private TypeOperation type_operation;
    private String uid_sender;
    private String uid_receiver;

    private AlertDialog alert_dialog;
    private FloatingActionButton button_switch_to_box;
    private boolean show_box = false;
    private boolean show_open_invoice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_invoice);


    }

    private void GetParametrs() {
        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
            caption = parametr.getString("caption");
            type_operation = (TypeOperation)parametr.get("type_operation");
            uid_sender = parametr.getString("uid_sender");
            uid_receiver = parametr.getString("uid_receiver");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.GetBarcodeReader().SetListener(this);

        if(current_date == null) {
            current_date = new Date();
        }
        GetParametrs();
        InitToolbar();
        switch (type_operation) {
            case _accept: {
                if(show_box){
                    GetListBoxesBySender();
                }
                else{
                        if(show_open_invoice){
                            GetListOpenInvoiceBySender();
                        }
                        else{
                            GetListInvoiceBySender();
                        }
                }
                break;
            }
            case _assembly: {
                GetListInvoiceByReceiver();
                break;
            }
        }

    }

    private void InitToolbar() {
        caption_of_list_invoice = findViewById(R.id.caption_of_list_invoice);
        caption_of_list_invoice.setText(caption);
        current_date_toolbar = findViewById(R.id.current_date);
        button_switch_to_box = (FloatingActionButton) findViewById(R.id.floating_button_box);
        button_switch_to_box.bringToFront();
        button_switch_to_box.setOnClickListener(v -> {
            if(!show_box){
                show_box = true;
                button_switch_to_box.setImageDrawable(getResources().getDrawable(R.drawable.invoice));
                GetListBoxesBySender();
            }
            else{
                show_box = false;
                button_switch_to_box.setImageDrawable(getResources().getDrawable(R.drawable.box_address_64));
                GetListInvoiceBySender();
            }
        });

        toolbar = findViewById(R.id.toolbar_of_invoice);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });

        switch (type_operation){
            case _accept:{
                SetDateInToolbar(current_date);
                break;
            }
            case _assembly:{
                SetDateInToolbar(new Date());
                break;
            }
        }

    }
    private void SetDateInToolbar(Date value) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        current_date_toolbar.setText(dateFormat.format(value));
    }
    private void SetDateInToolbar(String value) {
        current_date_toolbar.setText(value);
    }

    private void InitRecyclerView() {

        ListInvoiceRecyclerView = findViewById(R.id.list_invoice);
        linearLayoutManager = new LinearLayoutManager(this);
        ListInvoiceRecyclerView.setLayoutManager(linearLayoutManager);

        if(show_box){
            listBoxAdapter = new ListBoxAdapter(this, list_box, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = ListInvoiceRecyclerView.getChildLayoutPosition(view);
                    String uid_box = list_box.GetBox(itemPosition).uid;
                    String name_box = list_box.GetBox(itemPosition).name;
                    list_box.SelectBox(itemPosition);

                    Intent intent = new Intent(getApplicationContext(), ItemsActivity.class);
                    intent.putExtra("num_doc",name_box);
                    intent.putExtra("uid_sender",uid_sender);
                    intent.putExtra("uid_receiver",uid_receiver);
                    intent.putExtra("type_operation", type_operation);
                    intent.putExtra("type_view", TypeView.not_group);
                    intent.putExtra("caption", caption);
                    intent.putExtra("current_date", current_date);
                    intent.putExtra("uid_box", uid_box);
                    intent.putExtra("show_box", show_box);

                    startActivity(intent);

                }
            });
            ListInvoiceRecyclerView.setAdapter(listBoxAdapter);
        }
        else{
            listInvoiceAdapter = new ListInvoiceAdapter(this, list_invoice, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = ListInvoiceRecyclerView.getChildLayoutPosition(view);

                    Date date_invoice = list_invoice.GetInvoice(itemPosition).date;
                    Date date_order = list_invoice.GetInvoice(itemPosition).date_order;
                    String uid_invoice = list_invoice.GetInvoice(itemPosition).uid;
                    String num_doc = list_invoice.GetInvoice(itemPosition).num_doc;

                    list_invoice.SelectInvoice(itemPosition);

                    Intent intent = new Intent(getApplicationContext(), ItemsActivity.class);
                    intent.putExtra("uid_invoice",uid_invoice);
                    intent.putExtra("num_doc",num_doc);
                    intent.putExtra("date_invoice",date_invoice);
                    intent.putExtra("date_order",date_order);
                    intent.putExtra("uid_sender",uid_sender);
                    intent.putExtra("uid_receiver",uid_receiver);
                    intent.putExtra("type_operation", type_operation);
                    intent.putExtra("type_view", TypeView.not_group);
                    intent.putExtra("caption", caption);
                    intent.putExtra("current_date", current_date);

                    startActivity(intent);
                }
            });
            ListInvoiceRecyclerView.setAdapter(listInvoiceAdapter);
        }
    }



    private void ShowAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Обновление");
        builder.setMessage("Идет получение данных, ждите...");
        alert_dialog = builder.create();
        alert_dialog.setCancelable(false);
        alert_dialog.show();
    }

    private void HideAlert()
    {
        alert_dialog.hide();
    }

    private void GetListInvoiceBySender() {
        show_open_invoice = false;
        button_switch_to_box.setEnabled(true);
        MainActivity.GetRestClient().GetListInvoiceBySender(current_date, uid_sender, type_operation, new TOnResponce<ListInvoice>() {
            @Override
            public void OnSuccess(TResponce<ListInvoice> responce) {

                if(!responce.Success) {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }

                String uid ="";
                if(list_invoice!= null){
                    uid = list_invoice.GetUidSelected();
                }

                list_invoice = responce.Data_;

                if(!uid.isEmpty()){
                    list_invoice.SelectedByUid(uid);
                }

                InitRecyclerView();
            }

            @Override
            public void OnFailure(String message) {
                Toast.makeText(getApplicationContext(), "Ошибка: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetListOpenInvoiceBySender() {
        ShowAlert();
        show_open_invoice = true;
        button_switch_to_box.setEnabled(false);
        MainActivity.GetRestClient().GetListOpenInvoiceBySender( uid_sender, type_operation, new TOnResponce<ListInvoice>() {
            @Override
            public void OnSuccess(TResponce<ListInvoice> responce) {

                if(!responce.Success) {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    HideAlert();
                    return;
                }

                String uid ="";
                if(list_invoice!= null){
                    uid = list_invoice.GetUidSelected();
                }

                list_invoice = responce.Data_;

                if(!uid.isEmpty()){
                    list_invoice.SelectedByUid(uid);
                }

                SetDateInToolbar("Открытые накладные");
                InitRecyclerView();
                HideAlert();
            }

            @Override
            public void OnFailure(String message) {
                Toast.makeText(getApplicationContext(), "Ошибка: " + message, Toast.LENGTH_SHORT).show();
                HideAlert();
            }
        });
    }

    private void GetListBoxesBySender() {
        MainActivity.GetRestClient().GetListBoxesBySender(current_date, uid_sender, type_operation, new TOnResponce<ListBox>() {
            @Override
            public void OnSuccess(TResponce<ListBox> responce) {

                if(!responce.Success) {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }

                String uid ="";
                if(list_box!= null){
                    uid = list_box.GetUidSelected();
                }

                list_box = responce.Data_;

                if(!uid.isEmpty()){
                    list_box.SelectedByUid(uid);
                }

                InitRecyclerView();
            }

            @Override
            public void OnFailure(String message) {
                Toast.makeText(getApplicationContext(), "Ошибка: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void GetListInvoiceByReceiver() {

        MainActivity.GetRestClient().GetListInvoiceByReceiver(current_date, uid_receiver, type_operation, new TOnResponce<ListInvoice>() {
            @Override
            public void OnSuccess(TResponce<ListInvoice> responce) {

                if(!responce.Success) {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }

                String uid ="";
                if(list_invoice!= null){
                    uid = list_invoice.GetUidSelected();
                }

                list_invoice = responce.Data_;

                if(!uid.isEmpty()){
                    list_invoice.SelectedByUid(uid);
                }

                InitRecyclerView();
            }

            @Override
            public void OnFailure(String message) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении списка накладных!  " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetInvoicesFromServer()
    {
        ShowAlert();
        MainActivity.GetRestClient().GetInvoicesFromServer(current_date, uid_sender, type_operation, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                if (!responce.Success) {
                    HideAlert();
                    Toast.makeText(getApplicationContext(), " Ошибка! " + responce.Message, Toast.LENGTH_SHORT).show();
                }
                else {
                    HideAlert();
                    if(show_box){
                        GetListBoxesBySender();
                    }
                    else{
                        GetListInvoiceBySender();
                    }

                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void OnFailure(String message) {
                HideAlert();
                Toast.makeText(getApplicationContext(), " Ошибка! " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        switch (type_operation){
            case _assembly:{
                //SetMenuForAssenbly(menu);
                break;
            }
            case _accept:{
                SetMenuForReceive(menu);
                break;
            }
        }

        //Код с рефлексией, используемый для включения в пункты меню иконок
        if(menu.getClass().getSimpleName()
                .equals("MenuBuilder")){
            try{
                Method m = menu.getClass()
                        .getDeclaredMethod (
                                "setOptionalIconsVisible",
                                Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            }
            catch(NoSuchMethodException e){
                System.err.println("onCreateOptionsMenu");
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    void SetMenuForAssenbly(Menu menu)
    {
        MenuHelper menu_helper = new MenuHelper(menu);

        menu_helper.Add("Дата", getDrawable(R.drawable.date_calendar_24), new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SelectDate();
                return true;
            }
        });

        menu_helper.Add("Обновить", getDrawable(R.drawable.reload_refresh_24), new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                GetInvoicesFromServer();
                return true;
            }
        });

        menu_helper.Add("Сгруппировать", getDrawable(R.drawable.list_lines_24), new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                GroupeInvoice();
                return true;
            }
        });
    }

    void SetMenuForReceive(Menu menu)
    {
        MenuHelper menu_helper = new MenuHelper(menu);

        menu_helper.Add("Дата", getDrawable(R.drawable.date_calendar_24), new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SelectDate();
                return true;
            }
        });

        menu_helper.Add("Обновить", getDrawable(R.drawable.reload_refresh_24), new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                GetInvoicesFromServer();
                return true;
            }
        });

        menu_helper.Add("Сгруппировать", getDrawable(R.drawable.list_lines_24), new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                GroupeInvoice();
                return true;
            }
        });

        menu_helper.Add("Открытые накладые", getDrawable(R.drawable.padlock_open_24), new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(!show_box){
                    GetListOpenInvoiceBySender();
                }

                return true;
            }
        });
    }

    private void GroupeInvoice() {
        Intent intent = new Intent(this, ItemsActivity.class);

        intent.putExtra("type_view", TypeView.group);
        intent.putExtra("current_date", current_date);
        intent.putExtra("caption", caption);
        intent.putExtra("uid_sender", uid_sender);
        intent.putExtra("type_operation", type_operation);
        startActivity(intent);
    }

    public void SelectDate() {
        int mYear, mMonth, mDay;
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog date_pic_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dt_str = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                try {
                    current_date = format.parse(dt_str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SetDateInToolbar(current_date);
                if(show_box){
                    GetListBoxesBySender();
                }
                else{
                    GetListInvoiceBySender();
                }
            }
        }, mYear, mMonth, mDay);

        date_pic_dialog.show();
    }



    @Override
    public void OnReadCode(String code) {
        if(show_box){
            String uid_box = code;
            Box box = list_box.GetBoxByUid(uid_box);
            if(box == null){
                ShowMessage("Не нашел коробку!");
                return;
            }
            String name_box = box.name;

            Intent intent = new Intent(getApplicationContext(), ItemsActivity.class);
            intent.putExtra("num_doc",name_box);
            intent.putExtra("uid_sender",uid_sender);
            intent.putExtra("uid_receiver",uid_receiver);
            intent.putExtra("type_operation", type_operation);
            intent.putExtra("type_view", TypeView.not_group);
            intent.putExtra("caption", caption);
            intent.putExtra("current_date", current_date);
            intent.putExtra("uid_box", uid_box);
            intent.putExtra("show_box", show_box);

            startActivity(intent);
        }
    }

    private void ShowMessage(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}