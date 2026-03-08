package com.example.primera_entrega;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

public class Preferencias extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_config);
    }
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("modo_oscuro".equals(key)) {
            boolean modoOscuro = sharedPreferences.getBoolean(key, false);

            if (modoOscuro) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            requireActivity().recreate(); // refresca la pantalla actual
        }
        if ("idioma".equals(key)) {

            String codigoIdioma = sharedPreferences.getString(key, "es");

            Locale locale = new Locale(codigoIdioma);
            Locale.setDefault(locale);

            Configuration config = new Configuration();
            config.setLocale(locale);

            requireActivity().getResources().updateConfiguration(
                    config,
                    requireActivity().getResources().getDisplayMetrics()
            );

            requireActivity().finish();
            startActivity(requireActivity().getIntent());
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
