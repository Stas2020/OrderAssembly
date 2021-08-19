package com.westas.orderassembly.invoice_items;


import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.Scaner.ScanerActivity;
import com.westas.orderassembly.WiFi.TStatusWiFi;
import com.westas.orderassembly.WiFi.TUtilsWiFi;
import com.westas.orderassembly.calculator.ParseBarcode;
import com.westas.orderassembly.calculator.QRCode;
import com.westas.orderassembly.dialog.TCallBackDialog;
import com.westas.orderassembly.dialog.TCallBackDialogQuantity;
import com.westas.orderassembly.dialog.TDialogForm;
import com.westas.orderassembly.barcode_reader.TOnReadBarcode;
import com.westas.orderassembly.dialog.TDialogQuestion;
import com.westas.orderassembly.dialog.TTypeForm;
import com.westas.orderassembly.invoice.TypeInvoice;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;
import com.westas.orderassembly.toolbar.ToolbarItemsOfInvoice;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public class ItemsInvoiceActivity extends AppCompatActivity implements  View.OnClickListener, View.OnLongClickListener, TOnReadBarcode, TCallBackDialogQuantity
{

    private RecyclerView ListGoodsRecyclerView;
    private ItemsInvoiceAdapter listGoodsAdapter;
    private ListInvoiceItem list_invoiceItem;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;

    private TextView caption_toolbar;
    private TextView date_toolbar;
    private TextView number_invoice_toolbar;



    private TextView invoice_date;
    private TextView invoice_number;
    private TextView subdivision_name;
    private TDialogForm dialog_quantity;
    private TDialogForm dialog_print_label;
    private TDialogQuestion dialog_question;
    private int selected_position;

    private String uid_subdivision ;
    private String name_subdivision ;
    private String uid_invoice ;
    private String num_doc ;

    private ParseBarcode parseBarcode;
    private TypeInvoice type_invoice;
    private TypeView type_view;
    private Date current_date;
    private Date date_invoice;

    int num_term;

    TUtilsWiFi utils_wifi;
    private Toolbar toolbar_status;
    private TextView message_toolbar;

    private AlertDialog alert_dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_invoice_new);

        InitToolbar();
        MainActivity.GetBarcodeReader().SetListren(this);

        dialog_quantity = new TDialogForm(ItemsInvoiceActivity.this,ItemsInvoiceActivity.this,"Количество", TTypeForm.change);
        dialog_print_label = new TDialogForm(ItemsInvoiceActivity.this,ItemsInvoiceActivity.this,"Печать этикетки",TTypeForm.label);
        dialog_question = new TDialogQuestion(this,"Удалить?");

        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
            type_view = (TypeView) parametr.get("type_view");
            type_invoice = (TypeInvoice)parametr.get("type_invoice");
            current_date = (Date)parametr.get("current_date");
            date_invoice = (Date)parametr.get("date_invoice");

            switch (type_view)
            {
                case group:
                {
                    DateFormat dateFormat = new SimpleDateFormat("dd MMM");
                    //SetInvoiceInfo(dateFormat.format(current_date), "", "");
                    break;
                }

                case not_group:
                {
                    uid_subdivision = parametr.getString("uid_subdivision");
                    name_subdivision = parametr.getString("name_subdivision");
                    uid_invoice = parametr.getString("uid_invoice");
                    num_doc = parametr.getString("num_doc");

                    Date date = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("dd MMM");

                    //SetInvoiceInfo(dateFormat.format(date_invoice), "№ "+num_doc, name_subdivision);
                    break;
                }
            }

        }

        parseBarcode = new ParseBarcode();
        num_term = MainActivity.GetSettings().num_term;

        FloatingActionButton floating_button_down = (FloatingActionButton) findViewById(R.id.floating_button_down);
        floating_button_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDown();
            }
        });



        toolbar_status = (Toolbar) findViewById(R.id.toolbar_status);
        message_toolbar = (TextView) findViewById(R.id.message_toolbar);


        com.getbase.floatingactionbutton.FloatingActionsMenu rightLabels = (FloatingActionsMenu) findViewById(R.id.right_labels_);
        com.getbase.floatingactionbutton.FloatingActionButton addedOnce = new com.getbase.floatingactionbutton.FloatingActionButton(this);
        addedOnce.setTitle("Код");
        rightLabels.addButton(addedOnce);

        addedOnce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_invoiceItem.SortingItemByCode();
                NotifyDataSetChangedReciclerView();
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton addedTwice = new com.getbase.floatingactionbutton.FloatingActionButton(this);
        addedTwice.setTitle("Название");
        rightLabels.addButton(addedTwice);
        rightLabels.removeButton(addedTwice);
        rightLabels.addButton(addedTwice);

        addedTwice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_invoiceItem.SortingItemByName();
                NotifyDataSetChangedReciclerView();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        switch (type_view)
        {
            case not_group:
            {
                GetItemsOfInvoice();
                break;
            }
            case group:
            {
                GetItemsOfInvoiceGroup();
                break;
            }
        }

        utils_wifi = new TUtilsWiFi(getApplicationContext());
        utils_wifi.StatrMonitoring(new TStatusWiFi() {

            @Override
            public void OnConnected() {
                toolbar_status.setBackgroundColor(Color.BLUE);
                message_toolbar.setText("WiFi Connected");
            }

            @Override
            public void OnLost() {
                toolbar_status.setBackgroundColor(Color.RED);
                message_toolbar.setText("WiFi Lost");
            }
        });

    }

    private void InitToolbar()
    {
        toolbar = findViewById(R.id.toolbar_items_of_invoice);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();// возврат на предыдущий activity
            }
        });

        caption_toolbar = findViewById(R.id.caption_textview);
        date_toolbar = findViewById(R.id.date_textview);
        number_invoice_toolbar = findViewById(R.id.number_textview);
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
        switch (type_invoice)
        {
            case invoice_external:
            {
                //GetTransferInvoiceGestori();
                break;
            }
            case invoice_1c:
            {
                GetTransferInvoiceGestori_1C();
                break;
            }
        }
    }

    private void GetItemsInvoiceGroup()
    {
        switch (type_invoice)
        {
            case invoice_external:
            {
                //GetTransferInvoiceGestori_Group();
                break;
            }
            case invoice_1c:
            {
                GetTransferInvoiceGestori_1C_Group();
                break;
            }

        }
    }

    private void GetItemsOfInvoiceGroup()
    {
        MainActivity.GetRestClient().GetItemsOfInvoiceGroup(current_date, type_invoice, new TOnResponce<ListInvoiceItem>() {
            @Override
            public void OnSuccess(TResponce<ListInvoiceItem> responce) {

                if(!responce.Success)
                {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }

                list_invoiceItem = responce.Data_;
                list_invoiceItem.SortingItem();

                InitRecyclerView();
            }

            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении списка товаров!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void GetTransferInvoiceGestori_1C_Group()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String date_str = dateFormat.format(current_date);

        MainActivity.GetRestClient().GetItemsOfInvoice1CGroup(date_str, new TOnResponce<ListInvoiceItem>() {
            @Override
            public void OnSuccess(TResponce<ListInvoiceItem> responce) {

                if(!responce.Success)
                {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }

                list_invoiceItem = responce.Data_;
                list_invoiceItem.SortingItem();

                InitRecyclerView();
            }

            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении списка товаров в накладной!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void GetTransferInvoiceGestori_1C()
    {
        MainActivity.GetRestClient().GetItemsOfInvoice1C(uid_invoice, new TOnResponce<ListInvoiceItem>() {
            @Override
            public void OnSuccess(TResponce<ListInvoiceItem> responce) {

                if(!responce.Success)
                {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }

                list_invoiceItem = responce.Data_;
                list_invoiceItem.SortingItem();

                InitRecyclerView();
            }

            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении списка товаров в накладной!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetItemsOfInvoice()
    {
        MainActivity.GetRestClient().GetItemsOfInvoice(uid_invoice, type_invoice, new TOnResponce<ListInvoiceItem>() {
            @Override
            public void OnSuccess(TResponce<ListInvoiceItem> responce) {

                if(!responce.Success)
                {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }

                list_invoiceItem = responce.Data_;
                list_invoiceItem.SortingItem();

                InitRecyclerView();
            }

            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении списка товаров в накладной!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(type_view == TypeView.group)
        {
            getMenuInflater().inflate(R.menu.menu_items_of_invoice_1c, menu);
            return true;
        }
        switch (type_invoice)
        {
            case invoice_external: {
                getMenuInflater().inflate(R.menu.menu_items_of_invoice, menu);
                break;
            }
            /*
            case invoice_external:{
                getMenuInflater().inflate(R.menu.menu_items_purchase, menu);
                break;
            }
            */

            case invoice_1c:
            {
                getMenuInflater().inflate(R.menu.menu_items_of_invoice_1c, menu);
                break;
            }
            default:{
                return true;
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
            case R.id.close_purchase:
                ClosePurchaseInvoice();
                return true;
            case R.id.scan:
                ScanItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void CloseInvoice_()
    {
        switch (type_invoice)
        {
            case invoice_external: {
                CloseInvoice();
                break;
            }
            case invoice_1c: {
                CloseInvoice1C();
                break;
            }
        }
    }
    private void ShowAlert(String title, String message, boolean show_cancel)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        alert_dialog = builder.create();
        alert_dialog.setCancelable(show_cancel);
        alert_dialog.show();
    }
    private void HideAlert()
    {
        alert_dialog.hide();
    }
    private void CloseInvoice()
    {

        ShowAlert("Закрытие накладной", "Идет передача данных...", false);

        MainActivity.GetRestClient().CloseInvoice(uid_invoice, type_invoice, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
                GetItemsInvoice();
                HideAlert();
            }

            @Override
            public void OnFailure(Throwable t) {

                Toast.makeText(getApplicationContext(), "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
                ShowAlert("Ошибка! ", t.getMessage(), true);

            }

        });
    }
    private void CloseInvoice1C()
    {
        if(type_view == TypeView.group)
        {
            Toast.makeText(getApplicationContext(), "Ошибка! Функционал пока не реализован. ", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Закрытие накладной");
        builder.setMessage("Идет передача данных в 1C");
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        MainActivity.GetRestClient().CloseInvoice1C(uid_invoice, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
                GetItemsInvoice();
                dialog.hide();
            }

            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();

                dialog.setMessage("Ошибка!  " + t.getMessage());
                dialog.setCancelable(true);
            }

        });
    }
    private void ClosePurchaseInvoice()
    {
        MainActivity.GetRestClient().ClosePurchaseInvoice(uid_invoice, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void PrintInvoice()
    {
        switch (type_invoice)
        {
            case invoice_external: {
                PrintInvoiceGestori();
                break;
            }

            case invoice_1c:
            {
                PrintInvoiceGestori1C();
                break;
            }
        }
    }
    private void PrintInvoiceGestori()
    {
        if(!list_invoiceItem.CheckAllItemSynchronized())
        {
            Toast.makeText(getApplicationContext(), "Ошибка! Не все данные отправлены в GESTORI.  ", Toast.LENGTH_SHORT).show();
            AlertSound();
            return;
        }

        MainActivity.GetRestClient().PrintInvoice(uid_invoice, num_term, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void PrintInvoiceGestori1C()
    {

    }
    private void EventDeleteItemFromInvoice(InvoiceItem item, int position)
    {
        dialog_question.Show(item.GetBarcode() + " " + item.GetName(), new TCallBackDialog() {
            @Override
            public void OnSuccess(boolean flag) {
                if (flag)
                {
                    MainActivity.GetRestClient().DeleteItemFromInvoice(uid_invoice, item.GetUid(), item.GetBarcode(), new TOnResponce() {
                        @Override
                        public void OnSuccess(TResponce responce) {
                            Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void OnFailure(Throwable t) {
                            Toast.makeText(getApplicationContext(), "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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

    private void ScanItem()
    {
        Intent intent = new Intent(this, ScanerActivity.class);
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
        selected_position = ListGoodsRecyclerView.getChildLayoutPosition(view);

        InvoiceItem item = list_invoiceItem.GetItems(selected_position);

        SelectItem(item);
        NotifyDataSetChangedReciclerView();

        String name = item.GetName();
        double quant = item.GetQuantity();
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
                PrintLabel((int)quantity,list_invoiceItem.GetItems(selected_position));
                break;
            }
            case change:{
                InvoiceItem item = list_invoiceItem.GetItems(selected_position);
                item.SetQuantity(quantity);
                switch (type_invoice)
                {
                    case invoice_1c:
                    {
                        ChangeQuantityOnServer1С(item);
                        break;
                    }
                    default:
                    {
                        ChangeQuantityOnServer(item);
                        break;
                    }
                }

                MovePositionToLastPlace(item);
                NotifyDataSetChangedReciclerView();
                ScrolToSelectPosition();
                break;
            }
        }
    }

    private void PrintLabel(int count_label, InvoiceItem invoiceItem)
    {
        MainActivity.GetRestClient().PrintLabel(uid_invoice, invoiceItem.GetUid(), count_label, num_term, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void ShowMessage(String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Alert(message);
            }
        });
    }

    @Override
    public void OnReadCode(final String code)
    {
        switch (type_invoice)
        {
            case invoice_1c:
            {
                InvoiceItem item = list_invoiceItem.GetItemsByBarcode(code);
                if(item == null)
                {
                    ShowMessage("Не нашел товар!");
                    return;
                }
                float quantity = item.GetQuantity() + item.GetBox(code).quantity_in_box;
                item.SetQuantity(quantity);

                SelectItem(item);
                MovePositionToLastPlace(item);

                runOnUiThread(() -> {
                    NotifyDataSetChangedReciclerView();
                    ScrolToSelectPosition();
                });

                ChangeQuantityOnServer1С(item);
                break;
            }
            case invoice_external:
            {
                QRCode qr_code = parseBarcode.ParseJSON(code);
                if (qr_code == null)
                {
                    ShowMessage("Не удалось прочитать QRCode!");
                    return;
                }
                InvoiceItem item = list_invoiceItem.GetItemsByBarcode(qr_code.code);
                if(item == null)
                {
                    ShowMessage("Не нашел товар!");
                    return;
                }

                CheckItemByInvoice(item, qr_code.uid_unique, new TOnResultCheckItemInvoice() {
                    @Override
                    public void OnSuccess() {

                        item.SetQuantity(item.GetRequiredQuantity());
                        SelectItem(item);
                        MovePositionToLastPlace(item);

                        runOnUiThread(() -> {
                            NotifyDataSetChangedReciclerView();
                            ScrolToSelectPosition();
                        });

                        ChangeQuantityOnServer(item);

                    }

                    @Override
                    public void OnFailure() {
                        //Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
                        Alert("Товар не принадлежит накладной!");
                        AlertSound();
                    }
                });

                break;
            }
            /*
            case transfer:
            {
                QRCode qr_code = parseBarcode.ParseJSON(code);
                if (qr_code == null)
                {
                    ShowMessage("Не удалось прочитать QRCode!");
                    return;
                }
                InvoiceItem item = list_invoiceItem.GetItemsByBarcode(qr_code.code);
                if(item == null)
                {
                    ShowMessage("Не нашел товар!");
                    return;
                }
                item.SetQuantity(item.GetRequiredQuantity());
                SelectItem(item);
                MovePositionToLastPlace(item);

                runOnUiThread(() -> {
                    NotifyDataSetChangedReciclerView();
                    ScrolToSelectPosition();
                });

                ChangeQuantityAndUniqueUidOnServer(item, qr_code.uid_unique);
                break;
            }

             */
        }

    }

    private void ScrolToSelectPosition()
    {
        int pos = list_invoiceItem.GetSelectedPosition();
        //linearLayoutManager.scrollToPosition(pos);
        linearLayoutManager.scrollToPositionWithOffset(pos, 150);
    }

    private void ScrolToLastPosition()
    {
        int pos = list_invoiceItem.GetLastPosition();
        linearLayoutManager.scrollToPositionWithOffset(pos, 150);
    }
    private void MovePositionToLastPlace(InvoiceItem item)
    {
        list_invoiceItem.MovePositionToLastPlace(item);
    }
    private void SelectItem(InvoiceItem item)
    {
        list_invoiceItem.SelectItem(item);
    }

    private void NotifyDataSetChangedReciclerView()
    {
        listGoodsAdapter.notifyDataSetChanged();
    }


    private void CheckItemByInvoice(InvoiceItem item, String uid_unique, TOnResultCheckItemInvoice resultCheckItemInvoice)
    {
        MainActivity.GetRestClient().CheckItemByInvoice(item.GetUidInvoice(), uid_unique, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                if(responce.Success)
                {
                    resultCheckItemInvoice.OnSuccess();
                }
                else
                {
                    resultCheckItemInvoice.OnFailure();
                }

            }
            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void ChangeQuantityOnServer(InvoiceItem item) {

        MainActivity.GetRestClient().SetQuantityItem(item.GetUidInvoice(), item.GetUid(), item.GetQuantity(), item.GetBarcode(), new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void ChangeQuantityAndUniqueUidOnServer(InvoiceItem item, String unique_uid) {

        MainActivity.GetRestClient().SetQuantityAndUniqueUidItem(item.GetUidInvoice(), item.GetUid(), item.GetQuantity(), item.GetBarcode(), unique_uid, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void ChangeQuantityOnServer1С(InvoiceItem item) {

        MainActivity.GetRestClient().SetQuantityItem1C(item.GetUidInvoice(), item.GetUid(), item.GetQuantity(), item.GetBarcode(), new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    private void SynchronizeItems()
    {
        List<InvoiceItem> items = list_invoiceItem.GetItemsNotSynchronized();
        for(InvoiceItem item:items)
        {
            ChangeQuantityOnServer(item.uid, item.barcode, item.quantity);
        }
    }

     */

}
