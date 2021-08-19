package com.westas.orderassembly.invoice_items;

import com.google.gson.annotations.SerializedName;

public class Box {
    @SerializedName("barcode")
    public String barcode;
    @SerializedName("quantity_in_box")
    public float quantity_in_box;
}
