package com.westas.orderassembly.rest_service;

import com.westas.orderassembly.accept_invoice.ListAcceptedInvoice;

public interface TOnResponceAcceptInvoice {
    void OnSuccess(ListAcceptedInvoice list_accept_invoice);
    void OnFailure(Throwable t);
}
