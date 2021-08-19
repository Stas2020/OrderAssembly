package com.westas.orderassembly.operations;

import java.util.ArrayList;
import java.util.List;

public class ListOperation {

    private List<Operation>list_operation;

    ListOperation()
    {
        list_operation = new ArrayList<>();
    }
    public Operation Get(int id)
    {
        return list_operation.get(id);
    }
    public int Count()
    {
        return list_operation.size();
    }
    public void Add(Operation operation)
    {
        list_operation.add(operation);
    }
}
