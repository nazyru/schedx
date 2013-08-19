
package com.nazir.schedx.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import com.nazir.schedx.util.PreferenceHelper;

public class SettingsActivity extends PreferenceActivity
    implements android.content.SharedPreferences.OnSharedPreferenceChangeListener
{

    private SharedPreferences pref;

    public SettingsActivity()
    {
    }

    private void updatePreferencesSummary()
    {
        PreferenceHelper preferencehelper = new PreferenceHelper(this);
        String s = preferencehelper.getAppModeKey();
        findPreference(s).setSummary((new StringBuilder(String.valueOf(pref.getString(s, "")))).append(" mode").toString());
        String s1 = preferencehelper.getLectureDelayKey();
        findPreference(s1).setSummary((new StringBuilder(String.valueOf(pref.getString(s1, "5")))).append(" Minutes Before Lectures").toString());
        String s2 = preferencehelper.getAssessmentDelayKey();
        findPreference(s2).setSummary((new StringBuilder(String.valueOf(pref.getString(s2, "2")))).append(" Days before Assessments").toString());
        String s3 = preferencehelper.getNotificationOnOffKey();
        Preference preference = findPreference(s3);
        StringBuilder stringbuilder = new StringBuilder("Notification ");
        String s4;
        if(pref.getBoolean(s3, true))
            s4 = "On";
        else
            s4 = "Off";
        preference.setSummary(stringbuilder.append(s4).toString());
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        addPreferencesFromResource(0x7f040000);
        pref = getPreferenceScreen().getSharedPreferences();
        updatePreferencesSummary();
    }

    protected void onPause()
    {
        super.onPause();
        pref.unregisterOnSharedPreferenceChangeListener(this);
    }

    protected void onResume()
    {
        super.onResume();
        pref.registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedpreferences, String s)
    {
        PreferenceHelper preferencehelper = new PreferenceHelper(this);
        if(s.equals(preferencehelper.getAppModeKey()))
        {
            findPreference(preferencehelper.getAppModeKey()).setSummary((new StringBuilder(String.valueOf(sharedpreferences.getString(s, "")))).append(" mode").toString());
        } else
        {
            if(s.equals(preferencehelper.getLectureDelayKey()))
            {
                findPreference(s).setSummary((new StringBuilder(String.valueOf(sharedpreferences.getString(s, "5")))).append(" Minutes Before Lectures").toString());
                return;
            }
            if(s.equals(preferencehelper.getAssessmentDelayKey()))
            {
                findPreference(s).setSummary((new StringBuilder(String.valueOf(sharedpreferences.getString(s, "2")))).append(" Days before Assessments").toString());
                return;
            }
            if(s.equals(preferencehelper.getNotificationOnOffKey()))
            {
                Preference preference = findPreference(s);
                StringBuilder stringbuilder = new StringBuilder("Notification ");
                String s1;
                if(sharedpreferences.getBoolean(s, true))
                    s1 = "On";
                else
                    s1 = "Off";
                preference.setSummary(stringbuilder.append(s1).toString());
                return;
            }
        }
    }

    
}
