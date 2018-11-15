package com.apps.fraisekiwi.crypto_carnet.Pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.apps.fraisekiwi.crypto_carnet.Fragments.SettingsFragment;

/**
 * Created by Fahirah Diarra on 4/13/18.
 */

// On lance les settings ici.
public class PageSettings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}

