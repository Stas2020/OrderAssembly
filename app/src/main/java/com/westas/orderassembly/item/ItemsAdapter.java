package com.westas.orderassembly.item;


import android.app.Activity;


import android.app.Application;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.westas.orderassembly.R;

import static com.westas.orderassembly.item.StausItem.add;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsInvoiceViewHolder> {

    private ListItem listInvoiceItem;
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;
    private Activity activity;

    public ItemsAdapter(Activity _activity, ListItem _listInvoiceItem, View.OnClickListener _onClickListener, View.OnLongClickListener _onLongClickListener)
    {
        listInvoiceItem = _listInvoiceItem;
        onClickListener = _onClickListener;
        onLongClickListener = _onLongClickListener;
        activity = _activity;
    }

    public void SetInvoices(ListItem invoices)
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
        public MaterialCardView item_cardview;


        public ItemsInvoiceViewHolder(View v) {
            super(v);
            view = v;
            barcode = view.findViewById(R.id.barcode);
            name = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quantity);
            required_quantity = view.findViewById(R.id.required_quantity);
            item_cardview = view.findViewById(R.id.item_cardview);

        }
    }

    @Override
    public ItemsInvoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        View view_swipe_menu = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout, parent, false);



        //ViewGroup view_group = view.findViewById(R.id.item_layout);
        ViewGroup view_group = (ViewGroup)view;
        view_group.addView(view_swipe_menu);
        //view.bringToFront();


        ItemsInvoiceViewHolder vh = new ItemsInvoiceViewHolder(view);
        //MaterialCardView view_item_cardview = view.findViewById(R.id.item_cardview);
        //view_item_cardview.setOnClickListener(onClickListener);
        //view_item_cardview.setOnLongClickListener(onLongClickListener);

        //view_group.setOnClickListener(onClickListener);
        //view_group.setOnLongClickListener(onLongClickListener);

/*
        view_group.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (v.getId()){
                    case R.id.item_layout:{
                        int gg = 10;
                        break;
                    }
                    case R.id.button:{
                        int gg = 0;
                        break;
                    }
                }

                int gg = 0;
                return false;
            }
        });
*/
        return vh;
    }


    @Override
    public void onBindViewHolder(ItemsInvoiceViewHolder holder, int position) {

        //holder.unit.setText(listInvoiceItem.GetItems(position).GetUnit());
        holder.barcode.setText(listInvoiceItem.GetItems(position).GetBarcode());
        holder.name.setText(listInvoiceItem.GetItems(position).GetName());
        holder.quantity.setText(Float.toString(listInvoiceItem.GetItems(position).GetRequiredQuantity()));
        holder.required_quantity.setText(Float.toString(listInvoiceItem.GetItems(position).GetQuantity()));


        switch (listInvoiceItem.GetItems(position).GetStatusQuantity())
        {
            case less: holder.item_cardview.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_less)); break;
            case equally: holder.item_cardview.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_equally)); break;
            case over: holder.item_cardview.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_over)); break;
            case default_: holder.item_cardview.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_default)); break;
        }

        if(listInvoiceItem.GetItems(position).GetStatus() == add)
        {
            holder.item_cardview.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.row_item_add));
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
            holder.item_cardview.setStrokeWidth(4);
        }
        else
        {
            holder.item_cardview.setStrokeWidth(0);
        }
    }

    @Override
    public int getItemCount() {
        return listInvoiceItem.GetSize();
    }

    public Item GetItem(int position)
    {
        return listInvoiceItem.GetItems(position);
    }

    public void removeItem(int position) {
        listInvoiceItem.Remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Item item, int position) {
        listInvoiceItem.Add(item, position);
        notifyItemInserted(position);
    }
}
