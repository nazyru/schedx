

package com.nazir.schedx.ui;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

public class TabSwitcher
    implements com.actionbarsherlock.app.ActionBar.TabListener
{

    public TabSwitcher(Context context1)
    {
        context = context1;
    }

    public void onTabReselected(com.actionbarsherlock.app.ActionBar.Tab tab, FragmentTransaction fragmenttransaction)
    {
    }

    public void onTabSelected(com.actionbarsherlock.app.ActionBar.Tab tab, FragmentTransaction fragmenttransaction)
    {
        Toast.makeText(context, "Tab Selected", 0).show();
    }

    public void onTabUnselected(com.actionbarsherlock.app.ActionBar.Tab tab, FragmentTransaction fragmenttransaction)
    {
        Toast.makeText(context, "Tab UnSelected", 0).show();
    }

    private Context context;
}
