package org.example;

import com.google.gson.Gson;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;

import java.sql.Timestamp;
import java.util.List;

// Для выполнения запросов
class Request {
    private final String key;

    Request(String key) {
        this.key = key;
    }

    // Выполнение POST запросов с опциональным телом
    private JsonNode __post(String url, String body) {
        try {
            HttpResponse<JsonNode> response = Unirest.post(url).header("Content-Type", "application/json").header("User-Agent", "insomnia/10.0.0").header("Authorization", key).body(body != null ? body : "").asJson();

            System.out.println("Status: " + response.getStatus() + " for " + url);

            if (response.getStatus() >= 200 && response.getStatus() < 300) {
                return response.getBody();
            } else {
                System.out.println("Non-success status code: " + response.getStatus() + " for URL: " + url);
                return null; // Return null on error
            }
        } catch (Exception e) {
            System.out.println("Request failed for URL: " + url + e);
            return null; // Return null on failure
        }
    }

    // Overload without a body
    private JsonNode __post(String url) {
        return __post(url, null);
    }

    // Получение списка карт
    public JsonNode cardListRequest() {
        String url = "https://api.hamsterkombatgame.io/interlude/upgrades-for-buy";
        System.out.println("Сбор информации по картам: " + url);
        return __post(url);
    }

    // Синхронизация данных
    public JsonNode sync() {
        String url = "https://api.hamsterkombatgame.io/interlude/sync";
        System.out.println("Sync: " + url);
        return __post(url);
    }

    // Внутренний класс для данных о карте
    static class __ResponseCard {
        public final String upgradeId;
        public final long timestamp;

        public __ResponseCard(String id, long time) {
            this.upgradeId = id;
            this.timestamp = time;
        }
    }

    // Покупка карты
    public void UpgradeCard(List<Card> data) {
        String url = "https://api.hamsterkombatgame.io/interlude/buy-upgrade";
        Gson gson = new Gson();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("===CARD===");
        int i = 0;
        for (Card card : data) {
            i++;
            System.out.print(i + ". ");
            if (card.getCooldownSeconds() == 0) {
                System.out.println("Cooldown on the " + card.getName() + " is missing.");
                if (card.isAvailable() && !card.isExpired()) {
                    if (User.getBalanceDiamonds() > card.getPrice()) {
                        __ResponseCard resp = new __ResponseCard(card.getId(), timestamp.getTime());
                        String json = gson.toJson(resp);

                        System.out.println("JSON for card upgrade: " + json);

                        JsonNode response = new Request(this.key).__post(url, json);
                        if (response != null) {
                            System.out.println("upgrade: " + card.getName());
                        } else {
                            System.out.println("Failed to upgrade card with ID: \"" + card.getName() + "\"");
                        }
                    } else {
                        System.out.println("There are not enough funds to purchase a card: \"" + card.getName() + "\"." + "\nThere are " + User.getBalanceDiamonds() + " diamonds now ");
                    }
                } else {
                    System.out.println("The card \"" + card.getName() + "\" is unavailable or has expired");
                }
            } else {
                System.out.println("The \"" + card.getName() + "\" is currently on cooldown. Time remaining: " + card.getCooldownSeconds() + ".");
            }
        }
    }
}
