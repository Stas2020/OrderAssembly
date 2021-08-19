package com.westas.orderassembly.toolbar;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.westas.orderassembly.R;

public class ToolbarItemsOfInvoice {

    private Toolbar toolbar;

    public ToolbarItemsOfInvoice(AppCompatActivity activity)
    {
        toolbar = new Toolbar(activity);


        Toolbar.LayoutParams toolBarParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT,
                R.attr.actionBarSize
        );
        toolbar.setLayoutParams(toolBarParams);

        activity.setSupportActionBar(toolbar);

        TextView caption = new TextView(activity);
        caption.setText("Заголовок");
        toolbar.addView(caption);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                activity.onBackPressed();// возврат на предыдущий activity
            }
        });

        activity.getActionBar().show();
    }

    public void SetCaption(String value)
    {

    }
    public void SetDateInvoice(String value)
    {

    }
    public  void SetNumberInvoice(String value)
    {

    }

}
