package com.westas.orderassembly.subdivision;

import com.google.gson.annotations.SerializedName;

public class Subdivision {

    @SerializedName("uid")
    public String uid;
    @SerializedName("name")
    public String name;
    public boolean selected;
}
