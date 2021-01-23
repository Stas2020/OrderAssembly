package com.westas.orderassembly.subdivision;

import com.google.gson.annotations.SerializedName;

public class ListSubdivision {

    @SerializedName("list_subdivision")
    private java.util.List<Subdivision> list;

    private static int last_position;
    public Subdivision GetSubdivision(int position)
    {
        return list.get(position);
    }

    public void SelectSubdivision(int position)
    {
        list.get(last_position).selected = false;
        list.get(position).selected = true;
        last_position = position;
    }

    public boolean CheckSelectedPosition(int position)
    {
        return list.get(position).selected;
    }
    public String GetUidSelected()
    {
        for (Subdivision subdivision:list) {
            if(subdivision.selected == true){
                return subdivision.uid;
            }
        }
        return "";
    }

    public int GetSize()
    {
        return list.size();
    }
    public void SelectedByUid(String uid)
    {
            for (Subdivision subdivision:list) {
                if(subdivision.uid.equals(uid)){
                    subdivision.selected = true;
                }
            }
    }
}
