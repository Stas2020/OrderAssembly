package com.westas.orderassembly.accept_invoice;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AcceptedInvoice {
    @SerializedName("uid")
    public String uid;
    @SerializedName("num_doc")
    public String num_doc;
    @SerializedName("date")
    public Date date;
    @SerializedName("accepted")
    public boolean accepted;
}
