package com.westas.orderassembly.accept_invoice;

import com.google.gson.annotations.SerializedName;
import com.westas.orderassembly.invoice.TransferInvoice;

public class ListAcceptedInvoice {
    @SerializedName("list_accept_invoice")
    public java.util.List<AcceptedInvoice> list;
}