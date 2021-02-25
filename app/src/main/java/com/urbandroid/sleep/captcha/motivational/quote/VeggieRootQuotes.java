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
public class VeggieRootQuotes implements BaseQuote {
    public static String sourceName = "菜根譚";

    private final String author = "菜根譚";
    private String meaning = "棲守道德者，寂寞一時；依阿權勢者，淒涼萬古。達人觀物外之物，思身後之身，寧受一時之寂寞，毋取萬古之淒涼。";
    private String quote = "弄權一時 淒涼萬古";

    @Override
    public String getSource() {
        // TODO append random index
        String baseUrl ="https://sleepasandroid-quote-default-rtdb.firebaseio.com/veggie-root-principles.json";
        return baseUrl + "?orderBy=\"$key\"&equalTo=\"1\"";
    }

    @Override
    public String getContent() {
        return quote;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String displayText() {
        return this.getContent();
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
    private VeggieRootQuotes responseToObject(String response) {
        final ObjectMapper jsonMapper = new ObjectMapper();

        try {
            // TODO response is an array, but we need GET single item from firebase
            return jsonMapper.readValue(response, VeggieRootQuotes.class);
        } catch (IOException e) {
            return new VeggieRootQuotes();
        }
    }
}
