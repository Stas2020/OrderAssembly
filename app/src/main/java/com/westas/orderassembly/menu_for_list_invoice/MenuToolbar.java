package com.westas.orderassembly.menu_for_list_invoice;


import android.support.annotation.DrawableRes;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.westas.orderassembly.R;

public class MenuToolbar{

    private android.view.Menu menu;

    public MenuToolbar(Menu _menu)
    {
        menu = _menu;
    }
    public void Add(ItemMenu it)
    {
        MenuItem item = menu.add(it.GetCaption());
        item.setIcon(it.GetIcon());
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                it.OnClick();
                return true;
            }
        });
    }

}
