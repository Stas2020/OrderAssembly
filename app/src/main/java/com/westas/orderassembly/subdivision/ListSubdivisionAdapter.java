package com.westas.orderassembly.subdivision;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.westas.orderassembly.R;


public class ListSubdivisionAdapter extends RecyclerView.Adapter<ListSubdivisionAdapter.ListSubdivisionViewHolder> {

    private View.OnClickListener list_subdivision_activity;
    private ListSubdivision _listSubdivision;

    public static class ListSubdivisionViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView num_Subdivision;
        public TextView name_Subdivision;
        public MaterialCardView card_view_subdivision;
        public ListSubdivisionViewHolder(View v) {
            super(v);
            view = v;
            num_Subdivision = view.findViewById(R.id.NumSubdivision);
            name_Subdivision = view.findViewById(R.id.SubdivisionName);
            card_view_subdivision = view.findViewById(R.id.card_view_subdivision);
        }
    }


    public ListSubdivisionAdapter(ListSubdivision listSubdivision, View.OnClickListener activity) {
        _listSubdivision = listSubdivision;
        list_subdivision_activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListSubdivisionAdapter.ListSubdivisionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subdivision, parent, false);
        ListSubdivisionViewHolder vh = new ListSubdivisionViewHolder(view);
        view.setOnClickListener(list_subdivision_activity);
        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ListSubdivisionViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.name_Subdivision.setText(_listSubdivision.GetSubdivision(position).name);
        holder.num_Subdivision.setText( _listSubdivision.GetSubdivision(position).uid);

        if(_listSubdivision.CheckSelectedPosition(position)){
            holder.card_view_subdivision.setStrokeWidth(3);
        }
        else
        {
            holder.card_view_subdivision.setStrokeWidth(0);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _listSubdivision.GetSize();
    }



}
