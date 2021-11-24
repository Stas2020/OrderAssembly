package com.westas.orderassembly.rest_service;

import com.westas.orderassembly.calculator.ListBarcodeTemplate;
import com.westas.orderassembly.invoice.ListBox;
import com.westas.orderassembly.invoice.TypeOperation;
import com.westas.orderassembly.item.Item;
import com.westas.orderassembly.item.ListItem;
import com.westas.orderassembly.invoice.ListInvoice;
import com.westas.orderassembly.subdivision.ListSubdivision;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    @POST("/Service/json/GetListSubdivision")
    public Call<TResponce<ListSubdivision>> GetListSubdivision();

    @POST("/Service/json/GetListInvoiceBySender")
    public Call<TResponce<ListInvoice>>GetListInvoiceBySender(@Query("date") String date, @Query("uid_sender") String uid_sender, @Query("type_operation") TypeOperation type_operation);

    @POST("/Service/json/GetListOpenInvoiceBySender")
    public Call<TResponce<ListInvoice>>GetListOpenInvoiceBySender(@Query("uid_sender") String uid_sender, @Query("type_operation") TypeOperation type_operation);

    @POST("/Service/json/GetListInvoiceByReceiver")
    public Call<TResponce<ListInvoice>>GetListInvoiceByReceiver(@Query("date") String date, @Query("uid_receiver") String uid_sender, @Query("type_operation") TypeOperation type_operation);

    @POST("/Service/json/GetListBoxesBySender")
    public Call<TResponce<ListBox>>GetListBoxesBySender(@Query("date") String date, @Query("uid_sender") String uid_sender, @Query("type_operation") TypeOperation type_operation);

    @POST("/Service/json/GetInvoicesFromServer")
    public Call<TResponce>GetInvoicesFromServer(@Query("date") String date, @Query("uid_sender") String uid_sender, @Query("type_operation") TypeOperation type_operation);

    @POST("/Service/json/GetItemsOfInvoice")
    public Call<TResponce<ListItem>>GetItemsOfInvoice(@Query("uid_invoice") String uid_invoice, @Query("type_operation") TypeOperation type_operation);

    @POST("/Service/json/GetItemsOfBox")
    public Call<TResponce<ListItem>>GetItemsOfBox(@Query("uid_box") String uid_invoice, @Query("type_operation") TypeOperation type_operation);

    @POST("/Service/json/GetItemsOfInvoiceGroup")
    public Call<TResponce<ListItem>>GetItemsOfInvoiceGroup(@Query("date") String date, @Query("uid_sender") String uid_sender, @Query("type_operation") TypeOperation type_operation);

    @POST("/Service/json/CloseInvoice")
    public Call<TResponce>CloseInvoice(@Query("uid_sender") String uid_sender, @Query("uid_invoice") String uid_invoice, @Query("type_operation") TypeOperation type_operation);

    @POST("/Service/json/GetListBarcodeTemplate")
    public Call<TResponce<ListBarcodeTemplate>>GetListBarcodeTemplate();

    @POST("/Service/json/SetQuantityItem")
    public Call<TResponce>SetQuantityItem(@Query("type_operation") TypeOperation type_operation, @Query("uid_invoice") String uid_invoice, @Query("uid_item") String uid_item, @Query("quantity") float quantity, @Query("barcode_item") String barcode_item);

    @POST("/Service/json/SetQuantityAndUniqueUidItem")
    public Call<TResponce>SetQuantityAndUniqueUidItem(@Query("uid_invoice") String uid_invoice, @Query("uid_item") String uid_item, @Query("quantity") float quantity, @Query("barcode_item") String barcode_item, @Query("unique_uid_item") String unique_uid_item);

    @POST("/Service/json/AddItemToInvoice")
    public Call<TResponce>AddItemToInvoice(@Query("uid_invoice") String uid_invoice, @Query("barcode_item") String barcode_item);

    @POST("/Service/json/DeleteItemFromInvoice")
    public Call<TResponce>DeleteItemFromInvoice(@Query("uid_invoice") String uid_invoice, @Query("uid_item") String uid_item, @Query("barcode_item") String barcode_item);

    @POST("/Service/json/CheckItem")
    public Call<TResponce<Item>>CheckItem(@Query("barcode_item") String barcode_item);

    @POST("/Service/json/CheckItemByInvoice")
    public Call<TResponce>CheckItemByInvoice(@Query("uid_invoice") String uid_invoice, @Query("uid_unique_item") String uid_unique_item);

    @POST("/Service/json/PrintInvoice")
    public Call<TResponce>PrintInvoice(@Query("uid_invoice") String uid_invoice, @Query("num_term") int num_term);

    @POST("/Service/json/PrintLabel")
    public Call<TResponce>PrintLabel(@Query("uid_invoice") String uid_invoice, @Query("uid_item") String uid_item, @Query("count_label") int count_label, @Query("num_term") int num_term);

    @POST("/Service/json/GetResultSynchronizedInvoice")
    public Call<TResponce>GetResultSynchronizedInvoice(@Query("uid_invoice") String uid_invoice);


}
