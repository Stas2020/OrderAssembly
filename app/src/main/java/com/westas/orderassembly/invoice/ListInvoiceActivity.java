package com.westas.orderassembly.invoice;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.invoice_items.ItemsInvoiceActivity;
import com.westas.orderassembly.invoice_items.TypeView;
import com.westas.orderassembly.menu_for_list_invoice.ItemMenu;
import com.westas.orderassembly.menu_for_list_invoice.MenuToolbar;
import com.westas.orderassembly.menu_for_list_invoice.OnSelectItemMenu;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListInvoiceActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private TextView caption_of_list_invoice;
    private TextView current_date_toolbar;

    private RecyclerView ListInvoiceRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ListInvoiceAdapter listInvoiceAdapter;
    private ListInvoice list_invoice;

    private static Date current_date;
    private String caption;

    private TypeInvoice type_invoice;
    private String uid_sender;

    private AlertDialog alert_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_invoice);


    }

    private void GetParametrs()
    {
        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
            caption = parametr.getString("caption");
            type_invoice = (TypeInvoice)parametr.get("type_invoice");
            uid_sender = parametr.getString("uid_sender");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(current_date == null)
        {
            current_date = new Date();
        }
        GetParametrs();
        InitToolbar();
        GetListInvoiceBySender();
    }

    private void InitToolbar()
    {
        caption_of_list_invoice = findViewById(R.id.caption_of_list_invoice);
        caption_of_list_invoice.setText(caption);
        current_date_toolbar = findViewById(R.id.current_date);

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

        SetDateInToolbar(current_date);
    }
    private void SetDateInToolbar(Date value)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        current_date_toolbar.setText("Дата накладных: " + dateFormat.format(value));
    }

    private void InitRecyclerView()
    {
        ListInvoiceRecyclerView = findViewById(R.id.list_invoice);
        linearLayoutManager = new LinearLayoutManager(this);
        ListInvoiceRecyclerView.setLayoutManager(linearLayoutManager);

        listInvoiceAdapter = new ListInvoiceAdapter(this, list_invoice, this);
        ListInvoiceRecyclerView.setAdapter(listInvoiceAdapter);
    }


    @Override
    public void onClick(View view) {
        int itemPosition = ListInvoiceRecyclerView.getChildLayoutPosition(view);

        Date date_invoice = list_invoice.GetInvoice(itemPosition).date;
        Date date_order = list_invoice.GetInvoice(itemPosition).date_order;
        String uid_invoice = list_invoice.GetInvoice(itemPosition).uid;
        String num_doc = list_invoice.GetInvoice(itemPosition).num_doc;

        list_invoice.SelectInvoice(itemPosition);

        Intent intent = new Intent(this, ItemsInvoiceActivity.class);
        intent.putExtra("uid_invoice",uid_invoice);
        intent.putExtra("num_doc",num_doc);
        intent.putExtra("date_invoice",date_invoice);
        intent.putExtra("date_order",date_order);
        intent.putExtra("uid_sender",uid_sender);
        intent.putExtra("type_invoice", type_invoice);
        intent.putExtra("type_view", TypeView.not_group);
        intent.putExtra("caption", caption);
        intent.putExtra("current_date", current_date);

        startActivity(intent);
    }
    private void ShowAlert()
    {
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

    private void GetListInvoiceBySender()
    {
        MainActivity.GetRestClient().GetListInvoiceBySender(current_date, uid_sender, type_invoice, new TOnResponce<ListInvoice>() {
            @Override
            public void OnSuccess(TResponce<ListInvoice> responce) {

                if(!responce.Success)
                {
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
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении списка накладных!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetInvoicesFromServer()
    {
        ShowAlert();
        MainActivity.GetRestClient().GetInvoicesFromServer(current_date, uid_sender, type_invoice, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                if (!responce.Success)
                {
                    HideAlert();
                    Toast.makeText(getApplicationContext(), " Ошибка! " + responce.Message, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    HideAlert();
                    GetListInvoiceBySender();
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void OnFailure(Throwable t) {
                HideAlert();
                Toast.makeText(getApplicationContext(), " Ошибка! " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        ItemMenu m_item;
        MenuToolbar menu_toolbar = new MenuToolbar(menu);

        m_item = new ItemMenu();
        m_item.SetCaption("Дата");
        m_item.SetIcon(getDrawable(R.drawable.date_calendar_24));
        m_item.SetOnClickListren(new OnSelectItemMenu() {
            @Override
            public void OnSelect() {
                SelectDate();
            }
        });
        menu_toolbar.Add(m_item);

        m_item = new ItemMenu();
        m_item.SetCaption("Обновить");
        m_item.SetIcon(getDrawable(R.drawable.reload_refresh_24));
        m_item.SetOnClickListren(new OnSelectItemMenu() {
            @Override
            public void OnSelect() {
                GetInvoicesFromServer();
            }
        });
        menu_toolbar.Add(m_item);

        m_item = new ItemMenu();
        m_item.SetCaption("Сгруппировать");
        m_item.SetIcon(getDrawable(R.drawable.list_lines_24));
        m_item.SetOnClickListren(new OnSelectItemMenu() {
            @Override
            public void OnSelect() {
                GroupeInvoice();
            }
        });
        menu_toolbar.Add(m_item);

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

    private void GroupeInvoice()
    {
        Intent intent = new Intent(this, ItemsInvoiceActivity.class);
        intent.putExtra("type_invoice", type_invoice);
        intent.putExtra("type_view", TypeView.group);
        intent.putExtra("current_date", current_date);
        intent.putExtra("caption", caption);
        intent.putExtra("uid_sender", uid_sender);
        startActivity(intent);
    }

    public void SelectDate()
    {
        int mYear, mMonth, mDay;
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog date_pic_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    String dt_str = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                    current_date = format.parse(dt_str);
                    SetDateInToolbar(current_date);
                    GetListInvoiceBySender();

                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        }, mYear, mMonth, mDay);


        date_pic_dialog.show();

    }


}