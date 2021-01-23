package com.westas.orderassembly.invoice_items;


import android.app.Fragment;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.calculator.ParseBarcode;
import com.westas.orderassembly.calculator.QRCode;
import com.westas.orderassembly.dialog.TCallBackDialog;
import com.westas.orderassembly.dialog.TCallBackDialogQuantity;
import com.westas.orderassembly.dialog.TDialogForm;
import com.westas.orderassembly.barcode_reader.TOnReadBarcode;
import com.westas.orderassembly.dialog.TDialogQuestion;
import com.westas.orderassembly.dialog.TTypeForm;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TOnResponceItemsInvoice;
import com.westas.orderassembly.rest_service.TResponce;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ItemsInvoiceActivity extends AppCompatActivity implements  View.OnClickListener,
                                                                        View.OnLongClickListener,
                                                                        TOnReadBarcode,
                                                                        TOnResponceItemsInvoice,
                                                                        TCallBackDialogQuantity,
                                                                        TOnChangeQuantity,
                                                                        TOnResponce{


    private RecyclerView ListGoodsRecyclerView;
    private ItemsInvoiceAdapter listGoodsAdapter;
    private ListInvoiceItem list_invoiceItem;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;
    private TextView invoice_date;
    private TextView invoice_number;
    private TextView subdivision_name;
    private Fragment fragment_add;
    private Fragment fragment_scan;
    private TDialogForm dialog_quantity;
    private TDialogForm dialog_print_label;
    private TDialogQuestion dialog_question;
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

        dialog_quantity = new TDialogForm(ItemsInvoiceActivity.this,ItemsInvoiceActivity.this,"Количество", TTypeForm.change);
        dialog_print_label = new TDialogForm(ItemsInvoiceActivity.this,ItemsInvoiceActivity.this,"Печать этикетки",TTypeForm.label);
        dialog_question = new TDialogQuestion(this,"Удалить?");

        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
             uid_subdivision = parametr.getString("uid_subdivision");
             name_subdivision = parametr.getString("name_subdivision");
             uid_invoice = parametr.getString("uid_invoice");

            //GetItemsInvoice();
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd MMM");

            SetInvoiceInfo(dateFormat.format(date), "№ "+uid_invoice, name_subdivision);
        }

        parseBarcode = new ParseBarcode();

        FloatingActionButton floating_button_down = (FloatingActionButton) findViewById(R.id.floating_button_down);
        floating_button_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDown();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        GetItemsInvoice();
    }

    private void PageDown()
    {
        int count_item = listGoodsAdapter.getItemCount();
        ListGoodsRecyclerView.smoothScrollToPosition(count_item);
        Toast.makeText(getApplicationContext(), "Page down!  ", Toast.LENGTH_SHORT).show();
    }
    private void PageUp()
    {
        ListGoodsRecyclerView.smoothScrollToPosition(0);
        Toast.makeText(getApplicationContext(), "Page up!  ", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Ошибка при получении списка товаров в накладной!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnSuccessResponce(TResponce responce) {
        //GetItemsInvoice();
        Toast.makeText(this,  responce.Message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnFailureResponce(Throwable t) {
        Toast.makeText(this, "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_items_of_invoice, menu);

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
            case R.id.print:
                PrintInvoice();
                return true;
            case R.id.close:
                CloseInvoice();
                return true;
            case R.id.add_item:
                AddItemToInvoice();
                return true;
            case R.id.info_by_invoice:
                InfoByInvoice();
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


        ListGoodsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        listGoodsAdapter = new ItemsInvoiceAdapter(this, list_invoiceItem, this,this);
        ListGoodsRecyclerView.setAdapter(listGoodsAdapter);



        SwipeCallback swipe_callback = new SwipeCallback()
        {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                InvoiceItem item = listGoodsAdapter.GetItem(position);
                listGoodsAdapter.removeItem(position);
                EventDeleteItemFromInvoice(item, position);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipe_callback);
        itemTouchhelper.attachToRecyclerView(ListGoodsRecyclerView);
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

    private void EventDeleteItemFromInvoice(InvoiceItem item, int position)
    {
        InvoiceItem item_for_delete = item;
        dialog_question.Show(item.barcode + " " + item.name, new TCallBackDialog() {
            @Override
            public void OnSuccess(boolean flag) {
                if (flag == true)
                {
                    MainActivity.rest_client.DeleteItemFromInvoice(uid_invoice,item.uid,item.barcode);
                    Toast.makeText(getApplicationContext(), "Удалил товар из накладной.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listGoodsAdapter.restoreItem(item, position);
                }
            }
        });
    }

    private void AddItemToInvoice()
    {
        Intent intent = new Intent(this, AddItemToInvoiceActivity.class);
        intent.putExtra("uid_invoice",uid_invoice);
        startActivity(intent);
    }

    private void InfoByInvoice() {
        Intent intent = new Intent(this, InfoInvoiceActivity.class);
        intent.putExtra("uid_invoice",uid_invoice);
        startActivity(intent);
    }
    //Клик по Item в RecyclerView or click on button close_invoice
    @Override
    public void onClick(View view)
    {
        itemPosition = ListGoodsRecyclerView.getChildLayoutPosition(view);

        String name = list_invoiceItem.GetItems(itemPosition).name;
        double quant = list_invoiceItem.GetItems(itemPosition).quantity;

        dialog_quantity.Show(name,quant);
    }

    @Override
    public boolean onLongClick(View v) {
        dialog_print_label.Show("Количество этикеток",0);
        return false;
    }

    @Override
    public void OnChangeQuantity(float quantity, TTypeForm type_)
    {
        switch (type_)
        {
            case label: {
                PrintLabel((int)quantity,list_invoiceItem.GetItems(itemPosition));
                break;
            }
            case change:{
                list_invoiceItem.ChangeQuantity(quantity,list_invoiceItem.GetItems(itemPosition).barcode);
                listGoodsAdapter.notifyDataSetChanged();
                ListGoodsRecyclerView.smoothScrollToPosition(0);
                break;
            }
        }

    }

    private void PrintLabel(int count_label, InvoiceItem invoiceItem)
    {
        MainActivity.rest_client.SetEventResponce(this);
        MainActivity.rest_client.PrintLabel(uid_invoice, invoiceItem.uid, count_label);

    }

    private void Alert(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        AlertSound();
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
            runOnUiThread(() -> Alert("Не удалось прочитать QRCode!"));
        }

        if (qr_code == null)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Alert("Не удалось прочитать QRCode!");
                }
            });
        }

        boolean verify = list_invoiceItem.VerifyGoods(qr_code.quantity,qr_code.code);

        if (verify)
        {
            runOnUiThread(() -> {
                listGoodsAdapter.notifyDataSetChanged();
                ListGoodsRecyclerView.smoothScrollToPosition(0);
            });
        }
        else
        {
            runOnUiThread(() -> Alert("Не нашел товар!"));
        }

    }


    @Override
    public void EventChangeQuantity(String uid, String barcode, float quantity) {
        MainActivity.rest_client.SetEventResponce(this);
        MainActivity.rest_client.SetQuantityItem(uid_invoice, uid, quantity,barcode);
    }

}
