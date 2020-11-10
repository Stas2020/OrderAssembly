package com.westas.orderassembly.rest_service;

import com.westas.orderassembly.invoice.ListTransferInvoice;

public interface TOnResponceListInvoice {
    void OnSuccess(ListTransferInvoice list_transfer_invoice);
    void OnFailure(Throwable t);
}
