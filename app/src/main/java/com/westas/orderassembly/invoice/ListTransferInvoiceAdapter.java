package com.westas.orderassembly.invoice;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.westas.orderassembly.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ListTransferInvoiceAdapter extends RecyclerView.Adapter<ListTransferInvoiceAdapter.ListInvoceViewHolder> {

    private ListTransferInvoice listTransferInvoice;
    private View.OnClickListener onClickListener;

    public ListTransferInvoiceAdapter(ListTransferInvoice listInvoice,View.OnClickListener onClickListener)
    {
        listTransferInvoice = listInvoice;
        this.onClickListener = onClickListener;
    }

    public static class ListInvoceViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView date_invoice;
        public TextView number_invoice;
        public CardView cardview_of_invoice;
        public ListInvoceViewHolder(View v) {
            super(v);
            view = v;
            date_invoice = view.findViewById(R.id.date_invoice);
            number_invoice = view.findViewById(R.id.number_invoice);
            cardview_of_invoice = view.findViewById(R.id.cardview_invoice);
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
        holder.date_invoice.setText(new SimpleDateFormat("dd MMM yyyy").format(listTransferInvoice.list.get(position).date));
        holder.number_invoice.setText( listTransferInvoice.list.get(position).uid);

        if(listTransferInvoice.list.get(position).closed)
        {
            holder.cardview_of_invoice.setCardBackgroundColor(Color.parseColor("#47caff"));
        }
    }

    @Override
    public int getItemCount() {
        return listTransferInvoice.list.size();
    }



}
