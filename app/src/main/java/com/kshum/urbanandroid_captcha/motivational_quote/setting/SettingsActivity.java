package com.kshum.urbanandroid_captcha.motivational_quote.setting;

 import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

 import com.kshum.urbanandroid_captcha.motivational_quote.R;

public class SettingsActivity extends AppCompatActivity {
    private static final int CREATE_SAVED_QUOTE_FILE_INTENT_ID = 1;
    private static Setting settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = new Setting(this);
        setPreferenceContentView(savedInstanceState);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        Preference saveQuoteLocationPref;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            saveQuoteLocationPref = findPreference("saved_quote_location");
            assert saveQuoteLocationPref != null;
            String userPrefSavedQuoteLocation = settings.get(Setting.Config.SAVED_QUOTE_LOCATION, "");
            saveQuoteLocationPref.setSummary(userPrefSavedQuoteLocation);
            saveQuoteLocationPref.setOnPreferenceClickListener(preference -> intentToDocumentPicker());
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CREATE_SAVED_QUOTE_FILE_INTENT_ID && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                settings.set(Setting.Config.SAVED_QUOTE_LOCATION, uri.toString());
                saveQuoteLocationPref.setSummary(uri.toString());
            }
        }

        private boolean intentToDocumentPicker() {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("plaintext/json");
            intent.putExtra(Intent.EXTRA_TITLE, "savedQuotes.json");
            startActivityForResult(intent, CREATE_SAVED_QUOTE_FILE_INTENT_ID);
            return false;
        }
    }

    private void setPreferenceContentView(Bundle savedInstanceState) {
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}