package com.westas.orderassembly.accept_invoice;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.westas.orderassembly.R;
import com.westas.orderassembly.invoice.ListTransferInvoice;
import com.westas.orderassembly.invoice.ListTransferInvoiceAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ListInvoiceAcceptAdapter extends RecyclerView.Adapter<ListInvoiceAcceptAdapter.ListInvoceAcceptViewHolder> {

    private ListAcceptedInvoice list_accept_invoice;
    private Activity activity;

    public ListInvoiceAcceptAdapter(Activity _activity, ListAcceptedInvoice _list_accept_invoice)
    {
        list_accept_invoice = _list_accept_invoice;
        activity = _activity;
    }

    @NonNull
    @Override
    public ListInvoceAcceptViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invoice, viewGroup, false);
        ListInvoiceAcceptAdapter.ListInvoceAcceptViewHolder vh = new ListInvoiceAcceptAdapter.ListInvoceAcceptViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListInvoceAcceptViewHolder holder, int position) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        holder.date_invoice.setText(new SimpleDateFormat("dd MMM yyyy").format(list_accept_invoice.list.get(position).date));
        holder.number_invoice.setText( list_accept_invoice.list.get(position).uid);

        if(list_accept_invoice.list.get(position).accepted)
        {
            holder.cardview_of_invoice.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.invoice_close));
            holder.img_synhronised_invoice.setImageResource(R.drawable.check_32);

        }
    }

    @Override
    public int getItemCount() {
        return list_accept_invoice.list.size();
    }

    public class ListInvoceAcceptViewHolder extends RecyclerView.ViewHolder {
        public TextView date_invoice;
        public TextView number_invoice;
        public CardView cardview_of_invoice;
        public ImageView img_synhronised_invoice;

        public ListInvoceAcceptViewHolder(@NonNull View itemView) {
            super(itemView);
            date_invoice = itemView.findViewById(R.id.date_invoice);
            number_invoice = itemView.findViewById(R.id.number_invoice);
            cardview_of_invoice = itemView.findViewById(R.id.cardview_invoice);
            img_synhronised_invoice = itemView.findViewById(R.id.image_synchronized_invoice);
        }
    }
}
