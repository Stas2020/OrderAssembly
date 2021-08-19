package com.westas.orderassembly.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.westas.orderassembly.R;

import java.text.DecimalFormat;

import static android.view.KeyEvent.ACTION_DOWN;

;

public class TDialogForm
{

    private TCallBackDialogQuantity calback_event = null;
    private Activity activity_;
    private TextView textView_quantity;
    private  AlertDialog.Builder builder;
    private  AlertDialog dialog;
    private String title;
    private TTypeForm type_f;

    public TDialogForm(TCallBackDialogQuantity calback, Activity activity, String _title, TTypeForm _type)
    {
        calback_event = calback;
        activity_ = activity;
        title = _title;
        type_f = _type;
    }

    private void Init()
    {
        builder = new AlertDialog.Builder(activity_);
        builder.setTitle(title);


        View linearlayout = activity_.getLayoutInflater().inflate(R.layout.dialog_quantity, null);
        builder.setView(linearlayout);

        textView_quantity = linearlayout.findViewById(R.id.quantity);
        textView_quantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == 66 && event.getAction() == ACTION_DOWN)
                {
                    float quantity = 0;
                    String value = textView_quantity.getText().toString();
                    if (!value.isEmpty())
                    {
                        quantity = Float.parseFloat(value);;
                    }
                    calback_event.OnChangeQuantity(quantity, type_f);
                    dialog.hide();
                    return true;
                }

                return false;
            }

        });


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                float quantity = 0.000f;
                String value = textView_quantity.getText().toString();
                if (!value.isEmpty())
                {
                    //DecimalFormat myFormatter = new DecimalFormat("#.###");
                    quantity = Float.parseFloat(value);;
                }
                calback_event.OnChangeQuantity(quantity, type_f);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

    }

    public void Show(String message, double quantity)
    {
        Init();

        builder.setMessage(message);
        dialog = builder.create();

        //textView_quantity.setText(Double.toString(quantity));
        textView_quantity.setText("");

        dialog.show();
    }

    public void Hide()
    {
        dialog.hide();
    }
}
