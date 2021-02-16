package com.urbandroid.sleep.captcha.example;

import android.os.AsyncTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class QuotesFetcher extends AsyncTask<String, Void, Quote> {

    @Override
    protected Quote doInBackground(String[] params) {
        StringBuilder fetchResponse = new StringBuilder();

        try {
            URL url = new URL("https://api.quotable.io/random");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                fetchResponse.append(line);
            }

            return this.responseToObject(fetchResponse.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Quote();
    }

    @Override
    protected void onPostExecute(Quote quote) {
        super.onPostExecute(quote);

        String displayText = String.format("\"%s\" by %s", quote.getContent(), quote.getAuthor());
        ReverseCaptchaActivity.captchaTextView.setText(displayText);
        ReverseCaptchaActivity.captchaText = quote.getContent();
    }

    /**
     * Construct Quote object from response
     * @param response Response from fetch
     * @return Quote object
     */
    private Quote responseToObject(String response) {
        final ObjectMapper jsonMapper = new ObjectMapper();

        try {
            return jsonMapper.readValue(response, Quote.class);
        } catch (IOException e) {
            return new Quote();
        }
    }
}
