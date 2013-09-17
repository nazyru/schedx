package com.nazir.schedx.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nazir.schedx.R;
import com.nazir.schedx.R.layout;
import com.nazir.schedx.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Assessment.*;

public class AssessmentDetailActivity extends MyFragmentActivity {
	public static String ASSESSMENT_NOTIF_FLAG = "com.nazir.schedx.ui.ASSESSMENT_FLAG";

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_assessment_detail);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		int id = getIntent().getIntExtra(ID, -1);
		Bundle detailBundle = new Bundle();
		detailBundle.putInt(ID, id);
		
		detailBundle.putBoolean(AssessmentDetailActivity.ASSESSMENT_NOTIF_FLAG, 
				getIntent().getBooleanExtra(ASSESSMENT_NOTIF_FLAG, false));
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		AssessmentDetailFragment detailFragment = new AssessmentDetailFragment();
		detailFragment.setArguments(detailBundle);
		transaction.replace(R.id.assessment_detail_container, detailFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode == KeyEvent.KEYCODE_BACK){
			goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void goBack() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		
	}
	
}
