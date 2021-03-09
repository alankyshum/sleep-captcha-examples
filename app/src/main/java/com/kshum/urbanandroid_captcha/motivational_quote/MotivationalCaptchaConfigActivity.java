package com.kshum.urbanandroid_captcha.motivational_quote;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.kshum.urbanandroid_captcha.motivational_quote.preference.Preference;
import com.urbandroid.sleep.captcha.CaptchaSupport;

public class MotivationalCaptchaConfigActivity extends Activity {
    private static final int CREATE_SAVED_QUOTE_FILE_INTENT_ID = 1;
    private Preference preferences;
    private CaptchaSupport captchaSupport; // include this in every captcha

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        initPreferences();

        findViewById(R.id.chooseStorageLocationButton).setOnClickListener(view -> this.selectSavedQuoteLocation());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_SAVED_QUOTE_FILE_INTENT_ID && resultCode == Activity.RESULT_OK) {
            updatePreferenceOfSavedQuoteLocation(data);
        }
    }

    private void initPreferences() {
        preferences = new Preference(this);
        String savedQuoteLocation = preferences.get(Preference.Config.SAVED_QUOTE_LOCATION);
        ((TextView) findViewById(R.id.currentStorageLocationText)).setText(savedQuoteLocation);
    }

    private void selectSavedQuoteLocation() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        getIntent().addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("plaintext/json");
        intent.putExtra(Intent.EXTRA_TITLE, "savedQuotes.json");
        startActivityForResult(intent, CREATE_SAVED_QUOTE_FILE_INTENT_ID);
    }

    private void updatePreferenceOfSavedQuoteLocation(Intent data) {
        if (data == null) return;
        Uri uri = data.getData();
        preferences.set(Preference.Config.SAVED_QUOTE_LOCATION, uri.toString());
        ((TextView) findViewById(R.id.currentStorageLocationText)).setText(uri.toString());
        // TODO if new uri is difference from appPreferences.uri, migrate file
    }
}
