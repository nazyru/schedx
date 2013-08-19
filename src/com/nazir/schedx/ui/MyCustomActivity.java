
package com.nazir.schedx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;

public class MyCustomActivity extends SherlockActivity
{

    public MyCustomActivity()
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_my_custom);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.my_custom, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId()){
        case R.id.action_settings:
        	startActivity(new Intent(this, SettingsActivity.class));
        case R.id.help_action_item:
        	Toast.makeText(this, "Help", 0).show();
        	break;
        default: return super.onOptionsItemSelected(menuitem);
        }
        
        return true;

    }
}
