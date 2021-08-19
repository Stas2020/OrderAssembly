package com.westas.orderassembly.invoice;

import com.google.gson.annotations.SerializedName;

public class ListInvoice {

    @SerializedName("list_invoice")
    private java.util.List<Invoice> list;

    private static int last_position = 0;

    public void SelectInvoice(int position)
    {
        //todo ?????
        if(last_position < list.size())
        list.get(last_position).selected = false;

        list.get(position).selected = true;
        last_position = position;
    }

    public boolean CheckSelectedPosition(int position)
    {
        return list.get(position).selected;
    }

    public Invoice GetInvoice(int position)
    {
        return list.get(position);
    }
    public int GetSize()
    {
        return list.size();
    }

    public String GetUidSelected()
    {
        for (Invoice invoice:list) {
            if(invoice.selected == true){
                return invoice.uid;
            }
        }
        return "";
    }

    public void SelectedByUid(String uid)
    {
        for (Invoice invoice:list) {
            if(invoice.uid.equals(uid)){
                invoice.selected = true;
            }
        }
    }
}
