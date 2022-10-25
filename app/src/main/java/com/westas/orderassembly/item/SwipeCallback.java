package com.westas.orderassembly.item;


import static java.lang.Math.abs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.westas.orderassembly.R;

import java.io.Console;

public abstract class SwipeCallback extends ItemTouchHelper.Callback{

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags =  ItemTouchHelper.START;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        float width = (float) viewHolder.itemView.getWidth();

        View itemView = viewHolder.itemView;

        RectF leftButton = new RectF(itemView.getRight() - 200, itemView.getTop(), itemView.getRight(), itemView.getBottom());
/*
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        c.drawRoundRect(leftButton, 15, 15, p);
        c.drawText("text", 5, 5, p);

*/



        if(abs(dX) < (width-90)){




            getDefaultUIUtil().onDraw(
                    c,
                    recyclerView,
                    ((ItemsAdapter.ItemsInvoiceViewHolder)viewHolder).item_cardview,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
            );

        }





        /*
        Log.i("delta Y",String.valueOf(dX));
        Log.i("width",String.valueOf(width));
        if(abs(dX) < (width-300))
        viewHolder.itemView.setTranslationX(dX);
        */

       // super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

}
