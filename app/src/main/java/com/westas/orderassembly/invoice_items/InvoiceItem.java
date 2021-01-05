package com.westas.orderassembly.invoice_items;



import com.google.gson.annotations.SerializedName;

enum SatusQuantity {less, equally, over, default_}
enum SatusItem {add, delete, def}

public class InvoiceItem {

    @SerializedName("uid")
    public String uid;
    @SerializedName("barcode")
    public String barcode;
    @SerializedName("name")
    public String name;
    @SerializedName("quantity")
    public float quantity;
    @SerializedName("required_quantity")
    public float required_quantity;
    @SerializedName("unit")
    public String unit;
    @SerializedName("verify")
    public SatusQuantity verify;
    @SerializedName("status")
    public SatusItem status;
}

