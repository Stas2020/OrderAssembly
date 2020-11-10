package com.westas.orderassembly.rest_service;

import com.westas.orderassembly.invoice_items.ListInvoiceItem;

public interface TOnResponceItemsInvoice {
    void OnSuccess(ListInvoiceItem items_invoice);
    void OnFailure(Throwable t);
}
