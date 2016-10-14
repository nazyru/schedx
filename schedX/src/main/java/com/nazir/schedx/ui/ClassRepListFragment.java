
package com.nazir.schedx.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;
import com.nazir.schedx.model.ClassRep;
import com.nazir.schedx.persist.ClassRepHelper;
import com.nazir.schedx.persist.LecturesHelper;
import com.nazir.schedx.types.Flag;
import static com.nazir.schedx.persist.MySqliteOpenHelper.ClassRep.*;

public class ClassRepListFragment extends MyCustomFragment
{
    public static String CLASSREP_BUNDLE = "com.nazir.schedx.ui.CLASSREP_BUNDLE";
    private Cursor cursor;
    private ActionMode mActionMode;
    private ActionMode.Callback mCallback;
    private EditText nameView;
    private EditText regNoView;
    private EditText phoneNumberView;
    private EditText emailAddressView;
    private SimpleCursorAdapter adapter;


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
                	manageClassRep(Flag.EDIT);
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
    	int id = cursor.getInt(cursor.getColumnIndex("_id"));
    	
    	LecturesHelper helper = new LecturesHelper(getSherlockActivity());
    	try{
    	helper.unassignClassRep(id);
    	}
    	catch(SQLException exception){
    		Log.w("--UNASSIGNED--", "Not Assigned "+ exception.getMessage());
    	}
    	finally{
    		helper.disconnect();
    	}
    	
        new ClassRepHelper(getSherlockActivity()).delete(id);
        Toast.makeText(getSherlockActivity(), "Deleted", Toast.LENGTH_SHORT).show();
        refreshList();
    }

    private void refreshList() {
    	ClassRepHelper helper = new ClassRepHelper(getSherlockActivity());
    	cursor = helper.getClassReps();
    	adapter.changeCursor(cursor);
    	adapter.notifyDataSetChanged();
    	helper.disconnect();
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
    		manageClassRep(Flag.NEW);
    		break;
    	default :
    		return super.onOptionsItemSelected(menuitem);
    	}
        
        return true;
    }

    private void manageClassRep(final Flag flag) {
    	
    	LayoutInflater inflater = LayoutInflater.from(getSherlockActivity());
    	final View view = inflater.inflate(R.layout.class_rep_layout,
    			null, false);
    	
    	View titleView = inflater.inflate(R.layout.add_course_title_layout, null, false);
    	TextView titleHeaderView = (TextView) titleView.findViewById(R.id.add_course_title_layout);
    	String addUpdateTitle = "Add Class Rep";
    	
    	String positiveButtonLabel = "Add";
    	
    	if(flag == Flag.EDIT){
    		populateViews(view);
    		positiveButtonLabel = "Update";
    		addUpdateTitle = "Update Class Rep";
    	}
    	
    	titleHeaderView.setText(addUpdateTitle);
    	
		new AlertDialog.Builder(getSherlockActivity())
		.setCustomTitle(titleView)
		.setView(view)
		.setPositiveButton(positiveButtonLabel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				doSave(view, flag);
				
			}
		})
		.setNegativeButton(R.string.class_rep_cancel_button_label, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		}).show();
		
	}
    
    private void doSave(View view, Flag flag)
    {
        ClassRep classrep = new ClassRep();
        ClassRepHelper classrepHelper;
        
        nameView = (EditText) view.findViewById(R.id.class_rep_name);
        regNoView = (EditText) view.findViewById(R.id.class_rep_reg_num);
        phoneNumberView = (EditText) view.findViewById(R.id.class_rep_phone_number);
        emailAddressView = (EditText) view.findViewById(R.id.class_rep_email);
        
        classrep.setName(nameView.getText().toString());
        classrep.setEmailAddress(emailAddressView.getText().toString());
        classrep.setRegNumber(regNoView.getText().toString());
        classrep.setPhoneNumber(phoneNumberView.getText().toString());
        
        classrepHelper = new ClassRepHelper(getSherlockActivity());
        
        try{
        if(flag == Flag.EDIT){
        	
        	classrep.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
            classrepHelper.update(classrep);
            Toast.makeText(getSherlockActivity(), "Updated", Toast.LENGTH_SHORT).show();
        } 
        else{
        	classrepHelper.addClassRep(classrep);
            Toast.makeText(getSherlockActivity(), "Saved", Toast.LENGTH_SHORT).show();
        }
        }catch(SQLiteException sqlExc){
        	Log.i("Class Rep Add Exception", sqlExc.getMessage());
            Toast.makeText(getSherlockActivity(), "Not Saved", Toast.LENGTH_SHORT).show();
        }
        finally{
        	refreshList();
        	classrepHelper.disconnect();
        }
        
    }
    private void populateViews(View view)
    {
    	ClassRepHelper helper = new ClassRepHelper(getSherlockActivity());
    	ClassRep classRep = helper.getClassRep(cursor.getInt(cursor.getColumnIndex(_ID)));
    	helper.disconnect();
    	
    	
        nameView = (EditText) view.findViewById(R.id.class_rep_name);
        nameView.setText(classRep.getName());
        
        regNoView = (EditText) view.findViewById(R.id.class_rep_reg_num);
        regNoView.setText(classRep.getRegNumber());
        
        phoneNumberView = (EditText) view.findViewById(R.id.class_rep_phone_number);
        phoneNumberView.setText(classRep.getPhoneNumber());
        
        emailAddressView = (EditText) view.findViewById(R.id.class_rep_email);
        emailAddressView.setText(classRep.getEmailAddress());
      
    }

	public void onStart()
    {
        super.onStart();
        ListView listview = (ListView)getSherlockActivity().findViewById(R.id.list_view);
        ((Spinner)getSherlockActivity().findViewById(R.id.list_header_spinner))
        .setVisibility(Spinner.GONE);
        
        cursor = (new ClassRepHelper(getSherlockActivity())).getClassReps();
        adapter = new SimpleCursorAdapter(getSherlockActivity(),
        		android.R.layout.simple_list_item_2, cursor, new String[] {
            NAME, REG_NO
        }, new int[] {
            android.R.id.text1, android.R.id.text2
        });
        
        listview.setAdapter(adapter);
        
        initActionMode();
        
        listview.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

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
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterview, View view, int i, long l)
            {
                int lectureId = getArguments().getInt(_ID);
                if(lectureId == -1)
                	return;
                Log.i("~~~HERE~~~", "Problem Here  ID = "+ lectureId );
                int classRepId = cursor.getInt(cursor.getColumnIndex(_ID));
                LecturesHelper lectureshelper = new LecturesHelper(getSherlockActivity());
                
                if(lectureshelper.assignClassRep(classRepId, lectureId))
                    Toast.makeText(getSherlockActivity(), "Assigned", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getSherlockActivity(), "Not Assigned", Toast.LENGTH_SHORT).show();
                
                lectureshelper.disconnect();
                getSherlockActivity().finish();
            }
        }
);
    } 
}
