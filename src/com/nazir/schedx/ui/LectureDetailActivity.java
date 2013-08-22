package com.nazir.schedx.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.nazir.schedx.R;
import com.nazir.schedx.model.Lecture;
import com.nazir.schedx.persist.LecturesHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Lectures.*;

public class LectureDetailActivity extends MyFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture_detail);
		
		Bundle bundle = new Bundle();
		int id = getIntent().getIntExtra(_ID, -1);
		bundle.putInt(_ID, id);
		
		LecturesHelper helper = new LecturesHelper(this);
		Lecture lecture = helper.getLectureSchedlule(id);
		helper.disconnect();
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(lecture.getCourse().getCourseCode() +" Lecture");
		
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		LectureDetailFragment detailFragment = new LectureDetailFragment();
		detailFragment.setArguments(bundle);
		transaction.replace(R.id.lecture_detail_container, detailFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId()){
		case android.R.id.home:
			goBack();
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	 super.onKeyDown(keyCode, event);
	 
	 if(keyCode == KeyEvent.KEYCODE_BACK){
		 goBack();
		 return true;
	 }
	 return false;
	}

	private void goBack() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
		
}
