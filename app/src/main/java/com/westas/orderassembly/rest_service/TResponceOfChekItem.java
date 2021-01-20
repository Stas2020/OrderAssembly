package com.westas.orderassembly.rest_service;

import com.google.gson.annotations.SerializedName;
import com.westas.orderassembly.invoice_items.InvoiceItem;
import com.westas.orderassembly.subdivision.ListSubdivision;

public class TResponceOfChekItem {
    @SerializedName("Success")
    public boolean success;
    @SerializedName("Item")
    public InvoiceItem item;
    @SerializedName("Message")
    public String message;
}

