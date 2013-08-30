package com.nazir.schedx.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.actionbarsherlock.view.*;
import com.nazir.schedx.R;
import com.nazir.schedx.model.ClassRep;
import com.nazir.schedx.persist.ClassRepHelper;

public class ClassRepFragment extends MyCustomActivity
{
    private Bundle bundle;
    private EditText emailField;
    private EditText nameField;
    private EditText phoneField;
    private EditText regNoField;

    public ClassRepFragment()
    {
    }

    private void doSave()
    {
        ClassRep classrep;
        ClassRepHelper classrephelper;
        String action;
        classrep = new ClassRep();
        classrep.setName(nameField.getText().toString());
        classrep.setEmailAddress(emailField.getText().toString());
        classrep.setRegNumber(regNoField.getText().toString());
        classrep.setPhoneNumber(phoneField.getText().toString());
        classrephelper = new ClassRepHelper(this);
        action = getIntent().getAction();
        
        try{
        if(action != null && action.equals(Intent.ACTION_EDIT)){
        	classrep.setId(bundle.getInt("_id"));
            classrephelper.update(classrep);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        } 
        else{
        	classrephelper.addClassRep(classrep);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        }catch(SQLiteException sqlExc){
        	Log.i("Class Rep Add Excp", sqlExc.getMessage());
            Toast.makeText(this, "Not Saved", Toast.LENGTH_SHORT).show();
        }
        finally{
        classrephelper.disconnect();
        }
        
    }

    private void populateViews()
    {
        nameField.setText(bundle.getString("name"));
        regNoField.setText(bundle.getString("reg_number"));
        phoneField.setText(bundle.getString("phone_number"));
        emailField.setText(bundle.getString("email_address"));
    }

    public void onCreate(Bundle bundle1)
    {
        super.onCreate(bundle1);
        setContentView(R.layout.class_rep_layout);
        nameField = (EditText)findViewById(R.id.class_rep_name);
        regNoField = (EditText)findViewById(R.id.class_rep_reg_num);
        phoneField = (EditText)findViewById(R.id.class_rep_phone_number);
        emailField = (EditText)findViewById(R.id.class_rep_email);
        String action = getIntent().getAction();
        if(action != null && action.equals(Intent.ACTION_EDIT))
        {
            bundle = getIntent().getBundleExtra(ClassRepListFragment.CLASSREP_BUNDLE);
            populateViews();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.action_bar_lecture, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId())
        {
        default:
            return super.onOptionsItemSelected(menuitem);

        case R.id.ic_save_lecture: 
            doSave();
            break;
        }
        finish();
        return true;
    }

    
}
