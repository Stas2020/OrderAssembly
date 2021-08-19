package com.westas.orderassembly.rest_service;

import com.westas.orderassembly.accept_invoice.ListAcceptedInvoice;
import com.westas.orderassembly.calculator.ListBarcodeTemplate;
import com.westas.orderassembly.invoice.TypeInvoice;
import com.westas.orderassembly.invoice_items.InvoiceItem;
import com.westas.orderassembly.invoice_items.ListInvoiceItem;
import com.westas.orderassembly.invoice.ListInvoice;
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
import retrofit2.http.Query;


public class RestClient {

    private Retrofit retrofit;
    private RestApi rest_api;
    private Settings setting;

    public RestClient(Settings setting_)
    {
        setting = setting_;
    }

    //TODO:  Создаем Retrofit
    public void BuildRetrofit()
    {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
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
    public void GetListInvoiceBySender(Date date, String uid_sender, TypeInvoice type_invoice, TOnResponce on_responce_)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String date_str = dateFormat.format(date);

        Call<TResponce<ListInvoice>> call_invoice = rest_api.GetListInvoiceBySender(date_str, uid_sender, type_invoice);
        call_invoice.enqueue(new Callback<TResponce<ListInvoice>>() {
            @Override
            public void onResponse(Call<TResponce<ListInvoice>> call, Response<TResponce<ListInvoice>> response) {
                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce<ListInvoice>> call, Throwable t) {
                on_responce_.OnFailure(t);
            }
        });
    }
    public void GetListBarcodeTemplate(TOnResponce on_responce_)
    {
        Call<TResponce<ListBarcodeTemplate>> call_invoice = rest_api.GetListBarcodeTemplate();
        call_invoice.enqueue(new Callback<TResponce<ListBarcodeTemplate>>() {
            @Override
            public void onResponse(Call<TResponce<ListBarcodeTemplate>> call, Response<TResponce<ListBarcodeTemplate>> response) {
                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce<ListBarcodeTemplate>> call, Throwable t) {
                on_responce_.OnFailure(t);
            }
        });
    }
    //Закрытие накладной
    public void CloseInvoice1C(String uid_invoice, TOnResponce on_responce_)
    {
        try
        {
            Call<TResponce> close_invoice = rest_api.CloseInvoice1C(uid_invoice);

            close_invoice.enqueue(new Callback<TResponce>() {
                @Override
                public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                    on_responce_.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<TResponce> call, Throwable t) {
                    on_responce_.OnFailure(t);
                }
            });
        }
        catch(Exception e)
        {
            on_responce_.OnFailure(e);
        }
    }
    //Закрытие накладной
    public void CloseInvoice(String uid_sender, String uid_invoice, TypeInvoice type_invoice, TOnResponce on_responce_)
    {
        try
        {
            Call<TResponce> close_invoice = rest_api.CloseInvoice(uid_sender, uid_invoice, type_invoice);

            close_invoice.enqueue(new Callback<TResponce>() {
                @Override
                public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                    on_responce_.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<TResponce> call, Throwable t) {
                    on_responce_.OnFailure(t);
                }
            });
        }
        catch(Exception e)
        {
            on_responce_.OnFailure(e);
        }
    }
    public void GetListInvoice1C(Date date, TOnResponce on_responce_)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String dateText = dateFormat.format(date);

        Call<TResponce<ListInvoice>> call_invoice_1c = rest_api.GetInvoices1C(dateText);
        call_invoice_1c.enqueue(new Callback<TResponce<ListInvoice>>() {
            @Override
            public void onResponse(Call<TResponce<ListInvoice>> call, Response<TResponce<ListInvoice>> response) {
                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce<ListInvoice>> call, Throwable t) {
                on_responce_.OnFailure(t);
            }
        });
    }

    //TODO:  Замена количества товара в накладной
    public void SetQuantityItem1C( String uid_invoice,  String uid_item,  float quantity, String barcode_item, TOnResponce on_responce_)
    {
        Call<TResponce> set_quantity_item = rest_api.SetQuantityItem1C(uid_invoice,uid_item,quantity,barcode_item);
        set_quantity_item.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {

                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {

                on_responce_.OnFailure(t);
            }
        });
    }
    //TODO:  Получение, Подразделения
    public void GetListSubdivision(TOnResponce on_responce_)
    {
        Call<TResponce<ListSubdivision>> list_subdivision = rest_api.GetListSubdivision();
        list_subdivision.enqueue(new Callback<TResponce<ListSubdivision>>() {
            @Override
            public void onResponse(Call<TResponce<ListSubdivision>> call, Response<TResponce<ListSubdivision>> response) {
                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce<ListSubdivision>> call, Throwable t) {
                on_responce_.OnFailure(t);
            }
        });
    }

    //TODO:  Получение, список накладных
    public void GetInvoice(String uid_customer, TOnResponce on_responce_)
    {
            Call<TResponce<ListInvoice>> transfer_invoices = rest_api.GetListTransferInvoice(uid_customer);
            transfer_invoices.enqueue(new Callback<TResponce<ListInvoice>>() {
                @Override
                public void onResponse(Call<TResponce<ListInvoice>> call, Response<TResponce<ListInvoice>> response) {

                    on_responce_.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<TResponce<ListInvoice>> call, Throwable t) {

                    on_responce_.OnFailure(t);
                }
            });
    }

    //TODO:  Получение, список накладных для приемки на ресторане
    public void GetAcceptInvoice(Date date, TOnResponce on_responce_)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String dateText = dateFormat.format(date);

        Call<TResponce<ListAcceptedInvoice>> accept_invoices = rest_api.GetListAcceptInvoice(dateText);
        accept_invoices.enqueue(new Callback<TResponce<ListAcceptedInvoice>>() {
            @Override
            public void onResponse(Call<TResponce<ListAcceptedInvoice>> call, Response<TResponce<ListAcceptedInvoice>> response) {

                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce<ListAcceptedInvoice>> call, Throwable t) {

                on_responce_.OnFailure(t);
            }
        });
    }

    //TODO:  Получение, список накладных для приемки на ресторане
    public void GetInvoicesFromServer(Date date, String uid_sender, TypeInvoice type_invoice, TOnResponce on_responce_)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String date_str = dateFormat.format(date);

        Call<TResponce> accept_invoices = rest_api.GetInvoicesFromServer(date_str, uid_sender, type_invoice);
        accept_invoices.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {

                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {

                on_responce_.OnFailure(t);
            }
        });
    }

    //TODO:  Замена количества товара в накладной
    public void SetQuantityItem(TypeInvoice type_invoice, String uid_invoice,  String uid_item,  float quantity, String barcode_item, TOnResponce on_responce_)
    {
        Call<TResponce> set_quantity_item = rest_api.SetQuantityItem(type_invoice, uid_invoice,uid_item,quantity,barcode_item);
        set_quantity_item.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {

                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {

                on_responce_.OnFailure(t);
            }
        });
    }

    //TODO:  Замена количества товара в накладной и присвоить уникальный GUID
    public void SetQuantityAndUniqueUidItem( String uid_invoice,  String uid_item,  float quantity, String barcode_item, String unique_uid_item, TOnResponce on_responce_)
    {
        Call<TResponce> set_quantity_item = rest_api.SetQuantityAndUniqueUidItem(uid_invoice,uid_item,quantity,barcode_item, unique_uid_item);
        set_quantity_item.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {

                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {

                on_responce_.OnFailure(t);
            }
        });
    }

    //TODO:  Добавление товара в накладную
    public void AddItemToInvoice(String uid_invoice, String barcode_item, TOnResponce on_responce_)
    {
        Call<TResponce> add_item = rest_api.AddItemToInvoice(uid_invoice,  barcode_item);
        add_item.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {

                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {

                on_responce_.OnFailure(t);
            }
        });
    }

    //Удаление товара из накладной
    public void DeleteItemFromInvoice(String uid_invoice, String uid_item , String barcode_item, TOnResponce on_responce_)
    {
        Call<TResponce> delete_item = rest_api.DeleteItemFromInvoice(uid_invoice,  uid_item, barcode_item);
        delete_item.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {
                on_responce_.OnFailure(t);
            }
        });
    }

    //Проверка, существует ли баркод
    public void CheckItem(String barcode_item, TOnResponce on_responce_)
    {
        Call<TResponce<InvoiceItem>> cheked_item = rest_api.CheckItem(barcode_item);
        cheked_item.enqueue(new Callback<TResponce<InvoiceItem>>() {
            @Override
            public void onResponse(Call<TResponce<InvoiceItem>> call, Response<TResponce<InvoiceItem>> response) {

                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce<InvoiceItem>> call, Throwable t) {

                on_responce_.OnFailure(t);
            }
        });
    }

    //Проверка, принадлежит товар накладной
    public void CheckItemByInvoice (String uid_invoice, String uid_unique_item, TOnResponce on_responce_)
    {
        Call<TResponce> cheked_item = rest_api.CheckItemByInvoice(uid_invoice,uid_unique_item);
        cheked_item.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> response) {

                on_responce_.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {

                on_responce_.OnFailure(t);
            }
        });
    }

    //список товаров в накладной
    public void GetItemsOfInvoice(String uid_invoice, TypeInvoice type_invoice, TOnResponce on_responce_)
    {
        try
        {
            Call<TResponce<ListInvoiceItem>> invoice_items = rest_api.GetItemsOfInvoice(uid_invoice, type_invoice);

            invoice_items.enqueue(new Callback<TResponce<ListInvoiceItem>>() {
                @Override
                public void onResponse(Call<TResponce<ListInvoiceItem>> call, Response<TResponce<ListInvoiceItem>> response) {
                    on_responce_.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<TResponce<ListInvoiceItem>> call, Throwable t) {
                    on_responce_.OnFailure(t);
                }
            });
        }
        catch(Exception e)
        {
            String err_nes =  e.getMessage();
            on_responce_.OnFailure(e);
        }
    }

    //список товаров в сгрупированных накладных
    public void GetItemsOfInvoiceGroup(Date date, String uid_sender, TypeInvoice type_invoice, TOnResponce on_responce_)
    {
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
            String date_str = dateFormat.format(date);

            Call<TResponce<ListInvoiceItem>> invoice_items = rest_api.GetItemsOfInvoiceGroup(date_str, uid_sender, type_invoice);

            invoice_items.enqueue(new Callback<TResponce<ListInvoiceItem>>() {
                @Override
                public void onResponse(Call<TResponce<ListInvoiceItem>> call, Response<TResponce<ListInvoiceItem>> response) {
                    on_responce_.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<TResponce<ListInvoiceItem>> call, Throwable t) {
                    on_responce_.OnFailure(t);
                }
            });
        }
        catch(Exception e)
        {
            String err_nes =  e.getMessage();
            on_responce_.OnFailure(e);
        }
    }

    //список товаров в накладной 1C
    public void GetItemsOfInvoice1C(String uid_invoice, TOnResponce on_responce_)
    {
        try
        {
            Call<TResponce<ListInvoiceItem>> invoice_items = rest_api.GetItemsOfInvoice1C(uid_invoice);

            invoice_items.enqueue(new Callback<TResponce<ListInvoiceItem>>() {
                @Override
                public void onResponse(Call<TResponce<ListInvoiceItem>> call, Response<TResponce<ListInvoiceItem>> response) {
                    on_responce_.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<TResponce<ListInvoiceItem>> call, Throwable t) {
                    on_responce_.OnFailure(t);
                }
            });
        }
        catch(Exception e)
        {
            String err_nes =  e.getMessage();
            on_responce_.OnFailure(e);
        }
    }

    public void GetItemsOfInvoice1CGroup(String date_str, TOnResponce on_responce_)
    {
        try
        {
            Call<TResponce<ListInvoiceItem>> invoice_items = rest_api.GetItemsOfInvoice1CGroup(date_str);

            invoice_items.enqueue(new Callback<TResponce<ListInvoiceItem>>() {
                @Override
                public void onResponse(Call<TResponce<ListInvoiceItem>> call, Response<TResponce<ListInvoiceItem>> response) {
                    on_responce_.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<TResponce<ListInvoiceItem>> call, Throwable t) {
                    on_responce_.OnFailure(t);
                }
            });
        }
        catch(Exception e)
        {
            String err_nes =  e.getMessage();
            on_responce_.OnFailure(e);
        }
    }
    //Печать накладной
    public void PrintInvoice(String uid_invoice, int num_term , TOnResponce on_responce_)
    {
        try
        {
            Call<TResponce> print = rest_api.PrintInvoice(uid_invoice, num_term);

            print.enqueue(new Callback<TResponce>() {
                @Override
                public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                    on_responce_.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<TResponce> call, Throwable t) {
                    on_responce_.OnFailure(t);
                }
            });
        }
        catch(Exception e)
        {
            String err_nes =  e.getMessage();
            on_responce_.OnFailure(e);
        }
    }

    //Печать этикетки
    public void PrintLabel(String uid_invoice, String uid_item, int count_label, int num_term, TOnResponce on_responce_)
    {
        try
        {
            Call<TResponce> print_label = rest_api.PrintLabel(uid_invoice, uid_item, count_label, num_term);

            print_label.enqueue(new Callback<TResponce>() {
                @Override
                public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                    on_responce_.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<TResponce> call, Throwable t) {
                    on_responce_.OnFailure(t);
                }
            });
        }
        catch(Exception e)
        {
            String err_nes =  e.getMessage();
            on_responce_.OnFailure(e);
        }
    }

    //Закрытие приходной накладной
    public void ClosePurchaseInvoice(String uid_invoice, TOnResponce on_responce_)
    {
        try
        {
            Call<TResponce> close_invoice = rest_api.ClosePurchaseInvoice(uid_invoice);

            close_invoice.enqueue(new Callback<TResponce>() {
                @Override
                public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                    on_responce_.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<TResponce> call, Throwable t) {
                    on_responce_.OnFailure(t);
                }
            });
        }
        catch(Exception e)
        {
            String err_nes =  e.getMessage();
            on_responce_.OnFailure(e);
        }
    }
    //Результат синхронизации с GESTORI
    public void GetResultSynchronizedInvoice(String uid_invoice, TOnResponce on_responce_)
    {
        try
        {
            Call<TResponce> result_sync_invoice = rest_api.GetResultSynchronizedInvoice(uid_invoice);

            result_sync_invoice.enqueue(new Callback<TResponce>() {
                @Override
                public void onResponse(Call<TResponce> call, Response<TResponce> response) {
                    on_responce_.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<TResponce> call, Throwable t) {
                    on_responce_.OnFailure(t);
                }
            });
        }
        catch(Exception e)
        {
            String err_nes =  e.getMessage();
            on_responce_.OnFailure(e);
        }
    }
}
