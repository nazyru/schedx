
package com.nazir.schedx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;

public class MyCustomListActivity extends SherlockListActivity
{

    public MyCustomListActivity()
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.my_custom_list, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId()){
        case R.id.action_settings:
        	startActivity(new Intent(this, SettingsActivity.class));
        	break;
        case R.id.help_action_item:
        	Toast.makeText(this, "Help", 0).show();
        	break;
        default: return super.onOptionsItemSelected(menuitem);
        		
        }
       
        return true;
    }
}
