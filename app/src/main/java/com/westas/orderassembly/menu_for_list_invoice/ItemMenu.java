package com.westas.orderassembly.menu_for_list_invoice;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.MenuItem;

public class ItemMenu {
    private String caption;
    private OnSelectItemMenu on_select;
    private Drawable icon;

    public void SetIcon(@NonNull Drawable value)
    {
        icon = value;
    }
    public Drawable GetIcon()
    {
        return icon;
    }
    public void SetCaption(String value)
    {
        caption = value;
    }

    public String GetCaption()
    {
        return caption;
    }
    public void SetOnClickListren(OnSelectItemMenu value)
    {
        on_select = value;
    }
    public void OnClick()
    {
        if(on_select != null)
        {
            on_select.OnSelect();
        }
    }
}
