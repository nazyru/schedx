
package com.nazir.schedx.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.*;
import android.widget.*;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nazir.schedx.R;
import com.nazir.schedx.persist.AssessmentHelper;
import com.nazir.schedx.types.AssessmentType;
import com.nazir.schedx.util.AssessmentCursorAdapter;

import static com.nazir.schedx.persist.MySqliteOpenHelper.Assessment.*;

public class AssessmentListActivity extends MyCustomFragment
{
    public static String ASSESSMENT_BUNDLE = "com.nazir.schedx.ui.BUNDLE";
    private SimpleCursorAdapter adapter;
    private Cursor cursor;
    private Spinner filter;
    private AssessmentHelper helper;
    private ListView listView;
    private com.actionbarsherlock.view.ActionMode.Callback mActionCallback;
    private ActionMode mActionMode;


    public AssessmentListActivity()
    {
    }

    private void initActionMode()
    {
        mActionCallback = new ActionMode.Callback() {

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
        final int id = cursor.getInt(cursor.getColumnIndex(ID));
        
        new AlertDialog.Builder(getSherlockActivity())
        .setTitle(R.string.alert_dialog_title)
        .setIcon(R.drawable.alert)
        .setMessage(R.string.alert_dilog_message)
        .setPositiveButton(R.string.alert_dialog_yes_option, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                helper.delete(id);
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
        Bundle bundle = new Bundle();
        bundle.putInt(ID, cursor.getInt(cursor.getColumnIndex(ID)));
        Intent intent = new Intent(getSherlockActivity(), AssessmentActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        intent.putExtra(ASSESSMENT_BUNDLE, bundle);
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
        switch(menuitem.getItemId())
        {
        case R.id.action_item_new: 
            startActivity(new Intent(getSherlockActivity(), AssessmentActivity.class));
            break;
        default:
            return super.onOptionsItemSelected(menuitem);
        }
        return true;
    }

    public void onStart()
    {
        super.onStart();
        filter = getFilter();
        listView = getListView();
        helper = new AssessmentHelper(getSherlockActivity());
        cursor = helper.getAssessments();
        
        adapter = new AssessmentCursorAdapter(getSherlockActivity(), cursor);
        
        listView.setAdapter(adapter);
        
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> adapterview, View view, int i, long l)
            {
                if(mActionMode != null)
                {
                    return false;
                } else
                {
                    mActionMode = getSherlockActivity().startActionMode(mActionCallback);
                    return true;
                }
            }

        }
);
        final ArrayAdapter<AssessmentType> filterAdapter = new ArrayAdapter<AssessmentType>(getSherlockActivity(), 
        		android.R.layout.simple_list_item_1, AssessmentType.values());
        filter.setAdapter(filterAdapter);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterview, View view, int i, long l)
            {
                AssessmentType assessmenttype = (AssessmentType)filterAdapter.getItem(i);
                cursor = helper.getAssessmentByType(assessmenttype);
                adapter.changeCursor(cursor);
            }

            public void onNothingSelected(AdapterView<?> adapterview)
            {
            }
        }
);
        getListView().setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int id,
					long rowId) {
				
				Intent intent = new Intent(getSherlockActivity(), AssessmentDetailActivity.class);
				intent.putExtra(ID, cursor.getInt(cursor.getColumnIndex(ID)));
				startActivity(intent);
				
			}
		});
        
        initActionMode();
    }

}
