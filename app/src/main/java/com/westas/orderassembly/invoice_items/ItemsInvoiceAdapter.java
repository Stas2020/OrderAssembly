package com.westas.orderassembly.invoice_items;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
    private View.OnLongClickListener onLongClickListener;
    private Activity activity;

    public ItemsInvoiceAdapter(Activity _activity, ListInvoiceItem listInvoiceItem,View.OnClickListener _onClickListener,View.OnLongClickListener _onLongClickListener)
    {
        this.listInvoiceItem = listInvoiceItem;
        this.onClickListener = _onClickListener;
        this.onLongClickListener = _onLongClickListener;
        activity = _activity;
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
        view.setOnLongClickListener(onLongClickListener);
        return vh;
    }


    @Override
    public void onBindViewHolder(ItemsInvoiceViewHolder holder, int position) {

        holder.unit.setText(listInvoiceItem.GetItems(position).unit);
        holder.barcode.setText(listInvoiceItem.GetItems(position).barcode);
        holder.name.setText(listInvoiceItem.GetItems(position).name);
        holder.quantity.setText(Float.toString(listInvoiceItem.GetItems(position).required_quantity));
        holder.required_quantity.setText(Float.toString(listInvoiceItem.GetItems(position).quantity));

        if (listInvoiceItem.GetItems(position).verify != null)
        {
            switch (listInvoiceItem.GetItems(position).verify)
            {
                case less: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_less)); break;
                case equally: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_equally)); break;
                case over: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_over)); break;
                case default_: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_default)); break;
            }
        }
        else
        {
            holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_default));
        }

        if (listInvoiceItem.GetItems(position).status != null)
        {
            switch (listInvoiceItem.GetItems(position).status)
            {
                case add: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_add)); break;
                case delete: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_delete)); break;
                //case def: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_default)); break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return listInvoiceItem.GetSize();
    }

    public InvoiceItem GetItem(int position)
    {
        return listInvoiceItem.GetItems(position);
    }

    public void removeItem(int position) {
        listInvoiceItem.Remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(InvoiceItem item, int position) {
        listInvoiceItem.Add(item, position);
        notifyItemInserted(position);
    }
}
