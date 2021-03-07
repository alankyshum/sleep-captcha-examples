package com.kshum.urbanandroid_captcha.motivational_quote.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshum.urbanandroid_captcha.motivational_quote.quote.BaseQuote;

public class LocalStorage implements Storage {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void saveNewQuote(BaseQuote quote) {
        // TODO storedQuoteFile = readFile(appPreferences.savedQuotesLocation);
        // TODO storedQuoteFile.append(toJSON(quote))
    }

    @Override
    public BaseQuote getQuote() {
        return null;
    }
}
