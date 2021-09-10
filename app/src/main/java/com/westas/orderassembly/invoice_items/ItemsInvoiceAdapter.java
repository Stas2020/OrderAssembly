package com.westas.orderassembly.invoice_items;


import android.app.Activity;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.westas.orderassembly.R;

import static com.westas.orderassembly.invoice_items.SatusItem.add;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


public class ItemsInvoiceAdapter extends RecyclerView.Adapter<ItemsInvoiceAdapter.ItemsInvoiceViewHolder> {

    private ListInvoiceItem listInvoiceItem;
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;
    private Activity activity;

    public ItemsInvoiceAdapter(Activity _activity, ListInvoiceItem _listInvoiceItem, View.OnClickListener _onClickListener, View.OnLongClickListener _onLongClickListener)
    {
        listInvoiceItem = _listInvoiceItem;
        onClickListener = _onClickListener;
        onLongClickListener = _onLongClickListener;
        activity = _activity;
    }

    public void SetInvoices(ListInvoiceItem invoices)
    {
        listInvoiceItem = invoices;
    }

    public static class ItemsInvoiceViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView barcode;
        public TextView name;
        public TextView quantity;
        public TextView required_quantity;
        public TextView unit;
        public MaterialCardView cardview_of_goods;
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

        holder.unit.setText(listInvoiceItem.GetItems(position).GetUnit());
        holder.barcode.setText(listInvoiceItem.GetItems(position).GetBarcode());
        holder.name.setText(listInvoiceItem.GetItems(position).GetName());
        holder.quantity.setText(Float.toString(listInvoiceItem.GetItems(position).GetRequiredQuantity()));
        holder.required_quantity.setText(Float.toString(listInvoiceItem.GetItems(position).GetQuantity()));


        switch (listInvoiceItem.GetItems(position).GetStatusQuantity())
        {
            case less: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_less)); break;
            case equally: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_equally)); break;
            case over: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_over)); break;
            case default_: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_default)); break;
        }

        if(listInvoiceItem.GetItems(position).GetStatus() == add)
        {
            holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_add));
        }

/*
        if (listInvoiceItem.GetItems(position).status != null)
        {
            switch (listInvoiceItem.GetItems(position).status)
            {
                case add: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_add)); break;
                case delete: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_delete)); break;
                //case def: holder.cardview_of_goods.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_default)); break;
            }
        }
*/
        if(listInvoiceItem.CheckSelectedPosition(position))
        {
            holder.cardview_of_goods.setStrokeWidth(4);
        }
        else
        {
            holder.cardview_of_goods.setStrokeWidth(0);
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
