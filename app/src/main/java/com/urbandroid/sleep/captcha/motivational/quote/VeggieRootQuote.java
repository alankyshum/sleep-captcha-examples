package com.urbandroid.sleep.captcha.motivational.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Random;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VeggieRootQuote implements BaseQuote {
    public static final String SOURCE_NAME = "菜根譚";

    private final int TOTAL_NUMBER_OF_QUOTES = 360;
    private final String AUTHOR = "《菜根譚》";
    private int currentQuoteIndex = 0;
    private String meaning = "棲守道德者，寂寞一時；依阿權勢者，淒涼萬古。達人觀物外之物，思身後之身，寧受一時之寂寞，毋取萬古之淒涼。";
    private String quote = "弄權一時 淒涼萬古";

    @Override
    public String getContent() {
        return quote;
    }

    @Override
    public String getAuthor() {
        return AUTHOR;
    }

    @Override
    public String displayText() {
        return String.format("\"%s\" (%s) - %s", this.getContent(), this.meaning, this.getAuthor());
    }

    @Override
    public BaseQuote loadQuote() {
        this.currentQuoteIndex = new Random().nextInt(TOTAL_NUMBER_OF_QUOTES);
        String fetchedResponse = BaseQuote.fetchQuoteResponse(this.getSource());
        return this.responseToObject(fetchedResponse);
    }

    /**
     * @return the source url to fetch a random quote
     */
    public String getSource() {
        String baseUrl ="https://sleepasandroid-quote-default-rtdb.firebaseio.com/veggie-root-principles.json";
        return baseUrl + "?orderBy=\"$key\"&equalTo=\"" + this.currentQuoteIndex + "\"";
    }

    /**
     * Construct Quote object from response
     * @param response Response from fetch
     * @return Quote object
     */
    private VeggieRootQuote responseToObject(String response) {
        final ObjectMapper jsonMapper = new ObjectMapper();

        try {
            JsonNode quoteNode = jsonMapper.readTree(response);
            JsonNode quoteResponseBody = quoteNode.get(String.valueOf(this.currentQuoteIndex));
            VeggieRootQuote quote = new VeggieRootQuote();
            quote.meaning = quoteResponseBody.get("meaning").textValue();
            quote.quote = quoteResponseBody.get("quote").textValue();
            return quote;
        } catch (IOException e) {
            return new VeggieRootQuote();
        }
    }
}
