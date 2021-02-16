package com.urbandroid.sleep.captcha.motivational;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.urbandroid.sleep.captcha.CaptchaSupport;
import com.urbandroid.sleep.captcha.CaptchaSupportFactory;
import com.urbandroid.sleep.captcha.RemainingTimeListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// this is the main captcha activity
public class MotivationalCaptchaActivity extends Activity {
    private static String captchaText = "";
    private static final ExecutorService executorService = Executors.newFixedThreadPool(16);
    private CaptchaSupport captchaSupport; // include this in every captcha

    @SuppressLint("DefaultLocale")
    private final RemainingTimeListener remainingTimeListener = (seconds, aliveTimeout) -> {
        final TextView timeoutView = (TextView) findViewById(R.id.timeout);
        timeoutView.setText(String.format("%d/%s", seconds, aliveTimeout));
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.showNewQuote();

        captchaSupport = CaptchaSupportFactory.create(this); // include this in every captcha, in onCreate()
        captchaSupport.setRemainingTimeListener(remainingTimeListener);

        findViewById(R.id.fetch_api_button).setOnClickListener(view -> this.showNewQuote());
        findViewById(R.id.done_button).setOnClickListener(view -> this.checkCaptcha());
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
        final EditText input_text = (EditText) findViewById(R.id.input_text);
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

    private void showNewQuote() {
        final TextView captchaTextView = (TextView) findViewById(R.id.captcha_text);

        CompletableFuture.supplyAsync(QuotesFetcher::fetchQuote, executorService)
            .thenAccept(quote -> runOnUiThread(() -> {
                String displayText = String.format("\"%s\" by %s", quote.getContent(), quote.getAuthor());
                captchaTextView.setText(displayText);
                captchaText = quote.getContent();
            }));
    }

    private void checkCaptcha() {
        final EditText input_text = (EditText) findViewById(R.id.input_text);

        if (input_text.getText().toString().equals(captchaText)) {
            captchaSupport.solved(); // .solved() broadcasts an intent back to Sleep as Android to let it know that captcha is solved
            finish();
        }
    }
}
