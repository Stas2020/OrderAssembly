package com.westas.orderassembly.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.invoice.InvoiceClaimActivity;
import com.westas.orderassembly.scaner.ScanerActivity;
import com.westas.orderassembly.wifi.TStatusWiFi;
import com.westas.orderassembly.wifi.TUtilsWiFi;
import com.westas.orderassembly.barcode_reader.TOnReadBarcode;
import com.westas.orderassembly.calculator.ListBarcodeTemplate;
import com.westas.orderassembly.calculator.ParseBarcode;
import com.westas.orderassembly.calculator.QRCode;
import com.westas.orderassembly.dialog.TCallBackDialog;
import com.westas.orderassembly.dialog.TCallBackDialogQuantity;
import com.westas.orderassembly.dialog.TDialogForm;
import com.westas.orderassembly.dialog.TDialogQuestion;
import com.westas.orderassembly.dialog.TTypeForm;
import com.westas.orderassembly.invoice.CloseInvoiceActivity;
import com.westas.orderassembly.invoice.TypeOperation;
import com.westas.orderassembly.menu_helper.MenuHelper;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemsActivity extends AppCompatActivity implements  View.OnClickListener, View.OnLongClickListener, TOnReadBarcode, TCallBackDialogQuantity
{

    private RecyclerView ListGoodsRecyclerView;
    private ItemsAdapter listGoodsAdapter;
    private ListItem listItem;
    private LinearLayoutManager linearLayoutManager;

    private TextView caption_toolbar;
    private TextView date_toolbar;
    private TextView number_invoice_toolbar;

    private TypeView type_view;
    private TypeOperation type_operation;
    private Date current_date;
    private Date date_invoice;
    private String uid_invoice;
    private String caption;
    private String num_doc;
    private String uid_sender;
    private String uid_box;
    private boolean show_box;
    private boolean closed_invoice;
    private String claim;

    private int selected_position;
    int num_term;

    private ParseBarcode parseBarcode;
    private ListBarcodeTemplate list_barcode_template;

    private TDialogForm dialog_quantity;
    private TDialogForm dialog_print_label;
    private TDialogQuestion dialog_question;

    TUtilsWiFi utils_wifi;
    private Toolbar toolbar_status;
    private TextView message_toolbar;

    private AlertDialog alert_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        InitToolbar();
        GetListBarcodeTemplate();
        MainActivity.GetBarcodeReader().SetListener(this);

        dialog_quantity = new TDialogForm(ItemsActivity.this, ItemsActivity.this,"Количество", TTypeForm.change);
        dialog_print_label = new TDialogForm(ItemsActivity.this, ItemsActivity.this,"Печать этикетки",TTypeForm.label);
        dialog_question = new TDialogQuestion(this,"Удалить?");

        Bundle parametr = getIntent().getExtras();
        if(parametr!=null){
            type_view = (TypeView) parametr.get("type_view");
            type_operation = (TypeOperation)parametr.get("type_operation");
            current_date = (Date)parametr.get("current_date");
            date_invoice = (Date)parametr.get("date_invoice");
            uid_invoice = parametr.getString("uid_invoice");
            Date date_order = (Date) parametr.get("date_order");
            caption = parametr.getString("caption");
            num_doc = parametr.getString("num_doc");
            uid_sender = parametr.getString("uid_sender");
            uid_box = parametr.getString("uid_box");
            show_box = parametr.getBoolean("show_box");
            closed_invoice = parametr.getBoolean("closed_invoice", false);
        }

        parseBarcode = new ParseBarcode();
        num_term = MainActivity.GetSettings().num_term;


        com.getbase.floatingactionbutton.FloatingActionsMenu rightLabels = (FloatingActionsMenu) findViewById(R.id.right_labels_);
        com.getbase.floatingactionbutton.FloatingActionButton addedOnce = new com.getbase.floatingactionbutton.FloatingActionButton(this);
        addedOnce.setTitle("Код");
        rightLabels.addButton(addedOnce);

        addedOnce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItem.SortingItemByCode();
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
                listItem.SortingItemByName();
                NotifyDataSetChangedReciclerView();
            }
        });

        toolbar_status = findViewById(R.id.toolbar_status);
        message_toolbar = findViewById(R.id.message_toolbar);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onResume() {
        super.onResume();

        switch (type_view) {
            case not_group: {
                caption_toolbar.setText(caption);

                number_invoice_toolbar.setText("№ "+num_doc);
                //toolbar.setBackgroundColor(R.color.items_nogroup);
                if(show_box){
                    GetItemsOfBox();
                    date_toolbar.setText(new SimpleDateFormat("dd.MM.yy").format(current_date));
                }
                else{
                    GetItemsOfInvoice();
                    date_toolbar.setText(new SimpleDateFormat("dd.MM.yy").format(date_invoice));
                }

                break;
            }
            case group: {
                caption_toolbar.setText(caption);
                date_toolbar.setText(new SimpleDateFormat("dd.MM.yy").format(current_date));
                number_invoice_toolbar.setText("ВСЕ ПОЗИЦИИ");
                //toolbar.setBackgroundColor(R.color.items_group);
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


    private void InitToolbar() {
        caption_toolbar = findViewById(R.id.caption_textview);
        date_toolbar = findViewById(R.id.date_textview);
        number_invoice_toolbar = findViewById(R.id.number_textview);

        Toolbar toolbar = findViewById(R.id.toolbar_items_of_invoice);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();// возврат на предыдущий activity
        });
    }

    private void PageDown() {
        int count_item = listGoodsAdapter.getItemCount();
        ListGoodsRecyclerView.smoothScrollToPosition(count_item);
        Toast.makeText(getApplicationContext(), "Page down!  ", Toast.LENGTH_SHORT).show();
    }

    private void PageUp() {
        ListGoodsRecyclerView.smoothScrollToPosition(0);
        Toast.makeText(getApplicationContext(), "Page up!  ", Toast.LENGTH_SHORT).show();
    }

    private void GetItemsOfInvoice() {
        MainActivity.GetRestClient().GetItemsOfInvoice(uid_invoice, type_operation, new TOnResponce<ListItem>() {
            @Override
            public void OnSuccess(TResponce<ListItem> responce) {

                if(!responce.Success) {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }
                listItem = responce.Data_;
                listItem.SortingItem();

                InitRecyclerView();
            }

            @Override
            public void OnFailure(String message) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении списка товаров в накладной!  " + message, Toast.LENGTH_SHORT).show();
            }
        });
        // текст претензии
        MainActivity.GetRestClient().GetInfoClaim(uid_invoice, type_operation, new TOnResponce(){
            @Override
            public void OnSuccess(TResponce responce) {
                claim="";
            }

            @Override
            public void OnFailure(String message) {
                claim="";
            }
        });
    }
    private void GetItemsOfBox() {
        MainActivity.GetRestClient().GetItemsOfBox(uid_box, type_operation, new TOnResponce<ListItem>() {
            @Override
            public void OnSuccess(TResponce<ListItem> responce) {

                if(!responce.Success) {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }
                listItem = responce.Data_;
                listItem.SortingItem();

                InitRecyclerView();
            }

            @Override
            public void OnFailure(String message) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении списка товаров в накладной!  " + message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void GetItemsOfInvoiceGroup() {
        MainActivity.GetRestClient().GetItemsOfInvoiceGroup(current_date, uid_sender, type_operation, new TOnResponce<ListItem>() {
            @Override
            public void OnSuccess(TResponce<ListItem> responce) {

                if(!responce.Success) {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }

                listItem = responce.Data_;
                listItem.SortingItem();

                InitRecyclerView();
            }

            @Override
            public void OnFailure(String message) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении списка товаров!  " + message, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        switch (type_operation){
            case _assembly:{
                SetMenuForAssembly(menu);
                break;
            }
            case _accept:{
                if(!show_box)
                    SetMenuForReceive(menu);
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
    void SetMenuForAssembly(Menu menu){

        MenuHelper menu_helper = new MenuHelper(menu);

        menu_helper.Add("Печать", getDrawable(R.drawable.print_24), item -> {
            PrintInvoice();
            return true;
        });

        menu_helper.Add("Закрыть", getDrawable(R.drawable.closed_24), item -> {
            CloseInvoice();
            return true;
        });

        menu_helper.Add("Добавить", getDrawable(R.drawable.notepad_24), item -> {
            AddItemToInvoice();
            return true;
        });
    }

    void SetMenuForReceive(Menu menu){
        MenuHelper menu_helper = new MenuHelper(menu);

        menu_helper.Add("Печать", getDrawable(R.drawable.print_24), item -> {
            PrintInvoice();
            return true;
        });

//        if(closed_invoice)
            menu_helper.Add("Претензия", getDrawable(R.drawable.closed_24), item -> {
                ShowInvoiceClaim();
                return true;
            });
//        else
//            menu_helper.Add("Закрыть", getDrawable(R.drawable.closed_24), item -> {
//                CloseInvoice();
//                return true;
//            });

        menu_helper.Add("Сканировать", getDrawable(R.drawable.closed_24), item -> {
            ScanItem();
            return true;
        });

    }

    private void InitRecyclerView() {
        ListGoodsRecyclerView = findViewById(R.id.list_items_invoice);
        linearLayoutManager = new LinearLayoutManager(this);
        ListGoodsRecyclerView.setLayoutManager(linearLayoutManager);

        ListGoodsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        listGoodsAdapter = new ItemsAdapter(this, listItem, this,this);
        ListGoodsRecyclerView.setAdapter(listGoodsAdapter);

        ListGoodsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 0;
                outRect.top = 0;
                super.getItemOffsets(outRect, view, parent, state);
            }
        });

        registerForContextMenu(ListGoodsRecyclerView);

        //RecyclerTouchListener touchListener = new RecyclerTouchListener(ListGoodsRecyclerView);
        //ListGoodsRecyclerView.addOnItemTouchListener(touchListener);

/*
        SwipeCallback swipe_callback = new SwipeCallback()
        {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();

                Item item = listGoodsAdapter.GetItem(position);
                //listGoodsAdapter.removeItem(position);
                //EventDeleteItemFromInvoice(item, position);
            }
        };*/

        //ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipe_callback);
       // itemTouchhelper.attachToRecyclerView(ListGoodsRecyclerView);


    }

    private void ShowAlert(String title, String message, boolean show_cancel) {
        if(alert_dialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(message);
            alert_dialog = builder.create();
        }
        else{
            alert_dialog.setTitle(title);
            alert_dialog.setMessage(message);
        }
        alert_dialog.setCancelable(show_cancel);
        alert_dialog.show();


    }
    private void HideAlert() {
        alert_dialog.hide();
    }

    private void CloseInvoice(){
        Intent intent = new Intent(this, CloseInvoiceActivity.class);
        intent.putExtra("caption", caption);
        intent.putExtra("date_text", date_toolbar.getText().toString());
        intent.putExtra("num_doc", num_doc);
        intent.putExtra("uid_sender", uid_sender);
        intent.putExtra("uid_invoice", uid_invoice);
        intent.putExtra("type_operation", type_operation);
        startActivity(intent);
    }

    private void PrintInvoice() {

        MainActivity.GetRestClient().PrintInvoice(uid_invoice, num_term, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFailure(String message) {
                Toast.makeText(getApplicationContext(), "Ошибка!  " + message, Toast.LENGTH_SHORT).show();
                AlertSound();
            }
        });
    }

    private void EventDeleteItemFromInvoice(Item item, int position) {
        dialog_question.Show(item.GetBarcode() + " " + item.GetName(), new TCallBackDialog() {
            @Override
            public void OnSuccess(boolean flag) {
                if (flag) {
                    MainActivity.GetRestClient().DeleteItemFromInvoice(uid_invoice, item.GetUid(), item.GetBarcode(), new TOnResponce() {
                        @Override
                        public void OnSuccess(TResponce responce) {
                            Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnFailure(String message) {
                            Toast.makeText(getApplicationContext(), "Ошибка!  " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    listGoodsAdapter.restoreItem(item, position);
                }
            }
        });
    }

    private void AddItemToInvoice() {
        Intent intent = new Intent(this, AddItemToInvoiceActivity.class);
        intent.putExtra("uid_invoice",uid_invoice);
        startActivity(intent);
    }

    private void ScanItem() {
        Intent intent = new Intent(this, ScanerActivity.class);
        startActivity(intent);
    }

    private void ShowInvoiceClaim() {
        Intent intent = new Intent(this, InvoiceClaimActivity.class);
        intent.putExtra("caption", caption);
        intent.putExtra("date_text", date_toolbar.getText().toString());
        intent.putExtra("num_doc", num_doc);
        intent.putExtra("uid_invoice",uid_invoice);
        intent.putExtra("type_operation", type_operation);
        startActivity(intent);
    }

    private void InfoByInvoice() {
        Intent intent = new Intent(this, InfoInvoiceActivity.class);
        intent.putExtra("uid_invoice",uid_invoice);
        startActivity(intent);
    }

    //Клик по Item в RecyclerView
    @Override
    public void onClick(View view) {
        selected_position = ListGoodsRecyclerView.getChildLayoutPosition(view);

        Item item = listItem.GetItems(selected_position);

        SelectItem(item);
        NotifyDataSetChangedReciclerView();

        String name = item.GetName();
        double quant = item.GetQuantity();
        dialog_quantity.Show(name,quant);
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = ListGoodsRecyclerView.getChildLayoutPosition(v);
        if(selected_position!=pos){
            selected_position = pos;
            Item item = listItem.GetItems(selected_position);
            SelectItem(item);
            NotifyDataSetChangedReciclerView();
        }
        return false; // false - показать контекстное меню
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuHelper menu_helper = new MenuHelper(menu);
        Item item = listItem.GetItems(selected_position);
        float qty_required = item.GetRequiredQuantity();
        float qty = item.GetQuantity();

        // для непринятых позиций
        if(qty!=qty_required && qty_required>0) {
            menu_helper.Add("Принято полностью", null, menuItem -> {
                item.SetQuantity(qty_required);
                ChangeQuantityOnServer(item);
                MovePositionToLastPlace(item);
                NotifyDataSetChangedReciclerView();
                ScrolToSelectPosition();
                return true;
            });
        }

        menu_helper.Add("Печать", null, menuItem -> {
            dialog_print_label.Show("Количество этикеток",0);
            return true;
        });
    }

    @Override
    public void OnChangeQuantity(float quantity, TTypeForm type_)
    {
        switch (type_) {
            case label: {
                PrintLabel((int)quantity, listItem.GetItems(selected_position));
                break;
            }
            case change:{
                Item item = listItem.GetItems(selected_position);
                item.SetQuantity(quantity);

                ChangeQuantityOnServer(item);

                MovePositionToLastPlace(item);
                NotifyDataSetChangedReciclerView();
                ScrolToSelectPosition();
                break;
            }
        }
    }

    private void PrintLabel(int count_label, Item invoiceItem) {
        MainActivity.GetRestClient().PrintLabel(uid_invoice, invoiceItem.GetUid(), count_label, num_term, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFailure(String message) {
                Toast.makeText(getApplicationContext(), "Ошибка!  " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Alert(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        AlertSound();
    }

    private void AlertSound() {
        Uri notify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notify);
        r.play();
    }

    private void ShowMessage(String message) {
        runOnUiThread(() -> Alert(message));
    }

    @Override
    public void OnReadCode(final String code) {
        switch (type_operation) {
            case _accept: {
                Item item = null;
                if(uid_sender.compareTo("108") != 0) {
                    item = listItem.GetItemsByBarcode(code);
                    if(item == null) {
                        ParseBarcode.WeightAndCode weightAndCode = parseBarcode.ParseBarcodeByWeight(code, list_barcode_template);
                        item = listItem.GetItemsByBarcode(weightAndCode.good_code);
                        if(item == null){
                            ShowMessage("Не нашел товар!");
                            break;
                        }
                        float quantity = item.GetQuantity() + weightAndCode.weight;
                        item.SetQuantity(quantity);
                    }
                    else{
                        float quantity = item.GetQuantity() + item.GetPackage(code).quantity_in_package;
                        item.SetQuantity(quantity);
                    }
                }
                else{
                    QRCode qr_code = parseBarcode.ParseJSON(code);
                    if (qr_code == null) {
                        ShowMessage("Не удалось прочитать QRCode!");
                        break;
                    }
                    item  = listItem.GetItemsByBarcode(qr_code.code);
                    if(item == null) {
                        ShowMessage("Не нашел товар!");
                        break;
                    }
                }

                SelectItem(item);
                MovePositionToLastPlace(item);

                runOnUiThread(() -> {
                    NotifyDataSetChangedReciclerView();
                    ScrolToSelectPosition();
                });

                ChangeQuantityOnServer(item);
                break;
            }

            case _assembly: {

                QRCode qr_code = parseBarcode.ParseJSON(code);
                if (qr_code == null) {
                    ShowMessage("Не удалось прочитать QRCode!");
                    break;
                }
                Item item  = listItem.GetItemsByBarcode(qr_code.code);
                if(item == null) {
                    ShowMessage("Не нашел товар!");
                    break;
                }

                item.SetQuantity(item.GetRequiredQuantity());
                SelectItem(item);
                MovePositionToLastPlace(item);

                runOnUiThread(() -> {
                    NotifyDataSetChangedReciclerView();
                    ScrolToSelectPosition();
                });

                ChangeQuantityOnServer(item);

                break;
            }
        }
    }

    private void GetListBarcodeTemplate() {
        MainActivity.GetRestClient().GetListBarcodeTemplate( new TOnResponce<ListBarcodeTemplate>() {
            @Override
            public void OnSuccess(TResponce<ListBarcodeTemplate> responce) {
                if(!responce.Success) {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    list_barcode_template = responce.Data_;
                }
            }
            @Override
            public void OnFailure(String message) {
                Toast.makeText(getApplicationContext(), "Ошибка. Не удалось получить шаблоны баркодов.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ScrolToSelectPosition() {
        int pos = listItem.GetSelectedPosition();
        //linearLayoutManager.scrollToPosition(pos);
        linearLayoutManager.scrollToPositionWithOffset(pos, 150);
    }

    private void ScrolToLastPosition() {
        int pos = listItem.GetLastPosition();
        linearLayoutManager.scrollToPositionWithOffset(pos, 150);
    }

    private void MovePositionToLastPlace(Item item) {
        listItem.MovePositionToLastPlace(item);
    }

    private void SelectItem(Item item) {
        listItem.SelectItem(item);
    }

    private void NotifyDataSetChangedReciclerView() {
        listGoodsAdapter.notifyDataSetChanged();
    }

    private void CheckItemByInvoice(Item item, String uid_unique, TOnResultCheckItemInvoice resultCheckItemInvoice) {
        MainActivity.GetRestClient().CheckItemByInvoice(item.GetUidInvoice(), uid_unique, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                if(responce.Success) {
                    resultCheckItemInvoice.OnSuccess();
                }
                else {
                    resultCheckItemInvoice.OnFailure();
                }
            }
            @Override
            public void OnFailure(String message) {
                Toast.makeText(getApplicationContext(), "Ошибка!  " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    TOnResponce showMessageOnResponce = new TOnResponce() {
        @Override
        public void OnSuccess(TResponce responce) {
            Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void OnFailure(String message) {
            Toast.makeText(getApplicationContext(), "Ошибка!  " + message, Toast.LENGTH_SHORT).show();
        }
    };

    private void ChangeQuantityOnServer(Item item) {
        if(show_box){
            MainActivity.GetRestClient().SetQuantityItemInBox(type_operation, item.GetUidInvoice(), item.GetUid(),
                    item.GetBoxUid(), item.GetQuantity(), item.GetBarcode(), showMessageOnResponce);
        }
        else{
            MainActivity.GetRestClient().SetQuantityItem(type_operation, item.GetUidInvoice(), item.GetUid(),
                    item.GetQuantity(), item.GetBarcode(), showMessageOnResponce);
        }

    }

    private void ChangeQuantityAndUniqueUidOnServer(Item item, String unique_uid) {
        MainActivity.GetRestClient().SetQuantityAndUniqueUidItem(item.GetUidInvoice(), item.GetUid(),
                item.GetQuantity(), item.GetBarcode(), unique_uid, showMessageOnResponce);
    }

}
