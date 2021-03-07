package com.kshum.urbanandroid_captcha.motivational_quote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.kshum.urbanandroid_captcha.motivational_quote.quote.BaseQuote;
import com.kshum.urbanandroid_captcha.motivational_quote.quote.OpenApiQuotes;
import com.kshum.urbanandroid_captcha.motivational_quote.quote.VeggieRootQuote;
import com.urbandroid.sleep.captcha.CaptchaSupport;
import com.urbandroid.sleep.captcha.CaptchaSupportFactory;
import com.urbandroid.sleep.captcha.RemainingTimeListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// this is the main captcha activity
public class MotivationalCaptchaConfigActivity extends Activity {
    private CaptchaSupport captchaSupport; // include this in every captcha

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);

    //    TODO allow user to choose file save location
    //    ref: https://developer.android.com/training/data-storage/shared/documents-files#java
    }
}
