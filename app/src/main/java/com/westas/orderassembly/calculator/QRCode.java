package com.westas.orderassembly.calculator;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class QRCode {
    @SerializedName("uid_unique")
    public String uid_unique;
    @SerializedName("code")
    public String code;
    @SerializedName("name")
    public String name;
    @SerializedName("quantity")
    public float quantity;
}
