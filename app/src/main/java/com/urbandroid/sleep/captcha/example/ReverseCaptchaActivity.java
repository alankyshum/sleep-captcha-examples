package com.urbandroid.sleep.captcha.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.urbandroid.sleep.captcha.CaptchaSupport;
import com.urbandroid.sleep.captcha.CaptchaSupportFactory;
import com.urbandroid.sleep.captcha.RemainingTimeListener;

import java.util.Random;

// this is the main captcha activity
public class ReverseCaptchaActivity extends Activity {

    public static String captchaText = "";
    public static TextView captchaTextView;

    private CaptchaSupport captchaSupport; // include this in every captcha

    private final RemainingTimeListener remainingTimeListener = new RemainingTimeListener() {
        @Override
        public void timeRemain(int seconds, int aliveTimeout) {
            final TextView timeoutView = (TextView) findViewById(R.id.timeout);
            timeoutView.setText(seconds + "/" + aliveTimeout);
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        captchaSupport = CaptchaSupportFactory.create(this); // include this in every captcha, in onCreate()

        // show timeout in TextView with id "timeout"
        captchaSupport.setRemainingTimeListener(remainingTimeListener);

        this.captchaTextView = (TextView) findViewById(R.id.captcha_text);
        this.captchaTextView.setText("Loading the Quote of the day");

        findViewById(R.id.fetch_api_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fetch data from api set the captcha text
                QuotesFetcher process = new QuotesFetcher();
                process.execute();
            }
        });

        final EditText input_text = (EditText) findViewById(R.id.input_text);

        // send captchaSupport.solved() when correct string entered and "Done" button tapped
        findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ReverseCaptchaText", captchaText);
                Log.i("ReverseCaptchaInput", input_text.getText().toString());
                if (input_text.getText().toString().equals(new StringBuilder(captchaText).toString())) {
                    captchaSupport.solved(); // .solved() broadcasts an intent back to Sleep as Android to let it know that captcha is solved
                    finish();
                }
            }
        });
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
}
