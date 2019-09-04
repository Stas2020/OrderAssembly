package com.westas.orderassembly;

import java.util.ArrayList;
import java.util.Date;

public class ListTransferInvoice {

    public ListTransferInvoice()
    {
        this.list = new ArrayList<>();

        for (int i = 0; i < 3; i++)
        {
            TransferInvoice t_invoice = new TransferInvoice();
            t_invoice.DateInvoice = new Date();
            t_invoice.Number = "234543321";
            t_invoice.toSubdivision = 356;

            this.list.add(t_invoice);
        }


    }

    public java.util.List<TransferInvoice> list;
}
