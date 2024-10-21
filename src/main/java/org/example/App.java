package org.example;

public class App {
    public static void main(String[] args) {
        // Инициализация и загрузка ключей из файла
        User.setAuthorization(ReadKeyJson.readJson("conf.json"));
        // Отправка запросов
        for (String key : User.getAuthorization()) {
            System.out.println("\n");
            FetchData.sync(key);
            FetchData.cardList(key);
        }
    }
}

