package com.nazir.schedx.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;
import com.nazir.schedx.persist.CoursesHelper;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Courses.*;

public class CoursesListActivity extends MyCustomListActivity
{
    public static String COURSE_BUNDLE = "com.nazir.schedx.ui.COURSE_BUNDLE";
    private Cursor cursor;
    private ActionMode mActionMode;
    private ActionMode.Callback mCallback;

    private void initActionMode()
    {
        mCallback = new ActionMode.Callback() {

            public boolean onActionItemClicked(ActionMode actionmode, MenuItem menuitem)
            {
                switch(menuitem.getItemId()){
                case R.id.edit_schedule_action_item:
                	doEdit();
                    actionmode.finish();
                    break;
                case R.id.action_delete_item:
                	doDelete();
                    actionmode.finish();
                    break;
                default:  return false;
                }
          return true;
       }

            public boolean onCreateActionMode(ActionMode actionmode, Menu menu)
            {
                actionmode.getMenuInflater().inflate(R.menu.context_menu, menu);
                return true;
            }

            public void onDestroyActionMode(ActionMode actionmode)
            {
                mActionMode = null;
            }

            public boolean onPrepareActionMode(ActionMode actionmode, Menu menu)
            {
                return false;
            }

            
        };

    }

    protected void doDelete()
    {
    	new AlertDialog.Builder(this)
    	.setMessage(R.string.delete_course_message)
    	.setTitle(R.string.delete_course_title)
    	.setNegativeButton(R.string.delete_course_no_button, new AlertDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		})
		.setPositiveButton(R.string.delete_course_yes_button, new AlertDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				int id = cursor.getInt(cursor.getColumnIndex(_ID));
				doDeleteSchedules(id);
			}
		})
		.show();
        
    }

    protected void doDeleteSchedules(int id) {
		CoursesHelper helper = new CoursesHelper(this);
		helper.delete(id);
		Toast.makeText(CoursesListActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
		
	}

	protected void doEdit()
    {
        Bundle bundle = new Bundle();
        bundle.putInt(_ID, cursor.getInt(cursor.getColumnIndex(_ID)));
        bundle.putString("course_code", cursor.getString(cursor.getColumnIndex("course_code")));
        bundle.putString("course_unit", cursor.getString(cursor.getColumnIndex("course_unit")));
        bundle.putString("course_title", cursor.getString(cursor.getColumnIndex("course_title")));
        Intent intent = new Intent(this, CourseActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.putExtra(COURSE_BUNDLE, bundle);
        startActivity(intent);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        initActionMode();
        
        String as[] = {COURSE_CODE, COURSE_TITLE};
        int ai[] = {android.R.id.text1, android.R.id.text2};
       
        cursor = (new CoursesHelper(this)).getCourses();
        setListAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, as, ai));
        getListView().setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView adapterview, View view, int i, long l)
            {
                if(mActionMode != null)
                {
                    return false;
                } else
                {
                    mActionMode = startActionMode(mCallback);
                    return true;
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId()){
        case R.id.action_item_new:
        	startActivity(new Intent(this, CourseActivity.class));
        	finish();
        	break;
        default: return super.onOptionsItemSelected(menuitem);
        	
        }
        return true;
    }
}
