package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Инициализация и загрузка ключей из файла
        User.setAuthorization(Json.readJson());
        // Отправка запросов
        for (String key : User.getAuthorization()) {
            System.out.println("\n");
            FetchData.sync(key);
            FetchData.upgradeList(key);
        }
    }
}

class Card {
    private String id;
    private String name;
    private float price;
    private float profitPerHour;
    private int cooldownSeconds;
    private String section;
    private int level;
    private int maxLevel;
    private float currentProfitPerHour;
    private float profitPerHourDelta;
    private boolean isAvailable;
    private boolean isExpired;
    private int totalCooldownSeconds;

    // Геттеры и сеттеры для каждого поля

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getProfitPerHour() {
        return profitPerHour;
    }

    public void setProfitPerHour(float profitPerHour) {
        this.profitPerHour = profitPerHour;
    }

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    public void setCooldownSeconds(int cooldownSeconds) {
        this.cooldownSeconds = cooldownSeconds;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public float getCurrentProfitPerHour() {
        return currentProfitPerHour;
    }

    public void setCurrentProfitPerHour(float currentProfitPerHour) {
        this.currentProfitPerHour = currentProfitPerHour;
    }

    public float getProfitPerHourDelta() {
        return profitPerHourDelta;
    }

    public void setProfitPerHourDelta(float profitPerHourDelta) {
        this.profitPerHourDelta = profitPerHourDelta;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public int getTotalCooldownSeconds() {
        return totalCooldownSeconds;
    }

    public void setTotalCooldownSeconds(int totalCooldownSeconds) {
        this.totalCooldownSeconds = totalCooldownSeconds;
    }
}


class Key {
    private final String key; // Изменяем на String

    Key(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}


class User {
    private static String[] authorizationKeys;
    private static String upgrades;
    private static float balanceDiamonds;
    private static String id;
    private static float earnPassivePerSec;

    public static void setId(String id) {
        User.id = id;
    }

    public static String getId() {
        return id;
    }

    public static float getBalanceDiamonds() {
        return balanceDiamonds;
    }

    public static void setBalanceDiamonds(float balanceDiamonds) {
        User.balanceDiamonds = balanceDiamonds;
    }

    public static void setUpgrades(String upgrades) {
        User.upgrades = upgrades;
    }

    public static String getUpgrades() {
        return upgrades;
    }

    public static void setAuthorization(String[] authorization) {
        authorizationKeys = authorization;
    }

    public static String[] getAuthorization() {
        return authorizationKeys;
    }

    public static void setEarnPassivePerSec(float earnPassivePerSec) {
        User.earnPassivePerSec = earnPassivePerSec;
    }

    public static float getEarnPassivePerSec() {
        return earnPassivePerSec;
    }
}

class Request {
    static JsonNode post(String url, String key) {
        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("Content-Type", "application/json")
                .header("User-Agent", "insomnia/10.0.0")
                .header("Authorization", key)
                .asJson();

        if (response.getStatus() != 200) {
            System.out.println("Ошибка: " + response.getStatus());
        }
        return response.getBody();
    }
}

class FetchData {
    static String url;

    static void sync(String key) {
        url = "https://api.hamsterkombatgame.io/interlude/sync";
        JsonNode data = Request.post(url, key);

        // Поиск разной инфы
        if (data.getObject().has("interludeUser")) {
            JSONObject interludeUser = data.getObject().getJSONObject("interludeUser");

            // Получение количества средств
            if (interludeUser.has("balanceDiamonds")) {
                User.setBalanceDiamonds(interludeUser.getFloat("balanceDiamonds"));
                System.out.println("Balance Diamonds: " + User.getBalanceDiamonds());
            } else {
                System.out.println("Ключ 'balanceDiamonds' не найден в ответе.");
            }
            // Получение ID
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

        } else {
            System.out.println("Ключ 'interludeUser' не найден в ответе.");
        }
    }

    static void upgradeList(String key) {
        // Итерация по каждому ключу для отправки запросов
        url = "https://api.hamsterkombatgame.io/interlude/upgrades-for-buy";
        JsonNode data = Request.post(url, key);
        if (data.getObject().has("upgradesForBuy")) {
            User.setUpgrades(data.getObject().getJSONArray("upgradesForBuy").toString());

            // Используем Gson для преобразования JSON в список объектов
            Gson gson = new Gson();
            Type upgradeListType = new TypeToken<List<Card>>() {
            }.getType();

            List<Card> upgradesList = gson.fromJson(User.getUpgrades(), upgradeListType);

            // Теперь вы можете работать с объектами Upgrade
            for (Card card : upgradesList) {
                System.out.println("ID: " + card.getId());
                System.out.println("Name: " + card.getName());
                System.out.println("Price: " + card.getPrice());
                System.out.println("Profit per hour: " + card.getProfitPerHour());
                System.out.println("Max level: " + card.getMaxLevel());
                System.out.println("--------");
            }
        } else {
            System.out.println("Ключ 'upgradesForBuy' не найден в ответе.");
        }
    }
}

class Json {
    public static String[] readJson() {
        try {
            File file = new File("conf.json"); // путь к файлу
            FileReader reader = new FileReader(file);
            Type keyListType = new TypeToken<List<Key>>() {
            }.getType();
            List<Key> keys = new Gson().fromJson(reader, keyListType);
            reader.close();

            // Преобразование списка объектов Key в массив строк
            String[] keyArray = keys.stream().map(Key::getKey).toArray(String[]::new);
            System.out.println("keyArray: " + Arrays.toString(keyArray));
            if (!keys.isEmpty()) {
                return keyArray;
            } else {
                System.out.println("Ключи не найдены или список пуст.");
                return new String[0]; // Возвращаем пустой массив
            }
        } catch (Exception e) {
            System.out.println("Ошибка чтения JSON: " + e.getMessage());
            e.printStackTrace();
            return new String[0];
        }
    }
}
