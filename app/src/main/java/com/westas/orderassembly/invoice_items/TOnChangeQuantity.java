package com.westas.orderassembly.invoice_items;

public interface TOnChangeQuantity {
    void EventChangeQuantity(String uid, String barcode, float quantity);
}
