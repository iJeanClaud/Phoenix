package com.jcdev;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TelegramAPI {
    private final String endpoint = "https://api.telegram.org/";
    private final String token;

    public TelegramAPI(String token) {
        this.token = token;
    }

    public HttpResponse sendMessage(Integer chatId, String text) throws UnirestException {
        return Unirest.post(endpoint + "bot" + token + "/sendMessage")
                .field("chat_id", chatId)
                .field("text", text)
                .asJson();
    }

    public HttpResponse getUpdates(Integer offset) throws UnirestException {
        return Unirest.post(endpoint + "bot" + token + "/getUpdates")
                .field("offset", offset)
                .asJson();
    }
}