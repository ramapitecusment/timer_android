package com.example.cooltimer;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_xml);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        int count = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < count; i++){
            Preference preference = preferenceScreen.getPreference(i);

            if (preference instanceof ListPreference){
                String value = sharedPreferences.getString("timer_melody", "bell");
                setPreferenceText(preference, value);
            }else if(preference instanceof EditTextPreference){
                String value = sharedPreferences.getString("timer_interval", "30");
                setPreferenceText(preference, value);
            }
        }

        Preference preference = findPreference("timer_interval");
        preference.setOnPreferenceChangeListener(this);
    }

    private void setPreferenceText(Preference preference, String value){
        if (preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if (index>=0){
                listPreference.setSummary(listPreference.getEntries()[index]);
            }
        }

        if (preference instanceof EditTextPreference){
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            editTextPreference.setSummary(value);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);
        if (!(preference instanceof CheckBoxPreference)){
            String value = sharedPreferences.getString(preference.getKey(), "" );
            setPreferenceText(preference, value);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast toast = Toast.makeText(getContext(), "Please enter an integer number!", Toast.LENGTH_SHORT);
        try {
            if (preference.getKey().equals("timer_interval")){
                String defaultInterval = (String) newValue;
                Integer.parseInt(defaultInterval);
            }
        } catch (NumberFormatException nfe){
            toast.show();;
            return false;
        }
        return true;
    }
}
