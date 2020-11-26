package com.westas.orderassembly.invoice_items;

import com.westas.orderassembly.rest_service.TResponce;

public interface TOnChangeQuantity {
    void EventChangeQuantity(String uid, String barcode, double quantity);
}
