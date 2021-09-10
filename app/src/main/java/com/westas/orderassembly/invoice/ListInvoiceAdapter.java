package com.westas.orderassembly.invoice;

import android.app.Activity;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.westas.orderassembly.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ListInvoiceAdapter extends RecyclerView.Adapter<ListInvoiceAdapter.ListInvoceViewHolder> {

    private ListInvoice list_invoice;
    private View.OnClickListener onClickListener;
    private Activity activity;

    public ListInvoiceAdapter(Activity _activity, ListInvoice listInvoice, View.OnClickListener onClickListener)
    {
        list_invoice = listInvoice;
        this.onClickListener = onClickListener;
        activity = _activity;
    }

    public static class ListInvoceViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView date_order;
        public TextView number_invoice;
        public MaterialCardView cardview_of_invoice;
        public ImageView img_synhronised_invoice;

        public ListInvoceViewHolder(View v) {
            super(v);
            view = v;
            date_order = view.findViewById(R.id.date_order);
            number_invoice = view.findViewById(R.id.number_invoice);
            cardview_of_invoice = view.findViewById(R.id.cardview_invoice);
            img_synhronised_invoice = view.findViewById(R.id.image_synchronized_invoice);
        }
    }

    @Override
    public ListInvoceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        ListInvoceViewHolder vh = new ListInvoceViewHolder(view);
        view.setOnClickListener(onClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListInvoceViewHolder holder, int position) {

        int year = list_invoice.GetInvoice(position).date_order.getYear() + 1900;
        if (year < 2000)
        {
            holder.date_order.setText("Дата заказа: отсутствует");
        }
        else
        {
            holder.date_order.setText("Дата заказа: " + new SimpleDateFormat("dd MMM yy").format(list_invoice.GetInvoice(position).date_order));
        }

        holder.number_invoice.setText( list_invoice.GetInvoice(position).num_doc);


        if(list_invoice.GetInvoice(position).closed)
        {
            holder.cardview_of_invoice.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.invoice_close));
            holder.img_synhronised_invoice.setImageResource(R.drawable.closed_32);

        }
        else
        {
            if(list_invoice.GetInvoice(position).all_item_synhronized)
            {
                holder.img_synhronised_invoice.setImageResource(R.drawable.check_32);
                holder.cardview_of_invoice.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.invoice_checked));
            }
            else
            {
                holder.img_synhronised_invoice.setImageResource(R.drawable.alert_32);
                holder.cardview_of_invoice.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.invoice_alert));
            }
        }


        if(list_invoice.CheckSelectedPosition(position) == true)
        {
            holder.cardview_of_invoice.setStrokeWidth(4);
        }
        else
        {
            holder.cardview_of_invoice.setStrokeWidth(0);
        }
    }

    @Override
    public int getItemCount() {
        return list_invoice.GetSize();
    }



}
