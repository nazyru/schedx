
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
    private Cursor cursor;
    private TodosHelper helper;
    private ListView listView;
    private ActionMode mActionMode;
    int todoNameindx;
    public TodoListActivity()
    {
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
        listView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView adapterview, View view, int i, long l)
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
        final int id = cursor.getInt(cursor.getColumnIndex("_id"));
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
        int i = cursor.getInt(cursor.getColumnIndex("_id"));
        Intent intent = new Intent(getSherlockActivity(), TodoActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        Bundle bundle = new Bundle();
        bundle.putInt("_id", i);
        intent.putExtra(TODO_BUNDLE, bundle);
        startActivity(intent);
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
        case R.id.action_item_new:
            startActivity(new Intent(getSherlockActivity(), TodoActivity.class));
            break;
            default: return super.onOptionsItemSelected(menuitem);
        }
        return true;
    }

    public void onStart()
    {
        super.onStart();
        
        ((Spinner)getSherlockActivity().findViewById(R.id.list_header_spinner)).setVisibility(Spinner.GONE);
        listView = getListView();
        helper = new TodosHelper(getSherlockActivity());
        cursor = helper.getTodos();
        String as[] = {"name"};
        int ai[] = {R.id.todo_list_item};
        adapter = new SimpleCursorAdapter(getSherlockActivity(), R.layout.todo_list, cursor, as, ai);
        todoNameindx = cursor.getColumnIndex("name");
        listView.setAdapter(adapter);
        initActionMode();
    }

    
}
