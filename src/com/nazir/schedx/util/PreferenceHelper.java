package com.nazir.schedx.util;

import com.nazir.schedx.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

public class PreferenceHelper {
	private String APP_MODE_KEY;
	private Context mContext;
	private SharedPreferences prefs;
	private Resources res;

	public PreferenceHelper(Context context) {
		mContext = context;
		res = mContext.getResources();
		APP_MODE_KEY = res.getString(R.string.app_mode_key);
		prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
	}

	public String getAppModeKey() {
		return APP_MODE_KEY;
	}

	public boolean isStudentMode() {
		String s = prefs.getString(APP_MODE_KEY, "def");
		return "def".equals(s) || s.equals("Student");
	}

}
