package com.westas.orderassembly.invoice_items;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.dialog.TCallBackDialogQuantity;
import com.westas.orderassembly.dialog.TDialogQuantity;
import com.westas.orderassembly.barcode_reader.TOnReadBarcode;
import com.westas.orderassembly.rest_service.TOnResponceItemsInvoice;


public class ItemsInvoiceActivity extends AppCompatActivity implements View.OnClickListener, TOnReadBarcode, TOnResponceItemsInvoice, TCallBackDialogQuantity {


    private RecyclerView ListGoodsRecyclerView;
    private ItemsInvoiceAdapter listGoodsAdapter;
    private ListInvoiceItem list_invoiceItem;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;
    private TextView invoice_date;
    private TextView invoice_number;
    private TextView subdivision_name;
    private Button close_invoice;
    private TDialogQuantity dialog_;
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_invoice);

        InitToolbar();
        MainActivity.GetBarcodeReader().SetListren(this);


        dialog_ = new TDialogQuantity(ItemsInvoiceActivity.this,ItemsInvoiceActivity.this);

        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
            String uid_subdivision = parametr.getString("uid_subdivision");
            String name_subdivision = parametr.getString("name_subdivision");
            String uid_invoice = parametr.getString("uid_invoice");

            GetItemsInvoice(uid_invoice);
            SetInvoiveInfo("12/05", "№ "+uid_subdivision, name_subdivision);
        }

    }

    private void GetItemsInvoice(String uid_invoice)
    {
        MainActivity.rest_client.SetEvent(this);
        MainActivity.rest_client.GetInvoiceItem(uid_invoice);
    }

    @Override
    public void OnSuccess(ListInvoiceItem items_invoice) {
        list_invoiceItem = items_invoice;
        InitRecyclerView();
    }

    @Override
    public void OnFailure(Throwable t) {
        Toast.makeText(this, "Ошибка при получении списка товаров в нвкладной!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void InitToolbar()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        close_invoice = findViewById(R.id.close_invoice);
        close_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseInvoice();
            }
        });

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

    private void SetInvoiveInfo(String date, String number_invoice, String name_subdivision)
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

    }

    //Клик по Item в RecyclerView or click on button close_invoice
    @Override
    public void onClick(View view) {

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

    @Override
    public void OnReadCode(final String code)
    {

        String Barcode = "8763698";
        Barcode = code;
        double quantity = 1;

        boolean verify = list_invoiceItem.VerifyGoods(quantity,Barcode);


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
                }
            });
        }


    }


}
