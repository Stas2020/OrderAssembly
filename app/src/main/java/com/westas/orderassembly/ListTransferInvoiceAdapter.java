package com.westas.orderassembly;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        public ListInvoceViewHolder(View v) {
            super(v);
            view = v;
            date_invoice = view.findViewById(R.id.date_invoice);
            number_invoice = view.findViewById(R.id.number_invoice);
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

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        holder.date_invoice.setText(dateFormat.format(listTransferInvoice.list.get(position).DateInvoice));
        holder.number_invoice.setText( listTransferInvoice.list.get(position).Number);
    }

    @Override
    public int getItemCount() {
        return listTransferInvoice.list.size();
    }



}
