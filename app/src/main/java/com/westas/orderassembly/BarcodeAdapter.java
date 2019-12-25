package com.westas.orderassembly;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.BarcodeViewHolder> {

    private ListBarcode list_barcode;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");

    public BarcodeAdapter(ListBarcode barcode)
    {
        this.list_barcode = barcode;
    }

    public static class BarcodeViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView barcode;
        public TextView quantity;
        public TextView date_end;

        public BarcodeViewHolder(View v) {
            super(v);
            view = v;
            barcode = view.findViewById(R.id.text_barcode);
            quantity = view.findViewById(R.id.text_quantity);
            date_end = view.findViewById(R.id.text_Date);

        }
    }

    @Override
    public BarcodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.barcode, parent, false);
        BarcodeAdapter.BarcodeViewHolder vh = new BarcodeAdapter.BarcodeViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(BarcodeAdapter.BarcodeViewHolder holder, int position) {

        holder.barcode.setText(list_barcode.GetItems(position).Barcode);
        holder.quantity.setText(String.format("%.3f",list_barcode.GetItems(position).Quantity));
        //holder.quantity.setText(Double.toString(list_barcode.GetItems(position).Quantity));
        holder.date_end.setText(dateFormat.format(list_barcode.GetItems(position).DateEnd));
    }

    @Override
    public int getItemCount() {
        return list_barcode.GetSize();
    }
}
