package com.westas.orderassembly.item;

import android.app.Activity;
import android.view.*;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;
import com.westas.orderassembly.R;
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

    public void SetInvoices(ListItem _listInvoiceItem)
    {
        listInvoiceItem = _listInvoiceItem;
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
        //View view_swipe_menu = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout, parent, false);

        //ViewGroup view_group = view.findViewById(R.id.item_layout);
        ViewGroup view_group = (ViewGroup)view;
        //view_group.addView(view_swipe_menu);
        //view.bringToFront();

        ItemsInvoiceViewHolder vh = new ItemsInvoiceViewHolder(view);
        //MaterialCardView view_item_cardview = view.findViewById(R.id.item_cardview);
        //view_item_cardview.setOnClickListener(onClickListener);
        //view_item_cardview.setOnLongClickListener(onLongClickListener);

        view_group.setOnClickListener(onClickListener);
        view_group.setOnLongClickListener(onLongClickListener);

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

        Item item = listInvoiceItem.GetItems(position);
        //holder.unit.setText(item.GetUnit());
        holder.barcode.setText(item.GetBarcode());
        holder.name.setText(item.GetName());
        holder.quantity.setText(Float.toString(item.GetRequiredQuantity()));
        holder.required_quantity.setText(Float.toString(item.GetQuantity()));
        holder.item_cardview.setCardBackgroundColor(ContextCompat.getColor(activity, getBackgroundColor(item)));

        if(listInvoiceItem.CheckSelectedPosition(position))
            holder.item_cardview.setStrokeWidth(4);
        else
            holder.item_cardview.setStrokeWidth(0);
    }

    // обозначить статус позиции цветом
    int getBackgroundColor(Item item) {
        if(item.GetStatusSkip()!= StatusSkip.none)
            return  R.color.row_item_delete;

        switch (item.GetStatus())
        {
            case add: return R.color.row_item_add;
            case delete: return R.color.row_item_delete;
            default: break;
        }

        switch (item.GetStatusQuantity())
        {
            case less: return R.color.row_item_less;
            case equally: return R.color.row_item_equally;
            case over: return R.color.row_item_over;
            default: break;
        }
        return  R.color.row_item_default;
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

