package com.westas.orderassembly.accept_invoice;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.westas.orderassembly.invoice.TypeInvoice;
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

public class AcceptInvoiceActivity extends AppCompatActivity implements TOnResponce<ListAcceptedInvoice>, View.OnClickListener{

    private RecyclerView RecyclerViewByAcceptInvoices;
    private ListAcceptedInvoice list_accepted_invoice;
    private static Date curend_date;
    private int mYear, mMonth, mDay;
    private TextView selected_date_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_invoice);

    }

    @Override
    public void onResume() {
        super.onResume();

        if(curend_date == null)
        {
            curend_date = new Date();
        }

        InitToolbar();
        GetListAcceptInvoice(curend_date);
    }

    private void InitToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar_of_invoice);
        selected_date_text = findViewById(R.id.selected_date);
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
    private void ShowDateInToolbar()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MMMM.yyyy", Locale.getDefault());
        selected_date_text.setText("  Накладные за: " + dateFormat.format(curend_date));
    }
    private void InitRecyclerViewInvoices()
    {
        RecyclerViewByAcceptInvoices = findViewById(R.id.list_accept_invoice);
        RecyclerViewByAcceptInvoices.setLayoutManager(new LinearLayoutManager(this));

        ListInvoiceAcceptAdapter adapter = new ListInvoiceAcceptAdapter(this, list_accepted_invoice, this);
        RecyclerViewByAcceptInvoices.setAdapter(adapter);
    }
    private void InitRecyclerViewItems()
    {
        RecyclerViewByAcceptInvoices = findViewById(R.id.list_accept_invoice);
        RecyclerViewByAcceptInvoices.setLayoutManager(new LinearLayoutManager(this));

        ListInvoiceAcceptAdapter adapter = new ListInvoiceAcceptAdapter(this, list_accepted_invoice, this);
        RecyclerViewByAcceptInvoices.setAdapter(adapter);
    }

    private void GetListAcceptInvoice(Date date)
    {
        ShowDateInToolbar();
        MainActivity.GetRestClient().GetAcceptInvoice(date, this);
    }

    @Override
    public void OnSuccess(TResponce<ListAcceptedInvoice> responce) {
        list_accepted_invoice = responce.Data_;
        InitRecyclerViewInvoices();
    }

    @Override
    public void OnFailure(Throwable t) {
        Toast.makeText(this, "Ошибка при получении списка накладных!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_purchase_invoice, menu);

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
            case R.id.date_purch_invoice:
                SelectDate();
                return true;
            case R.id.update_purch_invoice:
                GetPurchaseInvoice();
                return true;
            case R.id.groupe_purch_invoice:
                GroupePurchaseInvoice();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void GroupePurchaseInvoice()
    {
        Intent intent = new Intent(this, ItemsInvoiceActivity.class);
        intent.putExtra("type_invoice", TypeInvoice.invoice_external);
        intent.putExtra("type_view", TypeView.group);
        intent.putExtra("current_date", curend_date);
        startActivity(intent);
    }

    private void GetPurchaseInvoice()
    {
        ShowDateInToolbar();

        MainActivity.GetRestClient().GetInvoicesFromServer(curend_date,"",TypeInvoice.invoice_external, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                if (!responce.Success)
                {
                    Toast.makeText(getApplicationContext(), " Ошибка! " + responce.Message, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    GetListAcceptInvoice(curend_date);
                }
            }

            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), " Ошибка! " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SelectDate()
    {
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
                GetListAcceptInvoice(curend_date);
            }
        }, mYear, mMonth, mDay);

        date_pic_dialog.show();

    }


    //Клик по Item в RecyclerView
    @Override
    public void onClick(View view) {

        int itemPosition = RecyclerViewByAcceptInvoices.getChildLayoutPosition(view);

        Date date_invoice = list_accepted_invoice.list.get(itemPosition).date;
        String uid_invoice = list_accepted_invoice.list.get(itemPosition).uid;
        String num_doc = list_accepted_invoice.list.get(itemPosition).num_doc;

        Intent intent = new Intent(this, ItemsInvoiceActivity.class);
        intent.putExtra("uid_invoice",uid_invoice);
        intent.putExtra("num_doc",num_doc);
        intent.putExtra("date_invoice",date_invoice);
        intent.putExtra("type_invoice", TypeInvoice.invoice_external);
        intent.putExtra("type_view", TypeView.not_group);
        startActivity(intent);
    }
}