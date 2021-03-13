package com.kshum.urbanandroid_captcha.motivational_quote.setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * A layer of abstraction so we can use synchronised preferences later if we want
 */
public class Setting {
    public enum Config {
        SAVED_QUOTE_LOCATION
    }

    /**
     * @see "https://developer.android.com/training/data-storage/shared-preferences"
     */
    private final SharedPreferences sharedPref;

    public Setting(Activity currentActivity) {
        this.sharedPref = currentActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }

    public void set(Config config, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();

        if (config == Config.SAVED_QUOTE_LOCATION) {
            editor.putString(config.toString(), value);
        }

        editor.apply();
    }

    public String get(Config config) {
        return this.get(config, "");
    }

    public String get(Config config, String defaultValue) {
        if (config == Config.SAVED_QUOTE_LOCATION) {
            return sharedPref.getString(config.toString(), defaultValue);
        }

        return "";
    }
}
