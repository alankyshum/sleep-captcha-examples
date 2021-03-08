package com.kshum.urbanandroid_captcha.motivational_quote.preference;

public class Preference {
    public static enum Config {
        SAVED_QUOTE_LOCATION
    }

    public void set(Config config, String value) {
        if (config == Config.SAVED_QUOTE_LOCATION) {
        //    TODO save to app DB
        }
    }

    public String get(Config config) {
        // TODO read from app database
        return config.toString();
    }
}
