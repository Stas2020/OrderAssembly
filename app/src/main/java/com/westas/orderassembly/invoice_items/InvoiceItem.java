package com.westas.orderassembly.invoice_items;



import com.google.gson.annotations.SerializedName;

enum Satus {less, equally, over}

public class InvoiceItem {

    @SerializedName("uid")
    public String uid;
    @SerializedName("barcode")
    public String barcode;
    @SerializedName("name")
    public String name;
    @SerializedName("quantity")
    public double quantity;
    @SerializedName("required_quantity")
    public double required_quantity;
    @SerializedName("unit")
    public String unit;
    @SerializedName("verify")
    public Satus verify;
}
