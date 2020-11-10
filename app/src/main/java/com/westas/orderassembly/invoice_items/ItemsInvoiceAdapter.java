package com.westas.orderassembly.invoice_items;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.westas.orderassembly.R;


public class ItemsInvoiceAdapter extends RecyclerView.Adapter<ItemsInvoiceAdapter.ItemsInvoiceViewHolder> {

    private ListInvoiceItem listInvoiceItem;
    private View.OnClickListener onClickListener;

    public ItemsInvoiceAdapter(ListInvoiceItem listInvoiceItem,View.OnClickListener onClickListener)
    {
        this.listInvoiceItem = listInvoiceItem;
        this.onClickListener = onClickListener;
    }

    public static class ItemsInvoiceViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView barcode;
        public TextView name;
        public TextView quantity;
        public TextView required_quantity;
        public TextView unit;
        public CardView cardview_of_goods;
        public ItemsInvoiceViewHolder(View v) {
            super(v);
            view = v;
            barcode = view.findViewById(R.id.barcode);
            name = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quantity);
            required_quantity = view.findViewById(R.id.required_quantity);
            unit = view.findViewById(R.id.unit);
            cardview_of_goods = view.findViewById(R.id.cardview_of_goods);
        }
    }

    @Override
    public ItemsInvoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_of_invoice, parent, false);
        ItemsInvoiceViewHolder vh = new ItemsInvoiceViewHolder(view);
        view.setOnClickListener(onClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ItemsInvoiceViewHolder holder, int position) {

        holder.unit.setText(listInvoiceItem.GetItems(position).unit);
        holder.barcode.setText(listInvoiceItem.GetItems(position).barcode);
        holder.name.setText(listInvoiceItem.GetItems(position).name);
        holder.quantity.setText(Double.toString(listInvoiceItem.GetItems(position).required_quantity));
        holder.required_quantity.setText(Double.toString(listInvoiceItem.GetItems(position).quantity));

        if (listInvoiceItem.GetItems(position).verify != null)
        {
            switch (listInvoiceItem.GetItems(position).verify)
            {
                case less: holder.cardview_of_goods.setCardBackgroundColor(Color.parseColor("#c0e8ff")); break;
                case equally: holder.cardview_of_goods.setCardBackgroundColor(Color.parseColor("#5cf800")); break;
                case over: holder.cardview_of_goods.setCardBackgroundColor(Color.parseColor("#ffa18c")); break;
                default: holder.cardview_of_goods.setCardBackgroundColor(Color.parseColor("#fffde2")); break;
            }
        }
        else
        {
            holder.cardview_of_goods.setCardBackgroundColor(Color.parseColor("#fffde2"));
        }



    }

    @Override
    public int getItemCount() {
        return listInvoiceItem.GetSize();
    }
}
