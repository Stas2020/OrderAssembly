package com.westas.orderassembly.scaner;

import java.util.ArrayList;
import java.util.List;



public class ScanerItems {

    private List<ItemScan>items;
    ScanerItems()
    {
        items = new ArrayList<>();
    }

    public void Add(ItemScan item)
    {
        items.add(item);
    }

    public ItemScan Get(int id)
    {
        return items.get(id);
    }

    public int Count()
    {
        return items.size();
    }

    public void Clear()
    {
        items.clear();
    }
}
