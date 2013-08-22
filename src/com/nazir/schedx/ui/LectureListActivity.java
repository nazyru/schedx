package com.nazir.schedx.ui;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.*;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nazir.schedx.R;
import com.nazir.schedx.model.Lecture;
import com.nazir.schedx.persist.LecturesHelper;
import com.nazir.schedx.remainder.ScheduleReceiver;
import com.nazir.schedx.types.Day;
import com.nazir.schedx.types.Status;
import com.nazir.schedx.util.MyCustomCursorAdapter;
import java.util.Calendar;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Lectures.*;

public class LectureListActivity extends MyCustomListFragment
{

    public static String LECTURE_BUNDLE = "com.nazir.schedx.ui.LECTURE";
    private Cursor cursor;
    private MyCustomCursorAdapter cusAdapter;
    private Spinner filter;
    private LecturesHelper helper;
    private ListView listView;
    private ActionMode mActionMode;
    private ActionMode.Callback mCallback;

    public void onStart()
    {
        super.onStart();
        initActionMode();
        helper = new LecturesHelper(getSherlockActivity());
        listView = getListView();
        filter = getFilter();
        
        final ArrayAdapter<Day> adapter = new ArrayAdapter(getSherlockActivity(), 
        		android.R.layout.simple_list_item_1, Day.values());
        
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapterview, View view, int i, long l)
            {
                Day day = (Day)adapter.getItem(i);
                cursor = helper.getLectureSchedules(day);
                cusAdapter.swapCursor(cursor);
            }

            public void onNothingSelected(AdapterView adapterview)
            {
            }
        });
        
        filter.setAdapter(adapter);
        cursor = helper.getLectureSchedules();
        
        String from[] = {COURSE_ID, START_TIME, END_TIME, VENUE};
        
        int to[] = {
            R.id.custom_adapter_course_code_view, R.id.custom_adapter_starting_time_view,
            R.id.custom_adapter_end_time_view, R.id.custom_adapter_venue_view };
        
        cusAdapter = new MyCustomCursorAdapter(getSherlockActivity(), cursor, from, to);
        listView.setAdapter(cusAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView adapterview, View view, int i, long l)
            {
                if(mActionMode != null)
                {
                    return false;
                } else
                {
                    mActionMode = getSherlockActivity().startActionMode(mCallback);
                    return true;
                }
            }

            
        }
);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterview, View view, int i, long l)
            {
                
                Intent intent = new Intent(getSherlockActivity(), LectureDetailActivity.class);
                intent.putExtra(_ID, cursor.getInt(cursor.getColumnIndex(_ID)));
                startActivity(intent);
            }    
        }
        		);
    }
    
    private void doAssignClassRep()
    {
        FragmentTransaction fragmenttransaction = getFragmentManager().beginTransaction();
        ClassRepListFragment classreplistfragment = new ClassRepListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("_id", cursor.getInt(cursor.getColumnIndex("_id")));
        classreplistfragment.setArguments(bundle);
        fragmenttransaction.replace(R.id.main_activity_frame, classreplistfragment);
        fragmenttransaction.addToBackStack(null);
        fragmenttransaction.commit();
    }

    private void doDelete()
    {
        int i = cursor.getInt(cursor.getColumnIndex("_id"));
        (new LecturesHelper(getSherlockActivity())).delete(i);
        Toast.makeText(getSherlockActivity(), "Deleted", 0).show();
        Intent intent = new Intent(getSherlockActivity(), ScheduleReceiver.class);
        intent.setAction(LectureActivity.LECTURE_ACTION);
        PendingIntent pendingintent = PendingIntent.getBroadcast(getSherlockActivity(), i, intent, 
        		PendingIntent.FLAG_CANCEL_CURRENT);
        
        ((AlarmManager)getSherlockActivity().getSystemService(Context.ALARM_SERVICE))
        .cancel(pendingintent);
    }

    private void doEdit()
    {
        Bundle bundle = new Bundle();
        bundle.putInt("_id", cursor.getInt(cursor.getColumnIndex("_id")));
        Intent intent = new Intent(getSherlockActivity(), LectureActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.putExtra(LECTURE_BUNDLE, bundle);
        startActivity(intent);
    }

    private Day getTodaysPosition()
    {
        switch(Calendar.getInstance().get(7))
        {
        default:
            return Day.CHOOSE;

        case 2: // '\002'
            return Day.MONDAY;

        case 3: // '\003'
            return Day.TUESDAY;

        case 4: // '\004'
            return Day.WEDNESDAY;

        case 5: // '\005'
            return Day.THURSDAY;

        case 6: // '\006'
            return Day.FRIDAY;

        case 7: // '\007'
            return Day.SATURDAY;

        case 1: // '\001'
            return Day.SUNDAY;
        }
    }

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
                case R.id.assign_class_rep_item:
                	doAssignClassRep();
                    actionmode.finish();
                    break;
                case R.id.lecture_status:
                	 manageStatus();
                     actionmode.finish();
                     break;
                default:  return false;
                }
                
                return true;

            }

            public boolean onCreateActionMode(ActionMode actionmode, Menu menu)
            {
                actionmode.getMenuInflater().inflate(R.menu.lecture_context_menu, menu);
                return true;
            }

            public void onDestroyActionMode(ActionMode actionmode)
            {
                if(mActionMode != null)
                    mActionMode = null;
            }

            public boolean onPrepareActionMode(ActionMode actionmode, Menu menu)
            {
                return false;
            }

        };
    }

    private void manageStatus()
    {   
        final int id = cursor.getInt(cursor.getColumnIndex(_ID));
        LecturesHelper helper = new LecturesHelper(getSherlockActivity());
		final Lecture lecture = helper.getLectureSchedlule(id);
		
		Log.i("---Status---", lecture.getStatus() != null? lecture.getStatus().getDescription() + " "+ lecture.getId(): "NULL");
        new AlertDialog.Builder(getSherlockActivity())
        .setTitle(R.string.lecture_status_title)
        .setSingleChoiceItems(new String[]{Status.ONGOING.toString(), Status.FINISHED.toString()},
        		lecture.getStatus() != null ? lecture.getStatus().ordinal(): 0, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 Status status;
				 
				 switch(which){
				 case 0:
					 status = Status.ONGOING;
					 break;
				 case 1:
					 status = Status.FINISHED; 
					 break;
				 default:status = Status.ONGOING;
				 }
				 
				updateStatus(status, lecture);
				dialog.dismiss();
			}
		})
        .show();
    }

    protected void updateStatus(Status status, Lecture lecture) {
    	
		if(lecture.getStatus().equals(status))
			return;
	
		Log.i("-->Status HERE--->", status.getDescription());
		
		lecture.setStatus(status);
		LecturesHelper helper = new LecturesHelper(getSherlockActivity());
		helper.update(lecture);
		
		Toast.makeText(getSherlockActivity(), "Status Changed", Toast.LENGTH_SHORT).show();
		helper.disconnect();
	}

	public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuinflater)
    {
        menuinflater.inflate(R.menu.courses_menu, menu);
        super.onCreateOptionsMenu(menu, menuinflater);
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return super.onCreateView(layoutinflater, viewgroup, bundle);
    }

    public void onDestroy()
    {
        super.onDestroy();
        if(cursor != null)
            cursor.close();
        if(helper != null)
            helper.disconnect();
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId()){
        case R.id.courses_action_item:
        	 startActivity(new Intent(getSherlockActivity(), CoursesListActivity.class));
        	 break;
        case R.id.action_item_new:
        	 startActivity(new Intent(getSherlockActivity(), LectureActivity.class));
        	 break;
        default: return super.onOptionsItemSelected(menuitem);
        }
        
        return true;

    }

    public void onResume()
    {
        super.onResume();
        filter = getFilter();
    }

    

}
