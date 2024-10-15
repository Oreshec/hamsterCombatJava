package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Инициализация и загрузка ключей из файла
        User.setAuthorization(Json.readJson());
        // Отправка запросов
        FetchData.list();
    }
}

class User {
    private static String[] authorizationKeys;

    public static void setAuthorization(String[] authorization) {
        authorizationKeys = authorization;
    }

    public static String[] getAuthorization() {
        return authorizationKeys;
    }
}

class FetchData {
    public static void list() {
        String[] keys = User.getAuthorization();
        if (keys == null || keys.length == 0) {
            System.out.println("Нет ключей авторизации.");
            return;
        }

        // Итерация по каждому ключу для отправки запросов
        for (String key : keys) {
            System.out.println(key);
            HttpResponse<JsonNode> response = Unirest.post("https://api.hamsterkombatgame.io/interlude/upgrades-for-buy")
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "insomnia/10.0.0")
                    .header("Authorization", key)
                    .asJson();

            if (response.getStatus() != 200) {
                System.out.println("Ошибка: " + response.getStatus());
            } else {
                System.out.println("Ответ: " + response.getBody().toString());
            }
        }
    }
}

class Json {
    public static String[] readJson() {
        try {
            File file = new File("conf.json"); // путь к файлу
            FileReader reader = new FileReader(file);
            Type keyListType = new TypeToken<List<Key>>() {}.getType();
            List<Key> keys = new Gson().fromJson(reader, keyListType);
            reader.close();

            // Преобразование списка объектов Key в массив строк
            if (keys != null && !keys.isEmpty()) {
                String[] keyArray = keys.stream().map(Key::getKey).toArray(String[]::new);
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


class Key {
    private String key; // Изменяем на String

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
