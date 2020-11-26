package com.westas.orderassembly.calculator;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class QRCode {
    @SerializedName("code")
    public String code;
    @SerializedName("name")
    public String name;
    @SerializedName("quantity")
    public double quantity;
}
