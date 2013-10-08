
package com.nazir.schedx.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;

import com.nazir.schedx.R;
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
        findPreference(s).setSummary((new StringBuilder(String.valueOf(pref.getString(s, ""))))
        		.append(" mode").toString());
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.settings);
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

    public void onSharedPreferenceChanged(SharedPreferences sharedpreferences, String key)
    {
        PreferenceHelper preferencehelper = new PreferenceHelper(this);
        if(key.equals(preferencehelper.getAppModeKey()))
        {
            findPreference(preferencehelper.getAppModeKey()).setSummary((
            		new StringBuilder(String.valueOf(sharedpreferences.getString(key, ""))))
            		.append(" mode").toString());
        }
        
    }  
}
