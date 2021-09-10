package com.westas.orderassembly.calculator;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Barcode
{
    @SerializedName("barcode")
    public String Barcode;
    @SerializedName("quantity")
    public double Quantity;
    @SerializedName("date_end")
    public Date DateEnd;
}
