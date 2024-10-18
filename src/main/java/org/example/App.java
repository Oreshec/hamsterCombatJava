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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public float getPayback() {
        // Проверяем, чтобы profitPerHour был больше нуля, иначе возможна ошибка деления на ноль
        if (profitPerHour > 0) {
            return price / profitPerHour;
        } else {
            return Float.MAX_VALUE; // Если прибыль на час 0 или отрицательная, возвращаем "бесконечность"
        }
    }

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
    private final String key;

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
        HttpResponse<JsonNode> response = Unirest.post(url).header("Content-Type", "application/json").header("User-Agent", "insomnia/10.0.0").header("Authorization", key).asJson();

        System.out.println("Status: " + response.getStatus());
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

    static void upgradeList(String key) {
        url = "https://api.hamsterkombatgame.io/interlude/upgrades-for-buy";
        JsonNode data = Request.post(url, key);
        if (data.getObject().has("upgradesForBuy")) {
            User.setUpgrades(data.getObject().getJSONArray("upgradesForBuy").toString());

            // Используем Gson для преобразования JSON в список объектов
            Gson gson = new Gson();
            Type upgradeListType = new TypeToken<List<Card>>() {
            }.getType();

            List<Card> upgradesList = gson.fromJson(User.getUpgrades(), upgradeListType);
            // Фильтрация списка карт, чтобы level не превышал maxLevel и maxLevel не был равен 0
            upgradesList = upgradesList.stream().filter(card -> card.getMaxLevel() == 0 || card.getLevel() < card.getMaxLevel()) // Пропускаем карты с maxLevel = 0
                    .collect(Collectors.toList());

            //Сортировка по окупаемости "цена / заработок"
            upgradesList.sort(Comparator.comparing((Card card) -> card.getPrice() / card.getProfitPerHour()));

            // Ограничение до 5 элементов
            List<Card> limitedList = upgradesList.size() > 5 ? upgradesList.subList(0, 5) : upgradesList;

            for (Card card : limitedList) {
                System.out.println("ID: " + card.getId());
                System.out.println("Name: " + card.getName());
                System.out.println("Available: " + card.isAvailable());
                System.out.println("Expired: " + card.isExpired());
                System.out.println("Price: " + card.getPrice());
                System.out.println("Profit per hour: " + card.getProfitPerHour());
                System.out.println("Max level: " + card.getMaxLevel());
                System.out.println("level: " + card.getLevel());
                System.out.println("Payback period: " + card.getPayback());
                System.out.println("--------");
            }
        } else {
            System.out.println("Ключ 'upgradesForBuy' не найден в ответе.");
        }
    }
}

class Json {
    static String pathFile = "conf.json";

    public static String[] readJson() {
        try {
            File file = new File(pathFile); // путь к файлу
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
            e.printStackTrace();
            return new String[0];
        }
    }
}
