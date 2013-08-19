

package com.nazir.schedx.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;
import com.nazir.schedx.persist.LecturesHelper;
import com.nazir.schedx.types.Day;

public class LectureGridView extends MyCustomActivity
{
    private String courseCode;
    private int courseIndx;
    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback;
    private Long startTime;
    private int startTimeIndx;
    private String venue;
    private int venueIndx;
    
    private class MyCustomOnLongClickHandler
        implements AdapterView.OnItemLongClickListener
    {

        public boolean onItemLongClick(AdapterView adapterview, View view, int i, long l)
        {
            if(mActionMode != null)
            {
                return false;
            } else
            {
                mActionMode = ((MyCustomActivity)context).startActionMode(mActionModeCallback);
                return true;
            }
        }

        private Context context;
        private Cursor cursor;
        

        public MyCustomOnLongClickHandler(Context context1, Cursor cursor1)
        {
            cursor = cursor1;
            context = context1;
        }
    }

    private void initActionMode()
    {
        mActionModeCallback = new com.actionbarsherlock.view.ActionMode.Callback() {

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
                     default: return false;
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
        Toast.makeText(this, "Deleted", 0).show();
    }

    protected void doEdit()
    {
        Toast.makeText(this, "Updated", 0).show();
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030021);
        initActionMode();
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        ListView listview = (ListView)findViewById(0x7f050061);
        ListView listview1 = (ListView)findViewById(0x7f050062);
        ListView listview2 = (ListView)findViewById(0x7f050063);
        ListView listview3 = (ListView)findViewById(0x7f050064);
        ListView listview4 = (ListView)findViewById(0x7f050065);
        ListView listview5 = (ListView)findViewById(0x7f050066);
        ListView listview6 = (ListView)findViewById(0x7f050067);
        LecturesHelper lectureshelper = new LecturesHelper(this);
        lectureshelper.addMockLectures(5);
        final Cursor mondayCursor = lectureshelper.getLectureSchedules(Day.MONDAY);
        Cursor cursor = lectureshelper.getLectureSchedules(Day.TUESDAY);
        Cursor cursor1 = lectureshelper.getLectureSchedules(Day.WEDNESDAY);
        Cursor cursor2 = lectureshelper.getLectureSchedules(Day.THURSDAY);
        Cursor cursor3 = lectureshelper.getLectureSchedules(Day.FRIDAY);
        Cursor cursor4 = lectureshelper.getLectureSchedules(Day.SATURDAY);
        Cursor cursor5 = lectureshelper.getLectureSchedules(Day.SUNDAY);
        listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                courseIndx = mondayCursor.getColumnIndex("course_code");
                startTimeIndx = mondayCursor.getColumnIndex("start_time");
                venueIndx = mondayCursor.getColumnIndex("venue");
                courseCode = mondayCursor.getString(courseIndx);
                startTime = Long.valueOf(mondayCursor.getLong(startTimeIndx));
                venue = mondayCursor.getString(venueIndx);
                Toast.makeText(LectureGridView.this, (new StringBuilder(String.valueOf(courseCode))).append(" Starting from ").append(startTime).append(" @ ").append(venue).toString(), 1).show();
            }

        }
);
        String as[] = {
            "course_code", "start_time", "venue"
        };
        int ai[] = {
            0x7f05005e, 0x7f05005f, 0x7f050060
        };
        SimpleCursorAdapter simplecursoradapter = new SimpleCursorAdapter(this, 0x7f030020, mondayCursor, as, ai);
        SimpleCursorAdapter simplecursoradapter1 = new SimpleCursorAdapter(this, 0x7f030020, cursor, as, ai);
        SimpleCursorAdapter simplecursoradapter2 = new SimpleCursorAdapter(this, 0x7f030020, cursor1, as, ai);
        SimpleCursorAdapter simplecursoradapter3 = new SimpleCursorAdapter(this, 0x7f030020, cursor2, as, ai);
        SimpleCursorAdapter simplecursoradapter4 = new SimpleCursorAdapter(this, 0x7f030020, cursor3, as, ai);
        SimpleCursorAdapter simplecursoradapter5 = new SimpleCursorAdapter(this, 0x7f030020, cursor4, as, ai);
        SimpleCursorAdapter simplecursoradapter6 = new SimpleCursorAdapter(this, 0x7f030020, cursor5, as, ai);
        listview.setAdapter(simplecursoradapter);
        listview1.setAdapter(simplecursoradapter1);
        listview2.setAdapter(simplecursoradapter2);
        listview3.setAdapter(simplecursoradapter3);
        listview4.setAdapter(simplecursoradapter4);
        listview5.setAdapter(simplecursoradapter5);
        listview6.setAdapter(simplecursoradapter6);
        listview.setOnItemLongClickListener(new MyCustomOnLongClickHandler(this, mondayCursor));
        listview1.setOnItemLongClickListener(new MyCustomOnLongClickHandler(this, cursor));
        listview2.setOnItemLongClickListener(new MyCustomOnLongClickHandler(this, cursor1));
        listview3.setOnItemLongClickListener(new MyCustomOnLongClickHandler(this, cursor2));
        listview4.setOnItemLongClickListener(new MyCustomOnLongClickHandler(this, cursor3));
        listview5.setOnItemLongClickListener(new MyCustomOnLongClickHandler(this, cursor4));
        listview6.setOnItemLongClickListener(new MyCustomOnLongClickHandler(this, cursor5));
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId()){
        case R.id.add_scedule_action_item:
        	startActivity(new Intent(this, LectureActivity.class));
        	break;
        case android.R.id.home:
        	Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;
        default: return super.onOptionsItemSelected(menuitem);
        }
      return true;
    }

}
