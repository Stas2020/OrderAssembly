package com.westas.orderassembly.calculator;

import com.google.gson.Gson;
import com.westas.orderassembly.calculator.Barcode;

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
        Gson gson = new Gson();
        QRCode qr_code = gson.fromJson(json_str,QRCode.class);
        return qr_code;

    }
}
