package com.nazir.schedx.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

public class PreferenceHelper
{
	 private String APP_MODE_KEY;
	    private String ASSESSMENT_NOTIFICATION_DELAY_KEY;
	    private String LECTURE_NOTIFICATION_DELAY_KEY;
	    private String NOTIFICATION_ON_OFF_KEY;
	    private String TAG = "PREFERENCE_HELPER";
	    private Context mContext;
	    private SharedPreferences prefs;
	    private Resources res;

    public PreferenceHelper(Context context)
    {
        
        mContext = context;
        res = mContext.getResources();
        APP_MODE_KEY = res.getString(0x7f0a0043);
        NOTIFICATION_ON_OFF_KEY = res.getString(0x7f0a0048);
        ASSESSMENT_NOTIFICATION_DELAY_KEY = res.getString(0x7f0a0050);
        LECTURE_NOTIFICATION_DELAY_KEY = res.getString(0x7f0a004d);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public String getAppModeKey()
    {
        return APP_MODE_KEY;
    }

    public long getAssessmentDelay()
    {
        String s = prefs.getString(ASSESSMENT_NOTIFICATION_DELAY_KEY, "-1");
        
       return 0L;
    }

    public String getAssessmentDelayKey()
    {
        return ASSESSMENT_NOTIFICATION_DELAY_KEY;
    }

    public long getLectureDelay()
    {
        return 0L;
    }

    public String getLectureDelayKey()
    {
        return LECTURE_NOTIFICATION_DELAY_KEY;
    }

    public String getNotificationOnOffKey()
    {
        return NOTIFICATION_ON_OFF_KEY;
    }

    public boolean isNotificationAllowed()
    {
        return prefs.getBoolean(NOTIFICATION_ON_OFF_KEY, true);
    }

    public boolean isStudentMode()
    {
        String s = prefs.getString(APP_MODE_KEY, "def");
        return "def".equals(s) || s.equals("Student");
    }

   
}
