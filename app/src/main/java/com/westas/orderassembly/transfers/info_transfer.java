package com.westas.orderassembly.transfers;

import com.google.gson.annotations.SerializedName;

public class info_transfer {
    @SerializedName("num_transfer")
    public int num_transfer;
    @SerializedName("reciver_name")
    public String reciver_name;
    @SerializedName("sender_name")
    public String sender_name;
}
