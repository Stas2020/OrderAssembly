package com.westas.orderassembly.rest_service;

import com.westas.orderassembly.calculator.ListBarcodeTemplate;
import com.westas.orderassembly.invoice.ListBox;
import com.westas.orderassembly.invoice.TypeOperation;
import com.westas.orderassembly.item.Item;
import com.westas.orderassembly.item.ListItem;
import com.westas.orderassembly.invoice.ListInvoice;
import com.westas.orderassembly.invoice.ListBox;
import com.westas.orderassembly.setting.Settings;
import com.westas.orderassembly.subdivision.ListSubdivision;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class RestClient {

    private Retrofit retrofit;
    private RestApi rest_api;
    private Settings setting;

    public RestClient(Settings setting_)
    {
        setting = setting_;
    }

    //TODO:  Создаем Retrofit
    public void BuildRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .build();

        //Читаем настройки
        String NameServer = setting.host;

        assert NameServer != null;
        NameServer = NameServer.replaceAll("\n|\r\n", "");
        NameServer = NameServer.trim();
        String Port = String.valueOf(setting.port);
        Port = Port.trim();
        String url = "http://"+NameServer+":"+Port;
        //NameServer = "192.168.222.111";

        retrofit = new Retrofit.Builder()
                .baseUrl(url) //Базовая часть адреса
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();

        rest_api= retrofit.create(RestApi.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public void GetListInvoiceBySender(Date date, String uid_sender, TypeOperation type_operation, TOnResponce on_responce_) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String date_str = dateFormat.format(date);

        Call<TResponce<ListInvoice>> call_invoice = rest_api.GetListInvoiceBySender(date_str, uid_sender, type_operation);
        call_invoice.enqueue(new Callback<TResponce<ListInvoice>>() {
            @Override
            public void onResponse(Call<TResponce<ListInvoice>> call, Response<TResponce<ListInvoice>> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce<ListInvoice>> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }

    public void GetListOpenInvoiceBySender(String uid_sender, TypeOperation type_operation, TOnResponce on_responce_) {

        Call<TResponce<ListInvoice>> call_invoice = rest_api.GetListOpenInvoiceBySender(uid_sender, type_operation);
        call_invoice.enqueue(new Callback<TResponce<ListInvoice>>() {
            @Override
            public void onResponse(Call<TResponce<ListInvoice>> call, Response<TResponce<ListInvoice>> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce<ListInvoice>> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }

    public void GetListInvoiceByReceiver(Date date, String uid_receiver, TypeOperation type_operation, TOnResponce on_responce_) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String date_str = dateFormat.format(date);

        Call<TResponce<ListInvoice>> call_invoice = rest_api.GetListInvoiceByReceiver(date_str, uid_receiver, type_operation);
        call_invoice.enqueue(new Callback<TResponce<ListInvoice>>() {
            @Override
            public void onResponse(Call<TResponce<ListInvoice>> call, Response<TResponce<ListInvoice>> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce<ListInvoice>> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }

    public void GetListBoxesBySender(Date date, String uid_sender, TypeOperation type_operation, TOnResponce on_responce_) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String date_str = dateFormat.format(date);

        Call<TResponce<ListBox>> call_invoice = rest_api.GetListBoxesBySender(date_str, uid_sender, type_operation);
        call_invoice.enqueue(new Callback<TResponce<ListBox>>() {
            @Override
            public void onResponse(Call<TResponce<ListBox>> call, Response<TResponce<ListBox>> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce<ListBox>> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }

    public void GetListBarcodeTemplate(TOnResponce on_responce_) {
        Call<TResponce<ListBarcodeTemplate>> call_invoice = rest_api.GetListBarcodeTemplate();
        call_invoice.enqueue(new Callback<TResponce<ListBarcodeTemplate>>() {
            @Override
            public void onResponse(Call<TResponce<ListBarcodeTemplate>> call, Response<TResponce<ListBarcodeTemplate>> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce<ListBarcodeTemplate>> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }

    //Закрытие накладной
    public void CloseInvoice(String uid_sender, String uid_invoice, TypeOperation type_operation, TOnResponce on_responce_) {
        try {
            Call<TResponce> close_invoice = rest_api.CloseInvoice(uid_sender, uid_invoice, type_operation);

            close_invoice.enqueue(new Callback<TResponce>() {
                @Override
                public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                    if (response.code() == 200) {
                        on_responce_.OnSuccess(response.body());
                    }
                    else {
                        on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                    }

                }
                @Override
                public void onFailure(Call<TResponce> call, Throwable t) {
                    on_responce_.OnFailure(t.getMessage());
                }
            });
        }
        catch(Exception e) {
            on_responce_.OnFailure(e.getMessage());
        }
    }


    //TODO:  Получение, Подразделения
    public void GetListSubdivision(TOnResponce on_responce_) {
        Call<TResponce<ListSubdivision>> list_subdivision = rest_api.GetListSubdivision();
        list_subdivision.enqueue(new Callback<TResponce<ListSubdivision>>() {
            @Override
            public void onResponse(Call<TResponce<ListSubdivision>> call, Response<TResponce<ListSubdivision>> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce<ListSubdivision>> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }


    //TODO:  Получение, список накладных для приемки на ресторане
    public void GetInvoicesFromServer(Date date, String uid_sender, TypeOperation type_operation, TOnResponce on_responce_) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String date_str = dateFormat.format(date);

        Call<TResponce> accept_invoices = rest_api.GetInvoicesFromServer(date_str, uid_sender, type_operation);
        accept_invoices.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }

    //TODO:  Замена количества товара в накладной
    public void SetQuantityItem(TypeOperation type_operation, String uid_invoice,  String uid_item,  float quantity, String barcode_item, TOnResponce on_responce_) {
        Call<TResponce> set_quantity_item = rest_api.SetQuantityItem(type_operation, uid_invoice,uid_item,quantity,barcode_item);
        set_quantity_item.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }

    //TODO:  Замена количества товара в накладной и присвоить уникальный GUID
    public void SetQuantityAndUniqueUidItem( String uid_invoice,  String uid_item,  float quantity, String barcode_item, String unique_uid_item, TOnResponce on_responce_) {
        Call<TResponce> set_quantity_item = rest_api.SetQuantityAndUniqueUidItem(uid_invoice,uid_item,quantity,barcode_item, unique_uid_item);
        set_quantity_item.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }

    //TODO:  Добавление товара в накладную
    public void AddItemToInvoice(String uid_invoice, String barcode_item, TOnResponce on_responce_) {
        Call<TResponce> add_item = rest_api.AddItemToInvoice(uid_invoice,  barcode_item);
        add_item.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }

    //Удаление товара из накладной
    public void DeleteItemFromInvoice(String uid_invoice, String uid_item , String barcode_item, TOnResponce on_responce_) {
        Call<TResponce> delete_item = rest_api.DeleteItemFromInvoice(uid_invoice,  uid_item, barcode_item);
        delete_item.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }

    //Проверка, существует ли баркод
    public void CheckItem(String barcode_item, TOnResponce on_responce_) {
        Call<TResponce<Item>> cheked_item = rest_api.CheckItem(barcode_item);
        cheked_item.enqueue(new Callback<TResponce<Item>>() {
            @Override
            public void onResponse(Call<TResponce<Item>> call, Response<TResponce<Item>> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce<Item>> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }

    //Проверка, принадлежит товар накладной
    public void CheckItemByInvoice (String uid_invoice, String uid_unique_item, TOnResponce on_responce_) {
        Call<TResponce> cheked_item = rest_api.CheckItemByInvoice(uid_invoice,uid_unique_item);
        cheked_item.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                if (response.code() == 200) {
                    on_responce_.OnSuccess(response.body());
                }
                else {
                    on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {
                on_responce_.OnFailure(t.getMessage());
            }
        });
    }

    //список товаров в накладной
    public void GetItemsOfInvoice(String uid_invoice, TypeOperation type_operation, TOnResponce on_responce_) {
        try {
            Call<TResponce<ListItem>> invoice_items = rest_api.GetItemsOfInvoice(uid_invoice, type_operation);

            invoice_items.enqueue(new Callback<TResponce<ListItem>>() {
                @Override
                public void onResponse(Call<TResponce<ListItem>> call, Response<TResponce<ListItem>> response) {
                    if (response.code() == 200) {
                        on_responce_.OnSuccess(response.body());
                    }
                    else {
                        on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                    }
                }
                @Override
                public void onFailure(Call<TResponce<ListItem>> call, Throwable t) {
                    on_responce_.OnFailure(t.getMessage());
                }
            });
        }
        catch(Exception e)
        {
            on_responce_.OnFailure(e.getMessage());
        }
    }

    //список товаров в накладной
    public void GetItemsOfBox(String uid_box, TypeOperation type_operation, TOnResponce on_responce_) {
        try {
            Call<TResponce<ListItem>> invoice_items = rest_api.GetItemsOfBox(uid_box, type_operation);

            invoice_items.enqueue(new Callback<TResponce<ListItem>>() {
                @Override
                public void onResponse(Call<TResponce<ListItem>> call, Response<TResponce<ListItem>> response) {
                    if (response.code() == 200) {
                        on_responce_.OnSuccess(response.body());
                    }
                    else {
                        on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                    }
                }
                @Override
                public void onFailure(Call<TResponce<ListItem>> call, Throwable t) {
                    on_responce_.OnFailure(t.getMessage());
                }
            });
        }
        catch(Exception e)
        {
            on_responce_.OnFailure(e.getMessage());
        }
    }
    //список товаров в сгрупированных накладных
    public void GetItemsOfInvoiceGroup(Date date, String uid_sender, TypeOperation type_operation, TOnResponce on_responce_) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
            String date_str = dateFormat.format(date);

            Call<TResponce<ListItem>> invoice_items = rest_api.GetItemsOfInvoiceGroup(date_str, uid_sender, type_operation);

            invoice_items.enqueue(new Callback<TResponce<ListItem>>() {
                @Override
                public void onResponse(Call<TResponce<ListItem>> call, Response<TResponce<ListItem>> response) {
                    if (response.code() == 200) {
                        on_responce_.OnSuccess(response.body());
                    }
                    else {
                        on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                    }
                }
                @Override
                public void onFailure(Call<TResponce<ListItem>> call, Throwable t) {
                    on_responce_.OnFailure(t.getMessage());
                }
            });
        }
        catch(Exception e)
        {
            on_responce_.OnFailure(e.getMessage());
        }
    }

    //Печать накладной
    public void PrintInvoice(String uid_invoice, int num_term , TOnResponce on_responce_) {
        try {
            Call<TResponce> print = rest_api.PrintInvoice(uid_invoice, num_term);

            print.enqueue(new Callback<TResponce>() {
                @Override
                public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                    if (response.code() == 200) {
                        on_responce_.OnSuccess(response.body());
                    }
                    else {
                        on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                    }
                }
                @Override
                public void onFailure(Call<TResponce> call, Throwable t) {
                    on_responce_.OnFailure(t.getMessage());
                }
            });
        }
        catch(Exception e) {
            on_responce_.OnFailure(e.getMessage());
        }
    }

    //Печать этикетки
    public void PrintLabel(String uid_invoice, String uid_item, int count_label, int num_term, TOnResponce on_responce_) {
        try
        {
            Call<TResponce> print_label = rest_api.PrintLabel(uid_invoice, uid_item, count_label, num_term);

            print_label.enqueue(new Callback<TResponce>() {
                @Override
                public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                    if (response.code() == 200) {
                        on_responce_.OnSuccess(response.body());
                    }
                    else {
                        on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                    }
                }
                @Override
                public void onFailure(Call<TResponce> call, Throwable t) {
                    on_responce_.OnFailure(t.getMessage());
                }
            });
        }
        catch(Exception e) {
            on_responce_.OnFailure(e.getMessage());
        }
    }

    //Результат синхронизации с GESTORI
    public void GetResultSynchronizedInvoice(String uid_invoice, TOnResponce on_responce_) {
        try {
            Call<TResponce> result_sync_invoice = rest_api.GetResultSynchronizedInvoice(uid_invoice);

            result_sync_invoice.enqueue(new Callback<TResponce>() {
                @Override
                public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                    if (response.code() == 200) {
                        on_responce_.OnSuccess(response.body());
                    }
                    else {
                        on_responce_.OnFailure( Integer.toString(response.code()) + " " +response.message());
                    }
                }
                @Override
                public void onFailure(Call<TResponce> call, Throwable t) {
                    on_responce_.OnFailure(t.getMessage());
                }
            });
        }
        catch(Exception e) {
            on_responce_.OnFailure(e.getMessage());
        }
    }
}
