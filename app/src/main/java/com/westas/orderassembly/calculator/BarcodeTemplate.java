package com.westas.orderassembly.calculator;

import com.google.gson.annotations.SerializedName;

public class BarcodeTemplate {
    @SerializedName("barcode_lenght")
    int barcode_lenght;
    @SerializedName("prefix")
    String prefix;
    @SerializedName("code_good_start")
    int code_good_start;
    @SerializedName("code_good_end")
    int code_good_end;
    @SerializedName("kg_start")
    int kg_start;
    @SerializedName("kg_end")
    int kg_end;
    @SerializedName("gramm_start")
    int gramm_start;
    @SerializedName("gramm_end")
    int gramm_end;
}
