package com.westas.orderassembly;

import android.support.v7.widget.CardView;

enum Satus {less, equally, over};

public class InvoiceItem {
    public String Barcode;
    public String Name;
    public double Quantity;
    public double RequiredQuantity;
    public String Unit;
    public Satus Verify;
}
