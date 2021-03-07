package com.urbandroid.sleep.captcha.motivational;

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

import com.urbandroid.sleep.captcha.CaptchaSupport;
import com.urbandroid.sleep.captcha.CaptchaSupportFactory;
import com.urbandroid.sleep.captcha.RemainingTimeListener;
import com.urbandroid.sleep.captcha.motivational.quote.OpenApiQuotes;
import com.urbandroid.sleep.captcha.motivational.quote.BaseQuote;
import com.urbandroid.sleep.captcha.motivational.quote.VeggieRootQuote;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// this is the main captcha activity
public class MotivationalCaptchaActivity extends Activity {
    private static String captchaText = "";
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private CaptchaSupport captchaSupport; // include this in every captcha
    private BaseQuote selectedQuoteInstance;

    @SuppressLint("DefaultLocale")
    private final RemainingTimeListener remainingTimeListener = (seconds, aliveTimeout) -> {
        final TextView timeoutView = findViewById(R.id.timeout);
        timeoutView.setText(String.format("%d/%s", seconds, aliveTimeout));
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        captchaSupport = CaptchaSupportFactory.create(this); // include this in every captcha, in onCreate()
        captchaSupport.setRemainingTimeListener(remainingTimeListener);
        this.initQuotesApiSourcesDropdown();
        this.showNewQuote();

        findViewById(R.id.fetch_api_button).setOnClickListener(view -> this.showNewQuote());
        findViewById(R.id.done_button).setOnClickListener(view -> this.checkCaptcha());
        ((EditText) findViewById(R.id.input_text)).addTextChangedListener(this.onUserTypeTexts);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        captchaSupport = CaptchaSupportFactory
            .create(this, intent)
            .setRemainingTimeListener(remainingTimeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final EditText input_text = findViewById(R.id.input_text);
        input_text.requestFocus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        captchaSupport.unsolved(); // .unsolved() broadcasts an intent back to AlarmAlertFullScreen that captcha was not solved
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        captchaSupport.alive(); // .alive() refreshes captcha timeout - intended to be sent on user interaction primarily, but can be called anytime anywhere
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captchaSupport.destroy();
    }

    private void initQuotesApiSourcesDropdown() {
        // TODO these arrays live in BaseQuote + Can we dynamically create new class?
        String[] quoteSources = new String[] {
            OpenApiQuotes.SOURCE_NAME,
            VeggieRootQuote.SOURCE_NAME
        };

        BaseQuote[] quoteInstances = new BaseQuote[] {
            new OpenApiQuotes(),
            new VeggieRootQuote()
        };

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, quoteSources);
        final Spinner quoteSourceDropdown = findViewById(R.id.quote_source_dropdown);
        quoteSourceDropdown.setAdapter(spinnerAdapter);

        quoteSourceDropdown.setSelection(0);
        this.selectedQuoteInstance = quoteInstances[0];

        quoteSourceDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int classIndex = adapterView.getSelectedItemPosition();
                MotivationalCaptchaActivity.this.selectedQuoteInstance = quoteInstances[classIndex];
                MotivationalCaptchaActivity.this.showNewQuote();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void showNewQuote() {
        final TextView captchaTextView = findViewById(R.id.captcha_text);

        CompletableFuture.supplyAsync(this.selectedQuoteInstance::loadQuote, executorService)
            .thenAccept(quote -> runOnUiThread(() -> {
                captchaTextView.setText(quote.displayText());
                captchaText = quote.getContent();
            }));
    }

    private void checkCaptcha() {
        final EditText input_text = findViewById(R.id.input_text);
        final TextView error_text = findViewById(R.id.error_text);

        if (input_text.getText().toString().equals(captchaText)) {
            captchaSupport.solved(); // .solved() broadcasts an intent back to Sleep as Android to let it know that captcha is solved
            finish();
        } else {
            error_text.setVisibility(View.VISIBLE);
        }
    }

    final private TextWatcher onUserTypeTexts = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            findViewById(R.id.error_text).setVisibility(View.INVISIBLE);
        }
    };
}
