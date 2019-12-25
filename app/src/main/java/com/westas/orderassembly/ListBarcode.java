package com.westas.orderassembly;

import java.util.ArrayList;
import java.util.List;

public class ListBarcode {

    private List<Barcode> list_barcode;

    public ListBarcode() {
        list_barcode = new ArrayList<>();
    }

    public Barcode GetItems(int position)
    {
        return list_barcode.get(position);
    }


    public int GetSize()
    {
        return list_barcode.size();
    }


    public void AddBarcode(Barcode barcode)
    {
        boolean contains = false;
        for (Barcode itm: list_barcode)
        {
            if (itm.Barcode.equals(barcode.Barcode) && itm.DateEnd.compareTo(barcode.DateEnd)== 0)
            {
                itm.Quantity += barcode.Quantity;
                contains = true;
                break;
            }
        }

        if (!contains)
        {
            list_barcode.add(barcode);
        }
    }

}
