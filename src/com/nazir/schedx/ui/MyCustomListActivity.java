
package com.nazir.schedx.ui;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.nazir.schedx.R;
import com.nazir.schedx.reports.ReportHelper;

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
        	Toast.makeText(this, "Comming Soon!!!", Toast.LENGTH_SHORT).show();
        	break;
        default: return super.onOptionsItemSelected(menuitem);
        		
        }
       
        return true;
    }

	
}
