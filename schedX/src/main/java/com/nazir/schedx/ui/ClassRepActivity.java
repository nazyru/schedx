package com.nazir.schedx.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.nazir.schedx.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import static com.nazir.schedx.persist.MySqliteOpenHelper.ClassRep.*;

public class ClassRepActivity extends MyFragmentActivity
{

    public ClassRepActivity()
    {
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.class_rep_fragment);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        Bundle idBundle = new Bundle();
        idBundle.putInt(_ID, getIntent().getIntExtra(_ID, -1));
        
        FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
        ClassRepListFragment classRepFragment = new ClassRepListFragment();
        classRepFragment.setArguments(idBundle);
        
        fragmenttransaction.replace(R.id.class_rep_fragment_container, classRepFragment);
        fragmenttransaction.addToBackStack(null);
        fragmenttransaction.commit();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent){
    	
    	
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		goBack();
    		return true;
    	}
    	
    	return super.onKeyDown(keyCode, keyEvent);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		switch(item.getItemId()){
		case android.R.id.home:
			goBack();
			break;
		default: return super.onOptionsItemSelected(item);
		}
		return true;
	}

	private void goBack() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
    
}
