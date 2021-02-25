package com.urbandroid.sleep.captcha.motivational.quote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public interface BaseQuote {
    public static String sourceName = new String();

    public String getSource();
    public String getContent();
    public String getAuthor();

    public String displayText();

    public BaseQuote loadQuote();

    static String fetchQuoteResponse(String apiEndpoint) {
        try {
            StringBuilder fetchResponse = new StringBuilder();
            URL url = new URL(apiEndpoint);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                fetchResponse.append(line);
            }

            return fetchResponse.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
