package com.kshum.urbanandroid_captcha.motivational_quote.storage;

import com.kshum.urbanandroid_captcha.motivational_quote.preference.Preference;
import com.kshum.urbanandroid_captcha.motivational_quote.quote.BaseQuote;

public class Storage {
    Preference preference = new Preference();

    public void saveQuote(BaseQuote quote) {
        String savedQuoteLocation = preference.get(Preference.Config.SAVED_QUOTE_LOCATION);
    //    TODO Edit the file at the location
    //    see: https://developer.android.com/training/data-storage/shared/documents-files#edit
    }
}
