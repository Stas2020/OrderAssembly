package com.westas.orderassembly.calculator;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListBarcodeTemplate {
    @SerializedName("list_barcode_template")
    private List<BarcodeTemplate>list_barcode_template;

    public int GetLenghtMaxPrefix()
    {
        int result = 0;
        for (BarcodeTemplate it: list_barcode_template) {
            if(it.prefix.length() > result)
            {
                result = it.prefix.length();
            }
        }
        return result;
    }

    public BarcodeTemplate GetTemplateByPrefix(String prefix)
    {
        for (BarcodeTemplate it: list_barcode_template) {
            if(it.prefix.compareTo(prefix) == 0)
            {
                return it;
            }
        }
        return null;
    }
}
