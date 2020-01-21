package com.example.hamadaelsha3r.the_movie_application;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;

import java.util.Objects;

public class SettingFregment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener  {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_setting);

          SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            int count = preferenceScreen.getPreferenceCount();
            for (int i = 0; i < count; i++) {
                Preference p = getPreferenceScreen().getPreference(i);
                String value = sharedPreferences.getString(p.getKey(), "");
                setpreferencesummary(p, value);
            }

        }

        private void setpreferencesummary(Preference preference, String value) {
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefindex = listPreference.findIndexOfValue(value);
                if (prefindex >= 0) {
                    listPreference.setSummary(listPreference.getEntries()[prefindex]);
                }
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            Preference preference = findPreference(s);
            if (null != preference) {
                if (preference instanceof ListPreference) {
                    String value = sharedPreferences.getString(preference.getKey(), "");
                    setpreferencesummary(preference, value);
                }
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getActivity()));
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onDestroy() {
            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
            super.onDestroy();
        }

}
