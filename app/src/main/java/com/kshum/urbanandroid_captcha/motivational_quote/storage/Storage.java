package com.kshum.urbanandroid_captcha.motivational_quote.storage;

import com.kshum.urbanandroid_captcha.motivational_quote.quote.BaseQuote;

public interface Storage {
    public void saveNewQuote(BaseQuote quote);
    public BaseQuote getQuote();
}
