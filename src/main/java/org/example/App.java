package org.example;

import java.util.List;

public class App {
    static boolean PRINT_INFO = false;


    public static void main(String[] args) {

        // Инициализация и загрузка ключей из файла
        User.setAuthorization(ReadKeyJson.readJson("conf.json"));

        // Отправка запросов
        while (true) {
            try {
                for (String key : User.getAuthorization()) {
                    FetchData data = new FetchData(key);
                    Request req = new Request(key);
                    List<Card> cardList = data.cardList();
                    data.syncInfo();
                    req.UpgradeCard(cardList);
                    if (PRINT_INFO) {
                        for (Card card : cardList) {
                            System.out.println("ID: " + card.getId());
                            System.out.println("Cooldown: " + card.getCooldownSeconds());
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
                    }
                    System.out.println("================\n");
                }
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


