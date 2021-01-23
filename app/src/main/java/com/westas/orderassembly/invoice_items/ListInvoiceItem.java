package com.westas.orderassembly.invoice_items;

import com.google.gson.annotations.SerializedName;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ListInvoiceItem {
    @SerializedName("list_item")
    private List<InvoiceItem> list;

    private TOnChangeQuantity event_change_quantity;

    public void SetEventOfChangeQuantity(TOnChangeQuantity value)
    {
        event_change_quantity = value;
    }

    public void SortingItem()
    {
        try
        {
            Collections.sort(list, (o1, o2) -> {
                int res = 1;
                if (o1.verify != null)
                {
                    if ( o1.verify == SatusQuantity.equally || o1.verify == SatusQuantity.over || o1.verify == SatusQuantity.less)
                    {
                        res = -1;
                    }
                }


                return res;
            });
        }
        catch(Exception e)
        {
            String err_nes =  e.getMessage();
        }
    }

    public void Remove(int idx)
    {
        list.remove(idx);
    }
    public void Add(InvoiceItem item, int idx)
    {
        list.add(idx, item);
    }
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
                itm.quantity = itm.required_quantity;

                if (event_change_quantity != null)
                    event_change_quantity.EventChangeQuantity(itm.uid, itm.barcode, itm.quantity);

                ChangeStatus(itm);
                //Меняем позицию найденного item на первую
                ChangePositionToLastSelect(position);

                return true;
            }
            position++;
        }

        return false;
    }

    public int GetLastPisitionBySelect()
    {
        int pos = 0;
        for (InvoiceItem itm:list) {
            if(itm.verify == SatusQuantity.default_){
                return pos;
            }
            pos++;
        }
        return pos;
    }

    private void ChangePositionToLastSelect(int position)
    {
        if(position != 0)
        {
            InvoiceItem itm = list.get(position);
            list.remove(position);
            int idx = GetLastPisitionBySelect();
            list.add(idx,itm);
        }
    }

    private void ChangeStatus(InvoiceItem itm) {

        if (itm.quantity > itm.required_quantity)
        {
            itm.verify = SatusQuantity.over;
        }
        if (itm.quantity == itm.required_quantity)
        {
            itm.verify = SatusQuantity.equally;
        }
        if (itm.quantity < itm.required_quantity)
        {
            itm.verify = SatusQuantity.less;
        }

    }

    public void ChangeQuantity(float quantity, String code)
    {
        int position = 0;
        int last_position =0;
        for (InvoiceItem itm: list)
        {
            if (itm.barcode.equals(code))
            {
                itm.quantity = quantity;

                if (event_change_quantity != null){
                    event_change_quantity.EventChangeQuantity(itm.uid, itm.barcode, itm.quantity);
                }
                ChangeStatus(itm);
                //Меняем позицию найденного item на первую
                ChangePositionToLastSelect(position);

                break;
            }
            position++;
        }
    }

}
