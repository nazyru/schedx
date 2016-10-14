package com.nazir.schedx.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nazir.schedx.R;
import com.nazir.schedx.model.Lecture;
import com.nazir.schedx.persist.LecturesHelper;
import com.nazir.schedx.remainder.AlarmHelper;
import com.nazir.schedx.types.Day;
import com.nazir.schedx.types.Status;
import com.nazir.schedx.util.MyCustomCursorAdapter;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Lectures.*;

public class LectureListActivity extends MyCustomFragment
{
    public static String LECTURE_BUNDLE = "com.nazir.schedx.ui.LECTURE";
    private Cursor cursor;
    private MyCustomCursorAdapter cusAdapter;
    private Spinner filter;
    private LecturesHelper helper;
    private ListView listView;
    private ActionMode mActionMode;
    private ActionMode.Callback mCallback;
    private ArrayAdapter<Day> daysAdapter;

    public void onStart()
    {
        super.onStart();
        initActionMode();
        helper = new LecturesHelper(getSherlockActivity());
        listView = getListView();
        filter = getFilter();
        
        daysAdapter = new ArrayAdapter<Day>(getSherlockActivity(), 
        		android.R.layout.simple_list_item_1, Day.values());
        
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterview, View view, int i, long l)
            {
                Day day = (Day)daysAdapter.getItem(i);
                cursor = helper.getLectureSchedules(day);
                cusAdapter.changeCursor(cursor);
            }

            public void onNothingSelected(AdapterView<?> adapterview)
            {
            }
        });
        
        filter.setAdapter(daysAdapter);
        cursor = helper.getLectureSchedules();
        cusAdapter = new MyCustomCursorAdapter(getSherlockActivity(), cursor);
        listView.setAdapter(cusAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> adapterview, View view, int i, long l)
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
    	Intent intent = new Intent(getSherlockActivity(), ClassRepActivity.class);
        intent.putExtra(_ID, cursor.getInt(cursor.getColumnIndex(_ID)));
        startActivity(intent);
    }

    private void doDelete()
    {
        int id = cursor.getInt(cursor.getColumnIndex(_ID));
        (new LecturesHelper(getSherlockActivity())).delete(id);
        Toast.makeText(getSherlockActivity(), "Deleted", Toast.LENGTH_SHORT).show();
        AlarmHelper.cancelAlarm(id, getSherlockActivity());
      
        //refresh list
        cursor = helper.getLectureSchedules();
        cusAdapter.changeCursor(cursor);
    }

    private void doEdit()
    {
        Bundle bundle = new Bundle();
        bundle.putInt(_ID, cursor.getInt(cursor.getColumnIndex(_ID)));
        bundle.putString(STATUS, cursor.getString(cursor.getColumnIndex(STATUS)));
        Intent intent = new Intent(getSherlockActivity(), LectureActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.putExtra(LECTURE_BUNDLE, bundle);
        startActivity(intent);
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
		
		lecture.setStatus(status);
		LecturesHelper helper = new LecturesHelper(getSherlockActivity());
		helper.update(lecture);
		
		switch(status){
		case FINISHED:
			 AlarmHelper.cancelAlarm(lecture.getId(), getSherlockActivity());
			break;
		case ONGOING:
			AlarmHelper.updateLectureRemainder(lecture, getSherlockActivity());
			break;
		default:
			break;
		}
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

}
