package com.nazir.schedx.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.nazir.schedx.R;

public class MainActivity extends MyFragmentActivity
{
	 public void onCreate(Bundle bundle)
	    {
	        super.onCreate(bundle);
	        setContentView(R.layout.activity_main);
	        
	        ActionBar actionbar = getSupportActionBar();
	        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        
	        actionbar.addTab(actionbar.newTab().setText("Lectures")
	        		.setTabListener(new MyTabListener(this, "Lectures", LectureListActivity.class)));
	        actionbar.addTab(actionbar.newTab().setText("Assessment")
	        		.setTabListener(new MyTabListener(this, "Assessment", AssessmentListActivity.class)));
	        actionbar.addTab(actionbar.newTab().setText("TODO")
	        		.setTabListener(new MyTabListener(this, "TODO", TodoListActivity.class)));
	    }
	 
    public static class MyTabListener
        implements ActionBar.TabListener
    {

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmenttransaction)
        {
        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmenttransaction)
        {
            if(mFragment != null)
            {
                fragmenttransaction.attach(mFragment);
                return;
            } else
            {
                mFragment = Fragment.instantiate(context, clz.getName());
                fragmenttransaction.replace(R.id.main_activity_frame, mFragment, mTag);
                fragmenttransaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                return;
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmenttransaction)
        {
            if(mFragment != null)
                fragmenttransaction.detach(mFragment);
        }

        Class clz;
        Activity context;
        Fragment mFragment;
        String mTag;

        public MyTabListener(Activity activity, String s, Class class1)
        {
            context = activity;
            mTag = s;
            clz = class1;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        return true;
    }
}
