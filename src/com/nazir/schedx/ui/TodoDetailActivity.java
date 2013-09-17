package com.nazir.schedx.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.nazir.schedx.R;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Todos.*;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

public class TodoDetailActivity extends MyFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_detail_layout);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		int id = getIntent().getIntExtra(ID, -1);
		Bundle bundle = new Bundle();
		bundle.putInt(ID, id);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		TodoDetailFragment todoFragment = new TodoDetailFragment();
		todoFragment.setArguments(bundle);
		transaction.replace(R.id.todo_detail_container, todoFragment);
		transaction.addToBackStack(null);
		transaction.commit();
		
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			goBack();
			return true;
		}
			
		return super.onKeyDown(keyCode, event);
	}
}
