package com.westas.orderassembly.scaner;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.westas.orderassembly.R;
import java.text.SimpleDateFormat;


public class ScanerAdapter extends RecyclerView.Adapter<ScanerAdapter.ScanerAdapterViewHolder>{

    private ScanerItems items;

    ScanerAdapter(ScanerItems items_)
    {
        items = items_;
    }


    @NonNull
    @Override
    public ScanerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scan_item, viewGroup, false);
        ScanerAdapter.ScanerAdapterViewHolder view_holder = new ScanerAdapter.ScanerAdapterViewHolder(view);
        return view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScanerAdapterViewHolder scanerAdapterViewHolder, int i) {

        String time_str = new SimpleDateFormat("hh:mm").format(items.Get(i).time);
        scanerAdapterViewHolder.text_time.setText(time_str);
        scanerAdapterViewHolder.text_message.setText(items.Get(i).code_value);

    }

    @Override
    public int getItemCount() {
        return items.Count();
    }


    public class ScanerAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView text_time;
        public  TextView text_message;

        public ScanerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            text_time = itemView.findViewById(R.id.textView_time);
            text_message = itemView.findViewById(R.id.textView_message);
        }
    }
}
