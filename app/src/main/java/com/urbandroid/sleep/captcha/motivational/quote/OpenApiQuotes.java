package com.urbandroid.sleep.captcha.motivational.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenApiQuotes implements BaseQuote {
    public static String sourceName = "api.quotable.io";

    private String content = "Just do it";
    private String author = "Nike Inc.";

    @Override
    public String getSource() {
        return "https://api.quotable.io/random";
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String displayText() {
        return String.format("\"%s\" by %s", this.getContent(), this.getAuthor());
    }

    @Override
    public BaseQuote loadQuote() {
        String fetchedResponse = BaseQuote.fetchQuoteResponse(this.getSource());
        return this.responseToObject(fetchedResponse);
    }

    /**
     * Construct Quote object from response
     * @param response Response from fetch
     * @return Quote object
     */
    private OpenApiQuotes responseToObject(String response) {
        final ObjectMapper jsonMapper = new ObjectMapper();

        try {
            return jsonMapper.readValue(response, OpenApiQuotes.class);
        } catch (IOException e) {
            return new OpenApiQuotes();
        }
    }
}
