package com.kshum.urbanandroid_captcha.motivational_quote.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.kshum.urbanandroid_captcha.motivational_quote.R;

/**
 * A layer of abstraction so we can use synchronised preferences later if we want
 */
public class Preference {
    public enum Config {
        SAVED_QUOTE_LOCATION
    }

    /**
     * @see "https://developer.android.com/training/data-storage/shared-preferences"
     */
    private final SharedPreferences sharedPref;
    private final Activity currentActivity;

    public Preference(Activity currentActivity) {
        this.currentActivity = currentActivity;
        this.sharedPref = currentActivity.getPreferences(Context.MODE_PRIVATE);
    }

    public void set(Config config, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();

        if (config == Config.SAVED_QUOTE_LOCATION) {
            editor.putString(config.toString(), value);
        }

        editor.apply();
    }

    public String get(Config config) {
        if (config == Config.SAVED_QUOTE_LOCATION) {
            String defaultValue = currentActivity.getResources().getString(R.string.settings_placeholder__pick_saved_quote_location);
            return sharedPref.getString(config.toString(), defaultValue);
        }

        return "";
    }
}
