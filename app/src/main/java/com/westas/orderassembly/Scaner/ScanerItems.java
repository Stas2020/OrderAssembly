package com.westas.orderassembly.Scaner;

import java.util.ArrayList;
import java.util.List;



public class ScanerItems {

    private List<Item>items;
    ScanerItems()
    {
        items = new ArrayList<>();
    }

    public void Add(Item item)
    {
        items.add(item);
    }

    public Item Get(int id)
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
