package com.westas.orderassembly.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;



public class TDialogQuestion {

    private TCallBackDialog calback_event = null;
    private Activity activity_;
    private  AlertDialog.Builder builder;
    private  AlertDialog dialog;
    private String title;

    public TDialogQuestion(Activity activity, String _title)
    {
        activity_ = activity;
        title = _title;
    }
    private void Init()
    {
        builder = new AlertDialog.Builder(activity_);
        builder.setTitle(title);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                if(calback_event != null){
                    calback_event.OnSuccess(true);
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                if(calback_event != null){
                    calback_event.OnSuccess(false);
                }

            }
        });

    }

    public void Show(String message, TCallBackDialog calback)
    {
        calback_event = calback;

        Init();
        builder.setMessage(message);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public void Hide()
    {
        dialog.hide();
    }
}
