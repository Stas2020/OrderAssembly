package com.westas.orderassembly.invoice;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Invoice {
    @SerializedName("uid")
    public String uid;
    @SerializedName("num_doc")
    public String num_doc;
    @SerializedName("date")
    public Date date;
    @SerializedName("date_order")
    public Date date_order;
    @SerializedName("uid_reciver")
    public String uid_reciver;
    @SerializedName("name_reciver")
    public String name_reciver;
    @SerializedName("uid_sender")
    public String uid_sender;
    @SerializedName("name_sender")
    public String name_sender;
    @SerializedName("closed")
    public boolean closed;
    @SerializedName("all_item_synhronized")
    public boolean all_item_synhronized;

    public boolean selected;



}
