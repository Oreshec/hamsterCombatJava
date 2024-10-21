package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import kong.unirest.core.JsonNode;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Получение данных
class FetchData {
    static String url;

    public static void sync(String key) {
        url = "https://api.hamsterkombatgame.io/interlude/sync";
        System.out.println("Sync: " + url);
        JsonNode data = Request.post(url, key);
        // Поиск разной информации
        if (data.getObject().has("interludeUser")) {
            JSONObject interludeUser = data.getObject().getJSONObject("interludeUser");

            // Получение количества средств
            if (interludeUser.has("balanceDiamonds")) {
                User.setBalanceDiamonds(interludeUser.getFloat("balanceDiamonds"));
                System.out.println("Balance Diamonds: " + User.getBalanceDiamonds());
            } else {
                System.out.println("Ключ 'balanceDiamonds' не найден в ответе.");
            }

            // Получение userID
            if (interludeUser.has("id")) {
                User.setId(interludeUser.getString("id"));
                System.out.println("ID: " + User.getId());
            } else {
                System.out.println("Ключ 'id' не найден в ответе.");
            }

            // Получение заработка в секунду
            if (interludeUser.has("earnPassivePerSec")) {
                User.setEarnPassivePerSec(interludeUser.getFloat("earnPassivePerSec"));
                System.out.println("earnPassivePerSec: " + User.getEarnPassivePerSec());
            } else {
                System.out.println("Ключ 'earnPassivePerSec' не найден в ответе.");
            }
            System.out.println("\n");

        } else {
            System.out.println("Ключ 'interludeUser' не найден в ответе.");
        }
    }

    // Информация по картам
    static List<Card> cardList(String key) {
        try {
            url = "https://api.hamsterkombatgame.io/interlude/upgrades-for-buy";
            System.out.println("Сбор информации по картам: " + url);
            JsonNode data = Request.post(url, key);
            if (data.getObject().has("upgradesForBuy")) {
                JSONArray upgradeArr = data.getObject().getJSONArray("upgradesForBuy");
                System.out.println("upgradeArr: " + upgradeArr);
                // Преобразования JSON в список объектов
                Gson gson = new Gson();
                Type upgradeListType = new TypeToken<List<Card>>() {
                }.getType();

                List<Card> cardList = gson.fromJson(upgradeArr.toString(), upgradeListType);
                // Фильтрация списка карт, чтобы level не превышал maxLevel и maxLevel не был равен 0
                cardList = cardList.stream()
                        .filter((Card card) -> card.getMaxLevel() == 0 || card.getLevel() < card.getMaxLevel()) // Пропускаем карты с maxLevel = 0
                        .collect(Collectors.toList());

                //Сортировка по окупаемости "цена / заработок"
                cardList.sort(Comparator.comparing((Card card) -> card.getPrice() / card.getProfitPerHour()));
                System.out.println("cardList: " + cardList);
                // Ограничение до 5 элементов
                List<Card> limitedList = cardList.size() > 5 ? cardList.subList(0, 5) : cardList;
                System.out.println("limitedList: " + limitedList + "\n");
                for (Card card : limitedList) {
                    System.out.println("ID: " + card.getId());
                    System.out.println("Name: " + card.getName());
                    System.out.println("Available: " + card.isAvailable());
                    System.out.println("Expired: " + card.isExpired());
                    System.out.println("Price: " + card.getPrice());
                    System.out.println("Profit per hour Delta: " + card.getProfitPerHourDelta());
                    System.out.println("Max level: " + card.getMaxLevel());
                    System.out.println("Level: " + card.getLevel());
                    System.out.println("Payback period: " + card.getPayback());
                    System.out.println("--------");
                }
                return cardList;
            } else {
                System.out.println("Ключ 'upgradesForBuy' не найден в ответе.");
                return null;
            }
        } catch (JSONException | JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}