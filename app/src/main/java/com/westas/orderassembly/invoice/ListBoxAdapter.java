package com.westas.orderassembly.invoice;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.westas.orderassembly.R;

import java.text.SimpleDateFormat;

public class ListBoxAdapter extends RecyclerView.Adapter<ListBoxAdapter.ListBoxViewHolder>{

    private ListBox list_box;
    private View.OnClickListener onClickListener;
    private Activity activity;

    public ListBoxAdapter(Activity _activity, ListBox list_box_, View.OnClickListener onClickListener)
    {
        list_box = list_box_;
        this.onClickListener = onClickListener;
        activity = _activity;
    }
    @NonNull
    @Override
    public ListBoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box, parent, false);
        ListBoxAdapter.ListBoxViewHolder vh = new ListBoxAdapter.ListBoxViewHolder(view);
        view.setOnClickListener(onClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListBoxViewHolder holder, int position) {

        holder.name.setText( list_box.GetBox(position).name);

        if(list_box.GetBox(position).all_scaned) {
            holder.cardview_of_box.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.invoice_checked));
            holder.img_status_box.setImageResource(R.drawable.check_32);
        }
        else{
            holder.cardview_of_box.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.invoice_alert));
        }

        if(list_box.CheckSelectedPosition(position) == true) {
            holder.cardview_of_box.setStrokeWidth(4);
        }
        else {
            holder.cardview_of_box.setStrokeWidth(0);
        }
    }

    @Override
    public int getItemCount() {
        return list_box.GetSize();
    }

    public static class ListBoxViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView name;
        public MaterialCardView cardview_of_box;
        public ImageView img_status_box;

        public ListBoxViewHolder(View v) {
            super(v);
            view = v;
            name = view.findViewById(R.id.name_box);
            cardview_of_box = view.findViewById(R.id.cardview_box);
            img_status_box = view.findViewById(R.id.img_status_box);
        }
    }
}
