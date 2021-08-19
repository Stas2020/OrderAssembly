package com.westas.orderassembly.invoice;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.invoice_items.ItemsInvoiceActivity;
import com.westas.orderassembly.invoice_items.TypeView;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListTransferInvoice1C_Activity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private TextView num_invoice;
    private RecyclerView ListInvoiceRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ListInvoiceAdapter listInvoiceAdapter;
    private ListInvoice list_invoice;
    private static Date curend_date;
    private TextView selected_date_tw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transfer_invoice1c_activity);

        InitToolbar();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(curend_date == null)
        {
            curend_date = new Date();
        }
        GetListInvoice(curend_date);
    }

    private void InitToolbar()
    {
        num_invoice = findViewById(R.id.num_invoice);
        num_invoice.setText("Накладные");
        selected_date_tw = findViewById(R.id.date_invoices);

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
    }

    private void InitRecyclerView()
    {
        ListInvoiceRecyclerView = findViewById(R.id.list_invoice1C);
        linearLayoutManager = new LinearLayoutManager(this);
        ListInvoiceRecyclerView.setLayoutManager(linearLayoutManager);

        listInvoiceAdapter = new ListInvoiceAdapter(this, list_invoice, this);
        ListInvoiceRecyclerView.setAdapter(listInvoiceAdapter);
    }

    private void ShowDateInToolbar()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MMMM.yyyy", Locale.getDefault());
        selected_date_tw.setText("за: " + dateFormat.format(curend_date));
    }

    @Override
    public void onClick(View view) {
        int itemPosition = ListInvoiceRecyclerView.getChildLayoutPosition(view);

        Date date_invoice = list_invoice.GetInvoice(itemPosition).date;
        String uid_invoice = list_invoice.GetInvoice(itemPosition).uid;
        String num_doc = list_invoice.GetInvoice(itemPosition).num_doc;

        list_invoice.SelectInvoice(itemPosition);

        Intent intent = new Intent(this, ItemsInvoiceActivity.class);
        intent.putExtra("uid_invoice",uid_invoice);
        intent.putExtra("num_doc",num_doc);
        intent.putExtra("date_invoice",date_invoice);
        intent.putExtra("type_invoice", TypeInvoice.invoice_1c);
        intent.putExtra("type_view", TypeView.not_group);

        startActivity(intent);
    }

    private void GetListInvoice(Date date)
    {
        ShowDateInToolbar();

        MainActivity.GetRestClient().GetListInvoice1C(date, new TOnResponce<ListInvoice>() {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_invoice_1c, menu);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.date_invoice_1c:
                SelectDate();
                return true;
            case R.id.groupe_1с_invoice:
                GroupeInvoice_1С();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void GroupeInvoice_1С()
    {
        Intent intent = new Intent(this, ItemsInvoiceActivity.class);
        intent.putExtra("type_invoice", TypeInvoice.invoice_1c);
        intent.putExtra("type_view", TypeView.group);
        intent.putExtra("current_date", curend_date);
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
                    curend_date = format.parse(dt_str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                GetListInvoice(curend_date);
            }
        }, mYear, mMonth, mDay);

        date_pic_dialog.show();

    }


}