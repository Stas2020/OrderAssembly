package com.westas.orderassembly.rest_service;

import com.westas.orderassembly.invoice_items.ListInvoiceItem;
import com.westas.orderassembly.invoice.ListTransferInvoice;
import com.westas.orderassembly.invoice.TransferInvoice;
import com.westas.orderassembly.subdivision.ListSubdivision;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    @POST("/Service/json/GetDataSubdivision")
    public Call<ListSubdivision> GetDataSubdivision();

    @POST("/Service/json/GetListTransferInvoice")
    public Call<ListTransferInvoice>GetListTransferInvoice(@Query("uid_customer") String uid_customer);

    @POST("/Service/json/GetInvoiceItem")
    public Call<ListInvoiceItem>GetInvoiceItem(@Query("uid_invoice") String uid_invoice);

    @POST("/Service/json/SetInvoiceItem")
    public Call<ListInvoiceItem>SetInvoiceItem(@Query("invoice") TransferInvoice invoice, @Query("items_invoice") TransferInvoice items_invoice);
}
