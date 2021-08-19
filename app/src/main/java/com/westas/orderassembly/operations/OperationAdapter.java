package com.westas.orderassembly.operations;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.westas.orderassembly.R;
import com.westas.orderassembly.invoice_items.ItemsInvoiceAdapter;

public class OperationAdapter extends RecyclerView.Adapter<OperationAdapter.OperationViewHolder> {

    private ListOperation list_operation;

    OperationAdapter(ListOperation _list_operation)
    {
        list_operation = _list_operation;
    }

    @NonNull
    @Override
    public OperationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_item_operation, viewGroup, false);
        OperationAdapter.OperationViewHolder view_holder = new OperationAdapter.OperationViewHolder(view);
        return view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OperationViewHolder operationViewHolder, int i) {
        operationViewHolder.card_view_operation.setOnClickListener(list_operation.Get(i).GetOnClickListener());
        operationViewHolder.caption_operation.setText(list_operation.Get(i).GetCaption());
    }

    @Override
    public int getItemCount() {
        return list_operation.Count();
    }

    public class OperationViewHolder extends RecyclerView.ViewHolder
    {
        public TextView caption_operation;
        public View card_view_operation;
        public OperationViewHolder(@NonNull View itemView) {
            super(itemView);

            caption_operation = itemView.findViewById(R.id.textViewCaptionOperation);
            card_view_operation = itemView.findViewById(R.id.card_view_operation);
        }
    }
}
