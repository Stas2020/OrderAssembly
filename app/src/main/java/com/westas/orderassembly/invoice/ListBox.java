package com.westas.orderassembly.invoice;

import com.google.gson.annotations.SerializedName;

public class ListBox {
    @SerializedName("list_box")
    private java.util.List<Box> list_box;



    private static int last_position = 0;

    public void SelectBox(int position)
    {
        //todo ?????
        if(last_position < list_box.size())
            list_box.get(last_position).selected = false;

        list_box.get(position).selected = true;
        last_position = position;
    }

    public boolean CheckSelectedPosition(int position)
    {
        return list_box.get(position).selected;
    }

    public Box GetBox(int position)
    {
        return list_box.get(position);
    }
    public int GetSize()
    {
        return list_box.size();
    }

    public String GetUidSelected()
    {
        for (Box box:list_box) {
            if(box.selected == true){
                return box.uid;
            }
        }
        return "";
    }

    public void SelectedByUid(String uid)
    {
        for (Box box:list_box) {
            if(box.uid.equals(uid)){
                box.selected = true;
            }
        }
    }


}
