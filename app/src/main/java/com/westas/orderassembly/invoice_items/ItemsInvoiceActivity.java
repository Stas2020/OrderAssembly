package com.westas.orderassembly.invoice_items;


import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.calculator.ParseBarcode;
import com.westas.orderassembly.calculator.QRCode;
import com.westas.orderassembly.dialog.TCallBackDialogQuantity;
import com.westas.orderassembly.dialog.TDialogQuantity;
import com.westas.orderassembly.barcode_reader.TOnReadBarcode;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TOnResponceItemsInvoice;
import com.westas.orderassembly.rest_service.TResponce;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ItemsInvoiceActivity extends AppCompatActivity implements View.OnClickListener, TOnReadBarcode, TOnResponceItemsInvoice, TCallBackDialogQuantity, TOnChangeQuantity, TOnResponce {


    private RecyclerView ListGoodsRecyclerView;
    private ItemsInvoiceAdapter listGoodsAdapter;
    private ListInvoiceItem list_invoiceItem;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;
    private TextView invoice_date;
    private TextView invoice_number;
    private TextView subdivision_name;
    private TDialogQuantity dialog_;
    private int itemPosition;

    private String uid_subdivision ;
    private String name_subdivision ;
    private String uid_invoice ;

    private ParseBarcode parseBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_invoice);

        InitToolbar();
        MainActivity.GetBarcodeReader().SetListren(this);

        dialog_ = new TDialogQuantity(ItemsInvoiceActivity.this,ItemsInvoiceActivity.this);

        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
             uid_subdivision = parametr.getString("uid_subdivision");
             name_subdivision = parametr.getString("name_subdivision");
             uid_invoice = parametr.getString("uid_invoice");

            GetItemsInvoice();
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd MMM");

            SetInvoiceInfo(dateFormat.format(date), "№ "+uid_invoice, name_subdivision);
        }

        parseBarcode = new ParseBarcode();

    }

    private void GetItemsInvoice()
    {
        MainActivity.rest_client.SetEventItemsInvoice(this);
        MainActivity.rest_client.GetItemsOfInvoice(uid_invoice);
    }

    @Override
    public void OnSuccess(ListInvoiceItem items_invoice) {
        list_invoiceItem = items_invoice;
        list_invoiceItem.SortingItem();
        list_invoiceItem.SetEventOfChangeQuantity(this);

        InitRecyclerView();
    }

    @Override
    public void OnFailure(Throwable t) {
        Toast.makeText(this, "Ошибка при получении списка товаров в нвкладной!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnSuccessResponce(TResponce responce) {

        Toast.makeText(this,  responce.Message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnFailureResponce(Throwable t) {
        Toast.makeText(this, "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_items_of_invoice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.print:
                PrintInvoice();
                return true;
            case R.id.close:
                CloseInvoice();
                return true;
            case R.id.add_item:
                AddItemToInvoice();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void InitToolbar()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });

        invoice_date = findViewById(R.id.invoice_date);
        invoice_number = findViewById(R.id.invoice_number);
        subdivision_name = findViewById(R.id.subdivision_name);
    }

    private void SetInvoiceInfo(String date, String number_invoice, String name_subdivision)
    {
        invoice_date.setText(date);
        invoice_number.setText(number_invoice);
        subdivision_name.setText(name_subdivision);
    }

    private void InitRecyclerView()
    {
        ListGoodsRecyclerView = findViewById(R.id.list_items_invoice);
        linearLayoutManager = new LinearLayoutManager(this);
        ListGoodsRecyclerView.setLayoutManager(linearLayoutManager);

        listGoodsAdapter = new ItemsInvoiceAdapter(list_invoiceItem, this);
        ListGoodsRecyclerView.setAdapter(listGoodsAdapter);
    }

    private void CloseInvoice()
    {
        MainActivity.rest_client.SetEventResponce(this);
        MainActivity.rest_client.CloseInvoice(uid_invoice);
    }

    private void PrintInvoice()
    {
        MainActivity.rest_client.SetEventResponce(this);
        MainActivity.rest_client.PrintInvoice(uid_invoice);
    }

    private void AddItemToInvoice()
    {

    }

    //Клик по Item в RecyclerView or click on button close_invoice
    @Override
    public void onClick(View view)
    {
        itemPosition = ListGoodsRecyclerView.getChildLayoutPosition(view);

        String name = list_invoiceItem.GetItems(itemPosition).name;
        double quant = list_invoiceItem.GetItems(itemPosition).quantity;

        dialog_.Show(name,quant);
    }

    @Override
    public void OnChangeQuantity(double quantity)
    {
        list_invoiceItem.ChangeQuantity(quantity,list_invoiceItem.GetItems(itemPosition).barcode);
        listGoodsAdapter.notifyDataSetChanged();
    }

    private void AlertSound()
    {
        Uri notify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notify);
        r.play();
    }

    @Override
    public void OnReadCode(final String code)
    {
        QRCode qr_code = null;
        try
        {
             qr_code = parseBarcode.ParseJSON(code);
        }
        catch(Exception e)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ItemsInvoiceActivity.this, "Не удалось прочитать QRCode!", Toast.LENGTH_SHORT).show();
                    AlertSound();
                }
            });
        }

        if (qr_code == null)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ItemsInvoiceActivity.this, "Не удалось прочитать QRCode!", Toast.LENGTH_SHORT).show();
                    AlertSound();
                }
            });
        }

        boolean verify = list_invoiceItem.VerifyGoods(qr_code.quantity,qr_code.code);

        if (verify)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    listGoodsAdapter.notifyDataSetChanged();
                    ListGoodsRecyclerView.smoothScrollToPosition(0);
                }
            });
        }
        else
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ItemsInvoiceActivity.this, "Не нашел товар!", Toast.LENGTH_SHORT).show();
                    AlertSound();
                }
            });
        }

    }


    @Override
    public void EventChangeQuantity(String uid, String barcode, double quantity) {
        MainActivity.rest_client.SetEventResponce(this);
        MainActivity.rest_client.SetQuantityItem(uid_invoice, uid, quantity);
    }
}
