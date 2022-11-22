package com.westas.orderassembly.ui;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static java.lang.Math.abs;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.westas.orderassembly.R;
import com.westas.orderassembly.item.ItemsAdapter;

public class RecyclerTouchListener extends ItemTouchHelper.Callback implements RecyclerView.OnItemTouchListener {

    private RecyclerView recycler_view;
    private MotionEvent motion_evant;
    private boolean swipe_back = false;
    private float touche_x;
    private float touche_y;
    View touchedView;
    @SuppressLint("ClickableViewAccessibility")
    public RecyclerTouchListener(final RecyclerView _recycler_view)
    {
        recycler_view = _recycler_view;
        recycler_view.addOnItemTouchListener(this);
        /*
        recycler_view.setOnTouchListener((v, event) -> {

            motion_evant = event;

            switch (event.getActionMasked()){
                case ACTION_DOWN:{
                    Log.i("ACTION: ","ACTION_DOWN");

                    break;
                }
                case ACTION_UP:{
                    Log.i("ACTION: ","ACTION_UP");
                    swipe_back = true;
                    break;
                }
                case ACTION_MOVE:{
                    Log.i("ACTION: ","ACTION_MOVE");
                    swipe_back = true;

                    break;
                }
                case ACTION_CANCEL:{
                    Log.i("ACTION: ","ACTION_CANCEL");
                    break;
                }
                case ACTION_POINTER_UP:{
                    Log.i("ACTION: ","ACTION_POINTER_UP");
                    break;
                }

            }

            return false;
        });
*/
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(this);
        //itemTouchhelper.attachToRecyclerView(recycler_view);


    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        return HandleTouchEvent(rv,e);

    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        HandleTouchEvent(rv, e);

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private boolean HandleTouchEvent(RecyclerView recycler_view, MotionEvent motion_evant_)
    {
        motion_evant = motion_evant_;


        switch (motion_evant.getActionMasked()){
            case ACTION_DOWN:{

                touche_x = motion_evant.getRawX();
                touche_y = motion_evant.getRawY();


                Log.i("ACTION: ","ACTION_DOWN");
                swipe_back = true;
                int[] listViewCoords = new int[2];
                recycler_view.getLocationOnScreen(listViewCoords);
                //recycler_view.getLocationInWindow(listViewCoords);


                float x = motion_evant.getX() - listViewCoords[0];
                float y = motion_evant.getY() - listViewCoords[1];

                 x = motion_evant.getX() ;
                 y = motion_evant.getY() ;


                touchedView = recycler_view.findChildViewUnder(x,y);

                Rect rect = new Rect();
                for (int i = 0; i < recycler_view.getChildCount(); i++) {
                    View child  = recycler_view.getChildAt(i);
                    child.getHitRect(rect);
                    if (rect.contains((int)x, (int)y)) {
                        //touchedView = child;
                        Log.i("View: ","View");
                        break;
                    }
                }


                break;
            }
            case ACTION_UP:{
                Log.i("ACTION: ","ACTION_UP");
                float x = motion_evant.getRawX();
                float y = motion_evant.getRawY();
                break;
            }
            case ACTION_MOVE:{
                Log.i("ACTION: ","ACTION_MOVE");







                float x = motion_evant.getRawX() ;
                float y = motion_evant.getRawY();




                Log.i("X",String.valueOf(x));
                Log.i("Y",String.valueOf(y));

                if (touchedView != null){

                    Animation(Animation.OPEN, 300);

                    /*
                    int[] listViewCoords = new int[2];
                    touchedView.getLocationOnScreen(listViewCoords);

                    View v = touchedView.findViewById(R.id.item_cardview);

                    v.setTranslationX(x - v.getWidth() +listViewCoords[0]);
                    */

                }


                return false;

            }
            case ACTION_CANCEL:{
                Log.i("ACTION: ","ACTION_CANCEL");
                break;
            }
            case ACTION_POINTER_UP:{
                Log.i("ACTION: ","ACTION_POINTER_UP");
                break;
            }

        }

        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        int swipeFlags =  ItemTouchHelper.START;
        return makeMovementFlags(0, LEFT );
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        final int position = viewHolder.getAdapterPosition();
        Log.i("Event: ","onSwiped");

    }
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        float width = (float) viewHolder.itemView.getWidth();

/*
        View itemView = viewHolder.itemView;

        RectF leftButton = new RectF(itemView.getRight() - 200, itemView.getTop(), itemView.getRight(), itemView.getBottom());

        Paint p = new Paint();
        p.setColor(Color.BLUE);
        c.drawRoundRect(leftButton, 15, 15, p);
        c.drawText("text", 5, 5, p);

*/

     /*

        getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                ((ItemsAdapter.ItemsInvoiceViewHolder)viewHolder).item_cardview,
                dX,
                dY,
                actionState,
                isCurrentlyActive
        );
        */



        if(swipe_back){
            Log.i("swipe_back","true");
            swipe_back = false;
            ((ItemsAdapter.ItemsInvoiceViewHolder)viewHolder).item_cardview.setTranslationX(-500);
        }
        else{
            Log.i("swipe_back","false");
            ((ItemsAdapter.ItemsInvoiceViewHolder)viewHolder).item_cardview.setTranslationX(dX);
        }








       // Log.i("_D_Y",String.valueOf(dY));
       // Log.i("_D_X",String.valueOf(dX));
       // Log.i("width",String.valueOf(width));

     /*   //if(abs(dX) < (width-300))

        viewHolder.itemView.setTranslationX(dX);
*/

        //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void Animation(Animation animateType, long duration) {
        View v = touchedView.findViewById(R.id.item_cardview);
        if (animateType == Animation.OPEN) {
            ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(
                    v, View.TRANSLATION_X, -700);
            translateAnimator.setDuration(duration);
            translateAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
            translateAnimator.start();

        } else if (animateType == Animation.CLOSE) {
            ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(
                    v, View.TRANSLATION_X, 0f);
            translateAnimator.setDuration(duration);
            translateAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
            translateAnimator.start();

        }
    }
    private enum Animation {
        OPEN, CLOSE
    }
}
