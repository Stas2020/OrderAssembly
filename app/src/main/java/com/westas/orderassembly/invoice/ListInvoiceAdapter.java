package com.westas.orderassembly.invoice;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.card.MaterialCardView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.westas.orderassembly.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ListTransferInvoiceAdapter extends RecyclerView.Adapter<ListTransferInvoiceAdapter.ListInvoceViewHolder> {

    private ListTransferInvoice listTransferInvoice;
    private View.OnClickListener onClickListener;
    private Activity activity;

    public ListTransferInvoiceAdapter(Activity _activity, ListTransferInvoice listInvoice, View.OnClickListener onClickListener)
    {
        listTransferInvoice = listInvoice;
        this.onClickListener = onClickListener;
        activity = _activity;
    }

    public static class ListInvoceViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView date_invoice;
        public TextView number_invoice;
        public MaterialCardView cardview_of_invoice;
        public ImageView img_synhronised_invoice;
        public ListInvoceViewHolder(View v) {
            super(v);
            view = v;
            date_invoice = view.findViewById(R.id.date_invoice);
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

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        holder.date_invoice.setText(new SimpleDateFormat("dd MMM yy").format(listTransferInvoice.GetInvoice(position).date));
        holder.number_invoice.setText( listTransferInvoice.GetInvoice(position).num_doc);


        if(listTransferInvoice.GetInvoice(position).closed)
        {
            holder.cardview_of_invoice.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.invoice_close));
            holder.img_synhronised_invoice.setImageResource(R.drawable.closed_32);

        }
        else
        {
            if(listTransferInvoice.GetInvoice(position).all_item_synhronized)
            {
                holder.img_synhronised_invoice.setImageResource(R.drawable.check_32);
                holder.cardview_of_invoice.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.invoice_checked));
            }
            else
            {
                holder.img_synhronised_invoice.setImageResource(R.drawable.alert_32);
                holder.cardview_of_invoice.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.invoice_alert));
            }
            //holder.cardview_of_invoice.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.invoice_default));
        }


        if(listTransferInvoice.CheckSelectedPosition(position) == true)
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
        return listTransferInvoice.GetSize();
    }



}
