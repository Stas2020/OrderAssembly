package com.westas.orderassembly.operations;

import android.view.View;

public class Operation {
    private View.OnClickListener on_click;
    private String caption;

    public void SetCaption(String _caption)
    {
        caption = _caption;
    }
    String GetCaption()
    {
        return caption;
    }
    public void SetOnClick(View.OnClickListener _on_click)
    {
        on_click = _on_click;
    }

    public View.OnClickListener GetOnClickListener()
    {
        return on_click;
    }


}
