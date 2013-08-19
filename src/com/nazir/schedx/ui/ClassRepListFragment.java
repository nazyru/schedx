
package com.nazir.schedx.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;
import com.nazir.schedx.persist.ClassRepHelper;
import com.nazir.schedx.persist.LecturesHelper;

public class ClassRepListFragment extends MyCustomListFragment
{
    public static String CLASSREP_BUNDLE = "com.nazir.schedx.ui.CLASSREP_BUNDLE";
    private Cursor cursor;
    private ActionMode mActionMode;
    private com.actionbarsherlock.view.ActionMode.Callback mCallback;


    public ClassRepListFragment()
    {
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
        int i = cursor.getInt(cursor.getColumnIndex("_id"));
        new ClassRepHelper(getSherlockActivity()).delete(i);
        Toast.makeText(getSherlockActivity(), "Deleted", Toast.LENGTH_SHORT).show();
    }

    protected void doEdit()
    {
        Bundle bundle = new Bundle();
        bundle.putString("name", cursor.getString(cursor.getColumnIndex("name")));
        bundle.putString("phone_number", cursor.getString(cursor.getColumnIndex("phone_number")));
        bundle.putString("reg_number", cursor.getString(cursor.getColumnIndex("reg_number")));
        bundle.putInt("_id", cursor.getInt(cursor.getColumnIndex("_id")));
        bundle.putString("email_address", cursor.getString(cursor.getColumnIndex("email_address")));
        Intent intent = new Intent(getSherlockActivity(), ClassRepFragment.class);
        intent.setAction("android.intent.action.EDIT");
        intent.putExtra(CLASSREP_BUNDLE, bundle);
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

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
    	switch(menuitem.getItemId()){
    	case R.id.action_item_new:
    		startActivity(new Intent(getSherlockActivity(), ClassRepFragment.class));
    		break;
    	default :
    		return super.onOptionsItemSelected(menuitem);
    	}
        
        return true;
    }

    public void onStart()
    {
        super.onStart();
        ListView listview = (ListView)getSherlockActivity().findViewById(R.id.list_view);
        ((Spinner)getSherlockActivity().findViewById(R.id.list_header_spinner))
        .setVisibility(Spinner.GONE);
        
        cursor = (new ClassRepHelper(getSherlockActivity())).getClassReps();
        listview.setAdapter(new SimpleCursorAdapter(getSherlockActivity(),
        		android.R.layout.simple_list_item_2, cursor, new String[] {
            "name", "reg_number"
        }, new int[] {
            android.R.id.text1, android.R.id.text2
        }));
        
        initActionMode();
        
        listview.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

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
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                int lectureId = getArguments().getInt("_id");
                int classRepId = cursor.getInt(cursor.getColumnIndex("_id"));
                LecturesHelper lectureshelper = new LecturesHelper(getSherlockActivity());
                
                if(lectureshelper.assignClassRep(classRepId, lectureId))
                    Toast.makeText(getSherlockActivity(), "Assigned", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getSherlockActivity(), "Not Assigned", Toast.LENGTH_SHORT).show();
                lectureshelper.disconnect();
            }

        }
);
    }
    
}
