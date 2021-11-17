package com.westas.orderassembly.menu_helper;



import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

public class MenuHelper{

    private android.view.Menu menu;

    public MenuHelper(Menu _menu)
    {
        menu = _menu;
    }
    public void Add(String caption, @NonNull Drawable icon, MenuItem.OnMenuItemClickListener on_click)
    {
        MenuItem item = menu.add(caption);
        item.setIcon(icon);
        item.setOnMenuItemClickListener(on_click);
    }

}
