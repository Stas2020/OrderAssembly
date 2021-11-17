package com.westas.orderassembly.item;

public interface TOnChangeQuantity {
    void EventChangeQuantity(String uid, String barcode, float quantity);
}
