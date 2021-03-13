package com.kshum.urbanandroid_captcha.motivational_quote.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenApiQuotes implements BaseQuote {
    @Override
    public String getSourceName() { return "api.quotable.io"; }

    @JsonProperty
    private String content = "Just do it";

    @JsonProperty
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
