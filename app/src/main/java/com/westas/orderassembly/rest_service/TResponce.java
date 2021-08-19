package com.westas.orderassembly.rest_service;

import com.google.gson.annotations.SerializedName;

public class TResponce<T> {
    @SerializedName("Success")
    public boolean Success;
    @SerializedName("Message")
    public String Message;
    @SerializedName("Data")
    public T Data_;
}
