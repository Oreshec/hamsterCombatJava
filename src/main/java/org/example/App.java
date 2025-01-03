package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    static boolean PRINT_INFO = false;

    public static void main(String[] args) {
        // Инициализация и загрузка ключей из файла
        User.setAuthorization(ReadKeyJson.readJson("conf.json"));
        // Отправка запросов
        while (true) {
            try {
                LOGGER.info("Brute force through an array of passwords");
                int minCooldown = 60;
                for (String key : User.getAuthorization()) {

                    FetchData data = new FetchData(key);
                    Request req = new Request(key);

                    LOGGER.info("Loading syncInfo");
                    data.syncInfo();
                    LOGGER.info("Загрузка syncInfo прошла");

                    LOGGER.info("Loading App.data.main(card.List())");
                    List<Card> cardList = data.cardList();
                    if (cardList == null || cardList.isEmpty()) {
                        LOGGER.warning("cardList is null or empty for the key");
                        continue; // Пропуск текущей итерации и переход к следующему ключу
                    }

                    LOGGER.info("App.main.cardList: " + cardList.stream().limit(5).toList());

                    List<Integer> cooldownArr = new ArrayList<>();
                    for (Card card : cardList) {
                        if (card.getPrice() <= User.getBalanceDiamonds()) {
                            cooldownArr.add(card.getCooldownSeconds());
                        }
                    }
                    LOGGER.info("cooldownArr: " + cooldownArr);
                    if (!cooldownArr.isEmpty()) {
                        minCooldown = Collections.min(cooldownArr);
                    } else {
                        minCooldown = 60;
                    }

                    if (PRINT_INFO) {
                        for (Card card : cardList) {
                            System.out.println("===CARD===");
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
                    req.UpgradeCard(cardList);
                }

                int sleepTime = Math.min(minCooldown * 1000, 3 * 60 * 60 * 1000);

                long second = (sleepTime / 1000) % 60;
                long minute = (sleepTime / (1000 * 60)) % 60;
                long hours = (sleepTime / (1000 * 60 * 60)) % 24;

                String formatedTime = String.format("H.%02d:M.%02d:S.%02d", hours, minute, second);
                LOGGER.log(Level.INFO, "Sleep at " + "\"" + formatedTime + "\"");

                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "InterruptedException" + e.getMessage(), e);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Critical damage!" + e.getMessage(), e);
            }
        }
    }
}


