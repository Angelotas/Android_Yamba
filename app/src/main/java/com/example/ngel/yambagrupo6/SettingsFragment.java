package com.example.ngel.yambagrupo6;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment {

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onStart() {
        /*Arranca las preferencias y obtiene las preferencias compartidas actuales*/
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

}
