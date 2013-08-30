
package com.nazir.schedx.ui;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Todos.*;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.*;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nazir.schedx.R;
import com.nazir.schedx.persist.TodosHelper;
import com.nazir.schedx.remainder.ScheduleReceiver;

public class TodoListActivity extends MyCustomFragment
{
    public static final String TODO_BUNDLE = "com.nazir.schedx.ui.todo";
    private SimpleCursorAdapter adapter;
    private Cursor todayCursor;
    private TodosHelper helper;
    private ListView todayListView;
    private ListView tomorrowListView;
    private TextView todayField;
    private TextView tomorrowField;
    private ActionMode mActionMode;
    private ImageView todayExpandCollapseView;
    private ImageView todayAddView;
    int todoNameindx;
    
    public TodoListActivity()
    {
    }

    public void onStart()
    {
        super.onStart();
        
        todayListView = (ListView) getSherlockActivity().findViewById(R.id.today_list_view);
        tomorrowListView = (ListView) getSherlockActivity().findViewById(R.id.tomorrow_list_view);
        todayField = (TextView) getSherlockActivity().findViewById(R.id.today_field);
        tomorrowField = (TextView) getSherlockActivity().findViewById(R.id.tomorrow_field);
        todayExpandCollapseView = (ImageView) getSherlockActivity().findViewById(R.id.expand_collapse_button);
        todayAddView = (ImageView) getSherlockActivity().findViewById(R.id.today_add_image_view);
        
        todayField.setOnClickListener(new MyClickHandler());
        tomorrowField.setOnClickListener(new MyClickHandler());
        todayExpandCollapseView.setOnClickListener(new MyClickHandler());
        todayAddView.setOnClickListener(new MyClickHandler());
        
        helper = new TodosHelper(getSherlockActivity());
        todayCursor = helper.getTodos();
        todoNameindx = todayCursor.getColumnIndex(NAME);
        
        String as[] = {NAME};
        int ai[] = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(getSherlockActivity(), 
        		android.R.layout.simple_list_item_1, todayCursor, as, ai);
        todayListView.setAdapter(adapter);
        
        todayListView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				int id = todayCursor.getInt(todayCursor.getColumnIndex(ID));
				Intent intent = new Intent(getSherlockActivity(), TodoDetailActivity.class);
				intent.putExtra(ID, id);
				startActivity(intent);
				
			}
		});
       
        initActionMode();
    }

    protected void cancelAlarm(int i)
    {
        Intent intent = new Intent(getSherlockActivity(), ScheduleReceiver.class);
        intent.setAction(TodoActivity.TODO_ACTION);
        PendingIntent pendingintent = PendingIntent.getBroadcast(getSherlockActivity(), i, intent, 
        		PendingIntent.FLAG_CANCEL_CURRENT);
        ((AlarmManager)getSherlockActivity().getSystemService(Context.ALARM_SERVICE)).cancel(pendingintent);
    }

    protected void doDelete()
    {
        final int id = todayCursor.getInt(todayCursor.getColumnIndex("_id"));
        new AlertDialog.Builder(getSherlockActivity())
        .setTitle(R.string.alert_dialog_title)
        .setMessage(R.string.alert_dilog_message)
        .setIcon(R.drawable.alert)
        .setPositiveButton(R.string.alert_dialog_yes_option, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                helper.delete(id);
                cancelAlarm(id);
                adapter.notifyDataSetChanged();
                Toast.makeText(getSherlockActivity(), "Deleted", Toast.LENGTH_SHORT).show();
            }

        })
        .setNegativeButton(R.string.alert_dialog_no_option, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                dialoginterface.dismiss();
            }
        })
        .show();
    }

    protected void doEdit()
    {
        int i = todayCursor.getInt(todayCursor.getColumnIndex(ID));
        Intent intent = new Intent(getSherlockActivity(), TodoActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        Bundle bundle = new Bundle();
        bundle.putInt(ID, i);
        intent.putExtra(TODO_BUNDLE, bundle);
        startActivity(intent);
    }
    

    private void initActionMode()
    {
        final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

            public boolean onActionItemClicked(ActionMode actionmode, MenuItem menuitem)
            {
                switch(menuitem.getItemId()){
                case R.id.action_delete_item:
                	doDelete();
                    actionmode.finish();
                    break;
                case R.id.edit_schedule_action_item:
                	doEdit();
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

        }
;
        todayListView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> adapterview, View view, int i, long l)
            {
                if(mActionMode != null)
                {
                    return false;
                } else
                {
                    mActionMode = getSherlockActivity().startActionMode(mActionModeCallback);
                    return true;
                }
            }
        }
);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuinflater)
    {
        super.onCreateOptionsMenu(menu, menuinflater);
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return layoutinflater.inflate(R.layout.todo_list, null, false);
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId()){
        case R.id.action_item_new:
            startActivity(new Intent(getSherlockActivity(), TodoActivity.class));
            break;
            default: return super.onOptionsItemSelected(menuitem);
        }
        return true;
    }
    
    public void onDestroy()
    {
        super.onDestroy();
        if(todayCursor != null)
            todayCursor.close();
        if(helper != null)
            helper.disconnect();
    }
    
    private class MyClickHandler implements View.OnClickListener{

		@Override
		public void onClick(final View v) {
			
			switch(v.getId()){
			case R.id.today_field:
			case R.id.expand_collapse_button:
				toggleTodayList();
				break;
			case R.id.tomorrow_field:
				toggleTomorrowList();
				break;
			case R.id.today_add_image_view:
				startActivity(new Intent(getSherlockActivity(), TodoActivity.class));
				break;
			default: break;
			
			}
		}
    	
    }

	public void toggleTodayList() {
		
		switch(todayListView.getVisibility()){
		case ListView.GONE:
			todayListView.setVisibility(ListView.VISIBLE);
			todayExpandCollapseView.setImageResource(R.drawable.navigation_collapse);
			break;
		case ListView.VISIBLE:
			todayListView.setVisibility(ListView.GONE);
			todayExpandCollapseView.setImageResource(R.drawable.navigation_expand);
			break;
		}
	}

	public void toggleTomorrowList() {
		
		switch(tomorrowListView.getVisibility()){
		case ListView.GONE:
			tomorrowListView.setVisibility(ListView.VISIBLE);
			break;
		case ListView.VISIBLE:
			tomorrowListView.setVisibility(ListView.GONE);
			break;
		}
	}

}
