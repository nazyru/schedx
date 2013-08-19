
package com.nazir.schedx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nazir.schedx.R;

public class MyCustomListFragment extends SherlockFragment
{
    public Spinner getFilter()
    {
        return (Spinner)getSherlockActivity().findViewById(R.id.list_header_spinner);
    }

    public ListView getListView()
    {
        return (ListView)getSherlockActivity().findViewById(R.id.list_view);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuinflater)
    {
        super.onCreateOptionsMenu(menu, menuinflater);
        menuinflater.inflate(R.menu.my_custom_list, menu);
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        super.onCreateView(layoutinflater, viewgroup, bundle);
        return layoutinflater.inflate(R.layout.custom_list_header, null, false);
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId()){
        case R.id.action_settings:
        	 startActivity(new Intent(getSherlockActivity(), SettingsActivity.class));
        	 break;
        case R.id.help_action_item:
        	 Toast.makeText(getActivity(), "Help", Toast.LENGTH_SHORT).show();
        	 break;
        default: return super.onOptionsItemSelected(menuitem);
        }
        return true;

    }
}
