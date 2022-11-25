package com.westas.orderassembly.rest_service;

import android.app.Activity;
import android.util.Log;

import com.westas.orderassembly.calculator.ListBarcodeTemplate;
import com.westas.orderassembly.invoice.*;
import com.westas.orderassembly.item.*;
import com.westas.orderassembly.setting.Settings;
import com.westas.orderassembly.subdivision.ListSubdivision;
import com.westas.orderassembly.transfers.info_transfer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.*;
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
        //HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                //.addInterceptor(interceptor)
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

    public void GetListInvoiceBySender(Date date, String uid_sender, TypeOperation type_operation, TOnResponce<ListInvoice> on_response) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String date_str = dateFormat.format(date);
        ExecuteAsyncT(rest_api.GetListInvoiceBySender(date_str, uid_sender, type_operation), on_response);
    }

    public void GetListOpenInvoiceBySender(String uid_sender, TypeOperation type_operation, TOnResponce<ListInvoice> on_response) {
        ExecuteAsyncT(rest_api.GetListOpenInvoiceBySender(uid_sender, type_operation), on_response);
    }

    public void GetListInvoiceByReceiver(Date date, String uid_receiver, TypeOperation type_operation, TOnResponce<ListInvoice> on_response) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String date_str = dateFormat.format(date);
        ExecuteAsyncT(rest_api.GetListInvoiceByReceiver(date_str, uid_receiver, type_operation), on_response);
    }

    public void GetListBoxesBySender(Date date, String uid_sender, TypeOperation type_operation, TOnResponce<ListBox> on_response) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String date_str = dateFormat.format(date);
        ExecuteAsyncT(rest_api.GetListBoxesBySender(date_str, uid_sender, type_operation), on_response);
    }

    public void GetListBarcodeTemplate(TOnResponce<ListBarcodeTemplate> on_response) {
        ExecuteAsyncT(rest_api.GetListBarcodeTemplate(), on_response);
    }

    //Закрытие накладной
    public void CloseInvoice(String uid_sender, String uid_invoice, String claim_text, TypeOperation type_operation, TOnResponce on_response) {
        try {
            ExecuteAsync(rest_api.CloseInvoice(uid_sender, uid_invoice, type_operation, claim_text), on_response);
        }
        catch(Exception e) {
            on_response.OnFailure(e.getMessage());
        }
    }

    //TODO:  Получение, Подразделения
    public void GetListSubdivision(TOnResponce<ListSubdivision> on_response) {
        ExecuteAsyncT(rest_api.GetListSubdivision(), on_response);
    }

    //TODO:  Получение, список накладных для приемки на ресторане
    public void GetInvoicesFromServer(Date date, String uid_sender, TypeOperation type_operation, TOnResponce on_response) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String date_str = dateFormat.format(date);
        ExecuteAsync(rest_api.GetInvoicesFromServer(date_str, uid_sender, type_operation), on_response);
    }

    //TODO:  Замена количества товара в накладной
    public void SetQuantityItem(TypeOperation type_operation, String uid_invoice,  String uid_item,  float quantity, String barcode_item, TOnResponce on_response) {
        ExecuteAsync(rest_api.SetQuantityItem(type_operation, uid_invoice,uid_item,quantity,barcode_item), on_response);
    }
    //TODO:  Замена количества товара в коробке
    public void SetQuantityItemInBox(TypeOperation type_operation, String uid_invoice,  String uid_item,  String uid_box, float quantity, String barcode_item, TOnResponce on_response) {
        ExecuteAsync(rest_api.SetQuantityItemInBox(type_operation, uid_invoice,uid_item,uid_box, quantity,barcode_item), on_response);
    }
    //TODO:  Замена количества товара в накладной и присвоить уникальный GUID
    public void SetQuantityAndUniqueUidItem( String uid_invoice,  String uid_item,  float quantity, String barcode_item, String unique_uid_item, TOnResponce on_response) {
        ExecuteAsync(rest_api.SetQuantityAndUniqueUidItem(uid_invoice,uid_item,quantity,barcode_item, unique_uid_item), on_response);
    }

    //TODO:  Добавление товара в накладную
    public void AddItemToInvoice(String uid_invoice, String barcode_item, TOnResponce on_response) {
        ExecuteAsync(rest_api.AddItemToInvoice(uid_invoice,  barcode_item), on_response);
    }

    //TODO: Удаление товара из накладной
    public void DeleteItemFromInvoice(String uid_invoice, String uid_item , String barcode_item, TOnResponce on_response) {
        ExecuteAsync(rest_api.DeleteItemFromInvoice(uid_invoice,  uid_item, barcode_item), on_response);
    }

    //TODO: Проверка, существует ли баркод
    public void CheckItem(String barcode_item, TOnResponce<Item> on_response) {
        ExecuteAsyncT(rest_api.CheckItem(barcode_item), on_response);
    }

    //TODO: Проверка, принадлежит товар накладной
    public void CheckItemByInvoice (String uid_invoice, String uid_unique_item, TOnResponce on_response) {
        ExecuteAsync(rest_api.CheckItemByInvoice(uid_invoice,uid_unique_item), on_response);
    }

    //TODO: список товаров в накладной
    public void GetItemsOfInvoice(String uid_invoice, TypeOperation type_operation, TOnResponce<ListItem> on_response) {
        try {
            ExecuteAsyncT(rest_api.GetItemsOfInvoice(uid_invoice, type_operation), on_response);
        }
        catch(Exception e)
        {
            on_response.OnFailure(e.getMessage());
        }
    }

    //TODO: список товаров в накладной
    public void GetItemsOfBox(String uid_box, TypeOperation type_operation, TOnResponce on_response) {
        try {
            ExecuteAsyncT(rest_api.GetItemsOfBox(uid_box, type_operation), on_response);
        }
        catch(Exception e)
        {
            on_response.OnFailure(e.getMessage());
        }
    }
    //TODO: список товаров в сгрупированных накладных
    public void GetItemsOfInvoiceGroup(Date date, String uid_sender, TypeOperation type_operation, TOnResponce<ListItem> on_response) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
            String date_str = dateFormat.format(date);
            ExecuteAsyncT(rest_api.GetItemsOfInvoiceGroup(date_str, uid_sender, type_operation), on_response);
        }
        catch(Exception e)
        {
            on_response.OnFailure(e.getMessage());
        }
    }

    //TODO: Печать накладной
    public void PrintInvoice(String uid_invoice, int num_term , TOnResponce on_response) {
        try {
            ExecuteAsync(rest_api.PrintInvoice(uid_invoice, num_term), on_response);
        }
        catch(Exception e) {
            on_response.OnFailure(e.getMessage());
        }
    }

    //TODO: Печать этикетки
    public void PrintLabel(String uid_invoice, String uid_item, int count_label, int num_term, TOnResponce on_response) {
        try
        {
            ExecuteAsync(rest_api.PrintLabel(uid_invoice, uid_item, count_label, num_term), on_response);
        }
        catch(Exception e) {
            on_response.OnFailure(e.getMessage());
        }
    }

    //TODO: Результат синхронизации с GESTORI
    public void GetResultSynchronizedInvoice(String uid_invoice, TOnResponce on_response) {
        try {
            ExecuteAsync(rest_api.GetResultSynchronizedInvoice(uid_invoice), on_response);
        }
        catch(Exception e) {
            on_response.OnFailure(e.getMessage());
        }
    }

    public void GetDescriptionIncorrectItems(String uid_invoice, TypeOperation type_operation, TOnResponce<DescriptionIncorrectItems> on_response) {
        ExecuteAsyncT(rest_api.GetDescriptionIncorrectItems(uid_invoice, type_operation), on_response);
    }

    public void GetInfoClaim(String uid_invoice, TypeOperation type_operation, TOnResponce<Claim> on_response) {
        ExecuteAsyncT(rest_api.GetInfoClaim(uid_invoice, type_operation), on_response);
    }

    public void SendClaim(String uid_invoice, TypeOperation type_operation, TOnResponce on_response) {
        ExecuteAsync(rest_api.SendClaim(uid_invoice, type_operation), on_response);
    }

    public void GetInfoTransferNaumen(String uid_transfer, TOnResponce<info_transfer> on_response){
        ExecuteAsyncT(rest_api.GetInfoTransferNaumen(uid_transfer), on_response);
    }

    public void ClosedTransferNaumen(String uid_transfer, TOnResponce on_response){
        ExecuteAsync(rest_api.ClosedTransferNaumen(uid_transfer), on_response);
    }


    // Выполнение функций, возвращающих данные
    private <T> void ExecuteAsyncT(Call<TResponce<T>> call, TOnResponce<T> on_response){
        call.enqueue(new Callback<TResponce<T>>() {
            @Override
            public void onResponse(Call<TResponce<T>> call, Response<TResponce<T>> resp) {
                if (resp.code() == 200) {
                    on_response.OnSuccess(resp.body());
                }
                else {
                    on_response.OnFailure(resp.code() + " " +resp.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce<T>> call, Throwable t) {
                on_response.OnFailure(t.getMessage());
            }
        });
    }

    // Выполнение функций, возвращающих void
    private void ExecuteAsync(Call<TResponce> call, TOnResponce on_response){
        call.enqueue(new Callback<TResponce>() {
            @Override
            public void onResponse(Call<TResponce> call, Response<TResponce> resp) {
                if (resp.code() == 200) {
                    on_response.OnSuccess(resp.body());
                }
                else {
                    on_response.OnFailure(resp.code() + " " +resp.message());
                }
            }
            @Override
            public void onFailure(Call<TResponce> call, Throwable t) {
                on_response.OnFailure(t.getMessage());
            }
        });
    }

    // Для тестирования
    public <T> void TestExecuteAsyncT(Activity a, T test_data, TOnResponce<T> on_response){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
                TResponce<T> result = new TResponce<>();
                result.Data_ = test_data;
                result.Success = true;
                a.runOnUiThread(() -> on_response.OnSuccess(result));
            } catch (Exception ex) {
                a.runOnUiThread(() -> on_response.OnFailure(ex.getMessage()));
            }
        });
        t.start();
    }

    public void TestExecuteAsync(Activity a, TOnResponce on_response){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
                TResponce result = new TResponce();
                result.Success = true;
                a.runOnUiThread(() -> on_response.OnSuccess(result));
            } catch (Exception ex) {
                a.runOnUiThread(() -> on_response.OnFailure(ex.getMessage()));
            }
        });
        t.start();
    }

    public void TestExecuteAsyncErr(Activity a, TOnResponce on_response){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
                throw new Exception("Test error");
            } catch (Exception ex) {
                a.runOnUiThread(() -> on_response.OnFailure(ex.getMessage()));
            }
        });
        t.start();
    }

}
