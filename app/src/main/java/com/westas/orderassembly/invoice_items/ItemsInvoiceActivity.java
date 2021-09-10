package com.westas.orderassembly.invoice_items;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;





import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.Scaner.ScanerActivity;
import com.westas.orderassembly.WiFi.TStatusWiFi;
import com.westas.orderassembly.WiFi.TUtilsWiFi;
import com.westas.orderassembly.barcode_reader.TOnReadBarcode;
import com.westas.orderassembly.calculator.ListBarcodeTemplate;
import com.westas.orderassembly.calculator.ParseBarcode;
import com.westas.orderassembly.calculator.QRCode;
import com.westas.orderassembly.dialog.TCallBackDialog;
import com.westas.orderassembly.dialog.TCallBackDialogQuantity;
import com.westas.orderassembly.dialog.TDialogForm;
import com.westas.orderassembly.dialog.TDialogQuestion;
import com.westas.orderassembly.dialog.TTypeForm;
import com.westas.orderassembly.invoice.TypeInvoice;
import com.westas.orderassembly.menu_for_list_invoice.ItemMenu;
import com.westas.orderassembly.menu_for_list_invoice.MenuToolbar;
import com.westas.orderassembly.menu_for_list_invoice.OnSelectItemMenu;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ItemsInvoiceActivity extends AppCompatActivity implements  View.OnClickListener, View.OnLongClickListener, TOnReadBarcode, TCallBackDialogQuantity
{

    private RecyclerView ListGoodsRecyclerView;
    private ItemsInvoiceAdapter listGoodsAdapter;
    private ListInvoiceItem list_item_of_invoice;
    private LinearLayoutManager linearLayoutManager;

    private TextView caption_toolbar;
    private TextView date_toolbar;
    private TextView number_invoice_toolbar;


    private TypeView type_view;
    private TypeInvoice type_invoice;
    private Date current_date;
    private Date date_invoice;
    private String uid_invoice;
    private String caption;
    private String num_doc;
    private String uid_sender;


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
        setContentView(R.layout.activity_items_invoice);

        InitToolbar();
        GetListBarcodeTemplate();
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
            uid_invoice = parametr.getString("uid_invoice");
            Date date_order = (Date) parametr.get("date_order");
            caption = parametr.getString("caption");
            num_doc = parametr.getString("num_doc");
            uid_sender = parametr.getString("uid_sender");
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
                list_item_of_invoice.SortingItemByCode();
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
                list_item_of_invoice.SortingItemByName();
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

        switch (type_view)
        {
            case not_group:
            {
                caption_toolbar.setText(caption);
                date_toolbar.setText(new SimpleDateFormat("dd.MM.yy").format(date_invoice));
                number_invoice_toolbar.setText("№ "+num_doc);
                //toolbar.setBackgroundColor(R.color.items_nogroup);
                GetItemsOfInvoice();
                break;
            }
            case group:
            {
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

    private void InitToolbar()
    {
        caption_toolbar = findViewById(R.id.caption_textview);
        date_toolbar = findViewById(R.id.date_textview);
        number_invoice_toolbar = findViewById(R.id.number_textview);

        Toolbar toolbar = findViewById(R.id.toolbar_items_of_invoice);
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

                list_item_of_invoice = responce.Data_;
                list_item_of_invoice.SortingItem();

                InitRecyclerView();
            }

            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении списка товаров в накладной!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetItemsOfInvoiceGroup()
    {
        MainActivity.GetRestClient().GetItemsOfInvoiceGroup(current_date, uid_sender, type_invoice, new TOnResponce<ListInvoiceItem>() {
            @Override
            public void OnSuccess(TResponce<ListInvoiceItem> responce) {

                if(!responce.Success)
                {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }

                list_item_of_invoice = responce.Data_;
                list_item_of_invoice.SortingItem();

                InitRecyclerView();
            }

            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении списка товаров!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        ItemMenu m_item;
        MenuToolbar menu_toolbar = new MenuToolbar(menu);

        m_item = new ItemMenu();
        m_item.SetCaption("Печать");
        m_item.SetIcon(getDrawable(R.drawable.print_24));
        m_item.SetOnClickListren(new OnSelectItemMenu() {
            @Override
            public void OnSelect() {
                Toast.makeText(getApplicationContext(),  "Печать...", Toast.LENGTH_SHORT).show();
            }
        });
        menu_toolbar.Add(m_item);

        m_item = new ItemMenu();
        m_item.SetCaption("Закрыть");
        m_item.SetIcon(getDrawable(R.drawable.closed_24));
        m_item.SetOnClickListren(new OnSelectItemMenu() {
            @Override
            public void OnSelect() {
                CloseInvoice();
            }
        });
        menu_toolbar.Add(m_item);

        m_item = new ItemMenu();
        m_item.SetCaption("Сканировать");
        m_item.SetIcon(getDrawable(R.drawable.binoculars_24));
        m_item.SetOnClickListren(new OnSelectItemMenu() {
            @Override
            public void OnSelect() {
                ScanItem();
            }
        });
        menu_toolbar.Add(m_item);

        m_item = new ItemMenu();
        m_item.SetCaption("Претензия");
        m_item.SetIcon(getDrawable(R.drawable.notepad_24));
        m_item.SetOnClickListren(new OnSelectItemMenu() {
            @Override
            public void OnSelect() {
                Toast.makeText(getApplicationContext(),  "В разработке...", Toast.LENGTH_SHORT).show();
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

    private void InitRecyclerView()
    {
        ListGoodsRecyclerView = findViewById(R.id.list_items_invoice);
        linearLayoutManager = new LinearLayoutManager(this);
        ListGoodsRecyclerView.setLayoutManager(linearLayoutManager);


        ListGoodsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        listGoodsAdapter = new ItemsInvoiceAdapter(this, list_item_of_invoice, this,this);
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

        MainActivity.GetRestClient().CloseInvoice(uid_sender, uid_invoice, type_invoice, new TOnResponce() {
            @Override
            public void OnSuccess(TResponce responce) {
                Toast.makeText(getApplicationContext(),  responce.Message, Toast.LENGTH_SHORT).show();
                GetItemsOfInvoice();
                HideAlert();
            }

            @Override
            public void OnFailure(Throwable t) {

                Toast.makeText(getApplicationContext(), "Ошибка!  " + t.getMessage(), Toast.LENGTH_SHORT).show();
                ShowAlert("Ошибка! ", t.getMessage(), true);

            }

        });
    }

    private void PrintInvoice()
    {
        if(!list_item_of_invoice.CheckAllItemSynchronized())
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

        InvoiceItem item = list_item_of_invoice.GetItems(selected_position);

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
                PrintLabel((int)quantity,list_item_of_invoice.GetItems(selected_position));
                break;
            }
            case change:{
                InvoiceItem item = list_item_of_invoice.GetItems(selected_position);
                item.SetQuantity(quantity);

                ChangeQuantityOnServer(item);

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
                InvoiceItem item = list_item_of_invoice.GetItemsByBarcode(code);
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

                ChangeQuantityOnServer(item);
                break;
            }
            case invoice_external:
            {
                if(uid_sender.compareTo("112") == 0)
                {
                    InvoiceItem item = list_item_of_invoice.GetItemsByBarcode(code);
                    if(item == null)
                    {
                        ParseBarcode.WeightAndCode weightAndCode = parseBarcode.ParseBarcodeByWeight(code, list_barcode_template);
                        item = list_item_of_invoice.GetItemsByBarcode(weightAndCode.good_code);
                        if(item != null)
                        {
                            float quantity = item.GetQuantity() + weightAndCode.weight;
                            item.SetQuantity(quantity);
                            SelectItem(item);
                            MovePositionToLastPlace(item);
                            runOnUiThread(() -> {
                                NotifyDataSetChangedReciclerView();
                                ScrolToSelectPosition();
                            });
                            ChangeQuantityOnServer(item);
                            return;
                        }
                        {
                            ShowMessage("Не нашел товар!");
                            return;
                        }
                    }
                    Box box = item.GetBox(code);
                    if(box == null)
                    {
                        ParseBarcode.WeightAndCode weightAndCode = parseBarcode.ParseBarcodeByWeight(code, list_barcode_template);
                        float quantity = item.GetQuantity() + weightAndCode.weight;
                        item.SetQuantity(quantity);
                        SelectItem(item);
                        MovePositionToLastPlace(item);
                        runOnUiThread(() -> {
                            NotifyDataSetChangedReciclerView();
                            ScrolToSelectPosition();
                        });
                        ChangeQuantityOnServer(item);
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

                    ChangeQuantityOnServer(item);
                }
                if(uid_sender.compareTo("71") == 0)
                {
                    InvoiceItem item = list_item_of_invoice.GetItemsByBarcode(code);
                    if(item == null)
                    {
                        ShowMessage("Не нашел товар!");
                        return;
                    }

                    Box box = item.GetBox(code);
                    if(box == null)
                    {
                        ShowMessage("Не смог определить количество по баркоду!");
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

                    ChangeQuantityOnServer(item);
                }
                if(uid_sender.compareTo("108") == 0)
                {
                    QRCode qr_code = parseBarcode.ParseJSON(code);
                    if (qr_code == null)
                    {
                        ShowMessage("Не удалось прочитать QRCode!");
                        return;
                    }
                    InvoiceItem item  = list_item_of_invoice.GetItemsByBarcode(qr_code.code);
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
                }
                break;
            }
        }

    }

    private void GetListBarcodeTemplate()
    {
        MainActivity.GetRestClient().GetListBarcodeTemplate( new TOnResponce<ListBarcodeTemplate>() {
            @Override
            public void OnSuccess(TResponce<ListBarcodeTemplate> responce) {
                if(!responce.Success)
                {
                    Toast.makeText(getApplicationContext(), responce.Message, Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    list_barcode_template = responce.Data_;
                }
            }
            @Override
            public void OnFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка. Не удалось получить шаблоны баркодов.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ScrolToSelectPosition()
    {
        int pos = list_item_of_invoice.GetSelectedPosition();
        //linearLayoutManager.scrollToPosition(pos);
        linearLayoutManager.scrollToPositionWithOffset(pos, 150);
    }

    private void ScrolToLastPosition()
    {
        int pos = list_item_of_invoice.GetLastPosition();
        linearLayoutManager.scrollToPositionWithOffset(pos, 150);
    }
    private void MovePositionToLastPlace(InvoiceItem item)
    {
        list_item_of_invoice.MovePositionToLastPlace(item);
    }
    private void SelectItem(InvoiceItem item)
    {
        list_item_of_invoice.SelectItem(item);
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

        MainActivity.GetRestClient().SetQuantityItem(type_invoice, item.GetUidInvoice(), item.GetUid(), item.GetQuantity(), item.GetBarcode(), new TOnResponce() {
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





}
