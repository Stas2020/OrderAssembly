package com.westas.orderassembly.item;

import com.google.gson.annotations.SerializedName;

public class Package_ {
    @SerializedName("barcode")
    public String barcode;
    @SerializedName("quantity_in_package")
    public float quantity_in_package;
}
