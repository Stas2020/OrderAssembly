package com.westas.orderassembly.calculator;

import android.widget.Toast;

import com.google.gson.Gson;
import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.calculator.Barcode;
import com.westas.orderassembly.invoice_items.InvoiceItem;
import com.westas.orderassembly.rest_service.TOnResponce;
import com.westas.orderassembly.rest_service.TResponce;

import java.text.SimpleDateFormat;

public class ParseBarcode {

    private Barcode GetBarcode(String barcode) throws Exception
    {
        SimpleDateFormat format_date = new SimpleDateFormat("yyMMdd");
        Barcode res = null;

        if (barcode.length() > 32)
        {
            res = new Barcode();
            res.Barcode = barcode.substring(2,16);

            int identificator = Integer.parseInt(barcode.substring(16,20));
            switch (identificator)
            {
                case 3102: {
                    String Quant = barcode.substring(20,26);
                    StringBuffer Buff = new StringBuffer(Quant);
                    Buff.insert(4,".");
                    res.Quantity = Double.parseDouble(Buff.toString());
                    break;
                }
                case 3103:{
                    String Quant = barcode.substring(20,26);
                    StringBuffer Buff = new StringBuffer(Quant);
                    Buff.insert(3,".");
                    res.Quantity = Double.parseDouble(Buff.toString());
                    break;
                }
            }


            String dt = barcode.substring(36,42);
            res.DateEnd = format_date.parse(dt);
        }


        return res;
    }

    public class WeightAndCode
    {
        public float weight = 0;
        public String good_code = "";

    }
    public WeightAndCode ParseBarcodeByWeight(String code, ListBarcodeTemplate list_barcode_template)
    {
        WeightAndCode result = new WeightAndCode();
        int lenght_prefix = list_barcode_template.GetLenghtMaxPrefix();

        for(int id = lenght_prefix; id > 0; id--)
        {
            String prefix = code.substring(0,id);
            BarcodeTemplate template_barcode = list_barcode_template.GetTemplateByPrefix(prefix);
            if(template_barcode != null)
            {
                String weight_by_kg_str = code.substring(template_barcode.kg_start - 1,template_barcode.kg_end);
                String weight_by_gramm_str = code.substring(template_barcode.gramm_start - 1,template_barcode.gramm_end);
                String code_good_str = code.substring(template_barcode.code_good_start - 1,template_barcode.code_good_end);

                result = new WeightAndCode();
                result.weight = Float.parseFloat(weight_by_kg_str + weight_by_gramm_str)/1000;
                result.good_code = code_good_str;

                break;
            }
        }
        return result;
    }

    public Barcode Parse(String barcode){
        Barcode res = null;
        try {
            res =  GetBarcode(barcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public QRCode ParseJSON(String json_str)
    {
        try
        {
            Gson gson = new Gson();
            QRCode qr_code = gson.fromJson(json_str,QRCode.class);
            return qr_code;
        }
        catch(Exception e)
        {
            return null;
        }

    }
}
