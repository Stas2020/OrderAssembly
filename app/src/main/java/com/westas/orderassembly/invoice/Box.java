package com.westas.orderassembly.invoice;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Box {
    @SerializedName("uid")
    public String uid;
    @SerializedName("name")
    public String name;
    @SerializedName("all_scaned")
    public boolean all_scaned;

    public boolean selected;
}
