package com.westas.orderassembly.rest_service;

import com.westas.orderassembly.invoice_items.ListInvoiceItem;
import com.westas.orderassembly.invoice.ListTransferInvoice;
import com.westas.orderassembly.invoice.TransferInvoice;
import com.westas.orderassembly.subdivision.ListSubdivision;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    @POST("/Service/json/GetListSubdivision")
    public Call<ListSubdivision> GetListSubdivision();

    @POST("/Service/json/GetListTransferInvoice")
    public Call<ListTransferInvoice>GetListTransferInvoice(@Query("uid_customer") String uid_customer);

    @POST("/Service/json/SetQuantityItem")
    public Call<TResponce>SetQuantityItem(@Query("uid_invoice") String uid_invoice, @Query("uid_item") String uid_item, @Query("quantity") double quantity);

    @POST("/Service/json/AddItemToInvoice")
    public Call<TResponce>AddItemToInvoice(@Query("uid_invoice") String uid_invoice, @Query("barcode_item") String barcode_item, @Query("quantity") double quantity);

    @POST("/Service/json/GetItemsOfInvoice")
    public Call<ListInvoiceItem>GetItemsOfInvoice(@Query("uid_invoice") String uid_invoice);

    @POST("/Service/json/SetInvoiceItem")
    public Call<ListInvoiceItem>SetInvoiceItem(@Query("invoice") TransferInvoice invoice, @Query("items_invoice") TransferInvoice items_invoice);

    @POST("/Service/json/PrintInvoice")
    public Call<TResponce>PrintInvoice(@Query("uid_invoice") String uid_invoice);

    @POST("/Service/json/CloseInvoice")
    public Call<TResponce>CloseInvoice(@Query("uid_invoice") String uid_invoice);
}
