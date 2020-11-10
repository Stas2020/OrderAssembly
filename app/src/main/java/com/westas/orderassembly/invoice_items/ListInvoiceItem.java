package com.westas.orderassembly.invoice_items;

import com.google.gson.annotations.SerializedName;
import com.westas.orderassembly.invoice_items.Satus;

import java.util.List;

public class ListInvoiceItem {
    @SerializedName("list_item")
    private List<InvoiceItem> list;

    public InvoiceItem GetItems(int position)
    {
        return list.get(position);
    }

    public int GetSize()
    {
        return list.size();
    }

    public boolean VerifyGoods(double quantity, String code)
    {
        int position = 0;
        for (InvoiceItem itm: list)
        {
            if (itm.barcode.equals(code))
            {
                itm.quantity += quantity;

                if (itm.quantity > itm.required_quantity)
                {
                    itm.verify = Satus.over;
                }
                if (itm.quantity == itm.required_quantity)
                {
                    itm.verify = Satus.equally;
                }
                if (itm.quantity < itm.required_quantity)
                {
                    itm.verify = Satus.less;
                }


                //Меняем позицию найденного item на первую
                if(position != 0)
                {
                    list.remove(position);
                    list.add(0,itm);
                }

                return true;
            }
            position++;
        }

        return false;
    }

    public void ChangeQuantity(double quantity, String code)
    {
        int position = 0;
        for (InvoiceItem itm: list)
        {
            if (itm.barcode.equals(code))
            {
                itm.quantity = quantity;

                if (itm.quantity > itm.required_quantity)
                {
                    itm.verify = Satus.over;
                }
                if (itm.quantity == itm.required_quantity)
                {
                    itm.verify = Satus.equally;
                }
                if (itm.quantity < itm.required_quantity)
                {
                    itm.verify = Satus.less;
                }


                //Меняем позицию найденного item на первую
                if(position != 0)
                {
                    list.remove(position);
                    list.add(0,itm);
                }

                break;

            }
            position++;
        }
    }

}
