package com.apps.fraisekiwi.crypto_carnet.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;
import com.apps.fraisekiwi.crypto_carnet.DataBase.SettingsDataBase;
import com.apps.fraisekiwi.crypto_carnet.R;
import com.apps.fraisekiwi.crypto_carnet.Utils.MyMethods;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Fahirah Diarra on 4/6/18.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    Context context;
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference colorSwitch = findPreference("colorSettings");
        colorSwitch.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                SettingsDataBase settingsDataBase = new SettingsDataBase(getPreferenceScreen().getContext());
                boolean isTrue = (boolean) newValue;
                if (isTrue) {
                    settingsDataBase.putValue("colorSettings", "color");
                    Log.d("ColorSettings", "colored");
                } else {
                    settingsDataBase.putValue("colorSettings", "white");
                    Log.d("ColorSettings", "white");
                }
                return true;
            }
        });

        Preference pref = findPreference("clear_data");
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                MyMethods methods = new MyMethods(getPreferenceScreen().getContext());
                try {
                    methods.deleteAllDatabase();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getPreferenceScreen().getContext(), "Data has been Erased ", Toast.LENGTH_SHORT).show();

                return false;
            }

        });

    }
}
