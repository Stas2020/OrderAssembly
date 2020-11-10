package com.westas.orderassembly.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.westas.orderassembly.R;


public class TDialogQuantity
{

    private TCallBackDialogQuantity calback_event;
    private Activity activity_;
    private TextView textView_quantity;
    private  AlertDialog.Builder builder;
    private  AlertDialog dialog;

    public TDialogQuantity(TCallBackDialogQuantity calback, Activity activity)
    {
        calback_event = calback;
        activity_ = activity;

    }

    private void Init()
    {
        builder = new AlertDialog.Builder(activity_);
        builder.setTitle("Количество");


        View linearlayout = activity_.getLayoutInflater().inflate(R.layout.dialog_quantity, null);
        builder.setView(linearlayout);

        textView_quantity = linearlayout.findViewById(R.id.quantity);


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                double quantity = Double.parseDouble(textView_quantity.getText().toString());;
                calback_event.OnChangeQuantity(quantity);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });




    }

    public void Show(String NameGoods, double quantity)
    {
        Init();

        builder.setMessage(NameGoods);
        dialog = builder.create();

        textView_quantity.setText(Double.toString(quantity));

        dialog.show();
    }
}
