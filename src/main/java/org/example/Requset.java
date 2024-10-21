package org.example;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;

// Для выполнения запросов
class Request {
    // Выполнение POST запросов
    static JsonNode post(String url, String key) {
        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("Content-Type", "application/json")
                .header("User-Agent", "insomnia/10.0.0")
                .header("Authorization", key)
                .asJson();

        System.out.println("Status: " + response.getStatus());
        return response.getBody();
    }
}