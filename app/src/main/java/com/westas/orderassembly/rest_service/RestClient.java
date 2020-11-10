package com.westas.orderassembly.rest_service;

import android.content.SharedPreferences;

import com.westas.orderassembly.invoice_items.ListInvoiceItem;
import com.westas.orderassembly.invoice.ListTransferInvoice;
import com.westas.orderassembly.subdivision.ListSubdivision;

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
    public SharedPreferences shared_preferance;
    TOnResponceSubDivision on_responce_subdivision;
    TOnResponceListInvoice on_responce_list_invoice;
    TOnResponceItemsInvoice on_responce_items_invoice;

    public void SetEvent(TOnResponceSubDivision value)
    {
        on_responce_subdivision = value;
    }
    public void SetEvent(TOnResponceListInvoice value)
    {
        on_responce_list_invoice = value;
    }
    public void SetEvent(TOnResponceItemsInvoice value)
    {
        on_responce_items_invoice = value;
    }
    //Создаем Retrofit
    public void BuildRetrofit()
    {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        //Читаем настройки
        String NameServer = shared_preferance.getString("NAME_SERVER","localhost");

        assert NameServer != null;
        NameServer = NameServer.replaceAll("\n|\r\n", "");
        NameServer = NameServer.trim();

        String Port = shared_preferance.getString("NAME_PORT","8000");
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

    //Получение данных с REST сервера Подразделения
    public void GetDataSubdivision()
    {
        Call<ListSubdivision> list_subdivision = rest_api.GetDataSubdivision();
        list_subdivision.enqueue(new Callback<ListSubdivision>() {
            @Override
            public void onResponse(Call<ListSubdivision> call, Response<ListSubdivision> response) {
                on_responce_subdivision.OnSuccess(response.body());
            }
            @Override
            public void onFailure(Call<ListSubdivision> call, Throwable t) {
                on_responce_subdivision.OnFailure(t);
            }
        });
    }

    //Получение данных с REST сервера список накладных
    public void GetInvoice(String uid_customer)
    {
            Call<ListTransferInvoice> transfer_invoices = rest_api.GetListTransferInvoice(uid_customer);
            transfer_invoices.enqueue(new Callback<ListTransferInvoice>() {
                @Override
                public void onResponse(Call<ListTransferInvoice> call, Response<ListTransferInvoice> response) {

                    on_responce_list_invoice.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<ListTransferInvoice> call, Throwable t) {

                    on_responce_list_invoice.OnFailure(t);
                }
            });
    }


    //Получение данных с REST сервера список товаров в накладной
    public void GetInvoiceItem(String uid_invoice)
    {
        try
        {
            Call<ListInvoiceItem> invoice_items = rest_api.GetInvoiceItem(uid_invoice);

            invoice_items.enqueue(new Callback<ListInvoiceItem>() {
                @Override
                public void onResponse(Call<ListInvoiceItem> call, Response<ListInvoiceItem> response) {
                    on_responce_items_invoice.OnSuccess(response.body());
                }
                @Override
                public void onFailure(Call<ListInvoiceItem> call, Throwable t) {
                    on_responce_items_invoice.OnFailure(t);
                }
            });
        }
        catch(Exception e)
        {
            String err_nes =  e.getMessage();

        }
    }



}
