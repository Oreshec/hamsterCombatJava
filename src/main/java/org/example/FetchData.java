package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import kong.unirest.core.JsonNode;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// Получение данных
class FetchData {
    private static final Logger LOGGER = Logger.getLogger(FetchData.class.getName());
    private final String key;

    public FetchData(String key) {
        this.key = key;
    }

    public void syncInfo() {
        try {
            Request request = new Request(this.key);
            JsonNode data = request.sync();
            JSONObject interludeUser = data.getObject()
                    .optJSONObject("interludeUser");
            fetchBalance(interludeUser);
            fetchUserId(interludeUser);
            fetchPassiveIncome(interludeUser);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ключ 'interludeUser' не найден в ответе." + e.getMessage(), e);
        }
    }
    
    
    private void fetchBalance(JSONObject interludeUser) {
        try {
            interludeUser.has("balanceDiamonds");
            User.setBalanceDiamonds(interludeUser.getFloat("balanceDiamonds"));
            System.out.println("Balance Diamonds: " + User.getBalanceDiamonds());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ключ 'balanceDiamonds' не найден в ответе." + e.getMessage(), e);
        }
    }

    private void fetchUserId(JSONObject interludeUser) {
        try {
            interludeUser.has("id");
            User.setId(interludeUser.getString("id"));
            System.out.println("ID: " + User.getId());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Ключ 'id' не найден в ответе." + e.getMessage(), e);
        }
    }

    private void fetchPassiveIncome(JSONObject interludeUser) {
        try {
            interludeUser.has("earnPassivePerSec");
            User.setEarnPassivePerSec(interludeUser.getFloat("earnPassivePerSec"));
            System.out.println("earnPassivePerSec: " + User.getEarnPassivePerSec());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Ключ 'earnPassivePerSec' не найден в ответе." + e.getMessage(), e);
        }
    }

    // Информация по картам
    public List<Card> cardList() {
        Request request = new Request(this.key);
        JsonNode data = request.cardListRequest();
        JSONArray upgradeArr = data.getObject().optJSONArray("upgradesForBuy");
        if (upgradeArr == null) {
            System.out.println("Ключ 'upgradesForBuy' не найден в ответе.");
            return List.of();  // Return an empty list instead of null
        }
        LOGGER.info("cardList upgradeArr:" + upgradeArr);
        return parseAndFilterCards(upgradeArr);
    }

    private List<Card> parseAndFilterCards(JSONArray upgradeArr) {
        Gson gson = new Gson();
        Type upgradeListType = new TypeToken<List<Card>>() {
        }.getType();

        try {
            List<Card> cardList = gson.fromJson(upgradeArr.toString(), upgradeListType);

            // Filter and sort the list
            return cardList.stream()
                    .filter(card -> card.getMaxLevel() == 0 || card.getLevel() <= card.getMaxLevel())
                    .filter(card -> card.isAvailable() & !card.isExpired())
                    .sorted(Comparator.comparing(card -> card.getPrice() / card.getProfitPerHourDelta()))
                    .limit(5)
                    .collect(Collectors.toList());
        } catch (JsonSyntaxException e) {
            LOGGER.log(Level.SEVERE, "Error parsing card list" + e.getMessage(), e);
            return List.of();  // Return empty list in case of error
        }
    }
}