package com.westas.orderassembly.invoice;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TransferInvoice {
    @SerializedName("uid")
    public String uid;
    @SerializedName("date")
    public Date date;
    @SerializedName("uid_customer")
    public String uid_customer;
    @SerializedName("closed")
    public boolean closed;
    @SerializedName("all_item_synhronized")
    public boolean all_item_synhronized;
}
