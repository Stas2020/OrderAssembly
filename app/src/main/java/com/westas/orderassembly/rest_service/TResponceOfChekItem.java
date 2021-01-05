package com.westas.orderassembly.rest_service;

import com.google.gson.annotations.SerializedName;
import com.westas.orderassembly.invoice_items.InvoiceItem;
import com.westas.orderassembly.subdivision.ListSubdivision;

public class TResponceOfChekItem {
    @SerializedName("success")
    public boolean success;
    @SerializedName("item")
    public InvoiceItem item;
    @SerializedName("message")
    public String message;
}

