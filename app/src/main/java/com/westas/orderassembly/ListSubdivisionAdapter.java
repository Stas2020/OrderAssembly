package com.westas.orderassembly;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListSubdivisionAdapter extends RecyclerView.Adapter<ListSubdivisionAdapter.ListSubdivisionViewHolder> {

    private View.OnClickListener list_subdivision_activity;
    private ListSubdivision _listSubdivision;

    public static class ListSubdivisionViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView num_Subdivision;
        public TextView name_Subdivision;
        public ListSubdivisionViewHolder(View v) {
            super(v);
            view = v;
            num_Subdivision = view.findViewById(R.id.NumSubdivision);
            name_Subdivision = view.findViewById(R.id.SubdivisionName);
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

        holder.name_Subdivision.setText(_listSubdivision.list.get(position).Name);
        holder.num_Subdivision.setText( Integer.toString(_listSubdivision.list.get(position).Number));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _listSubdivision.list.size();
    }



}
