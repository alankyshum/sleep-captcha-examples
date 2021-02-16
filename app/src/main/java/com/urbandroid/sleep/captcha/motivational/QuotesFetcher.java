package com.urbandroid.sleep.captcha.motivational;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class QuotesFetcher {
    public static Quote fetchQuote() {
        String fetchedResponse = QuotesFetcher.fetchQuoteResponse();
        return QuotesFetcher.responseToObject(fetchedResponse);
    }

    private static String fetchQuoteResponse() {
        try {
            StringBuilder fetchResponse = new StringBuilder();
            URL url = new URL("https://api.quotable.io/random");
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

    /**
     * Construct Quote object from response
     * @param response Response from fetch
     * @return Quote object
     */
    private static Quote responseToObject(String response) {
        final ObjectMapper jsonMapper = new ObjectMapper();

        try {
            return jsonMapper.readValue(response, Quote.class);
        } catch (IOException e) {
            return new Quote();
        }
    }
}
