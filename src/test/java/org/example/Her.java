package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Her extends Application {
	private static final Logger LOGGER = Logger.getLogger(Her.class.getName());
	static boolean PRINT_INFO = true;
	private TextArea logArea;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("JavaFX App");
		
		logArea = new TextArea();
		logArea.setEditable(false);
		
		Button startButton = new Button("Start");
		startButton.setOnAction(e -> startProcess());
		
		VBox root = new VBox(10, logArea, startButton);
		Scene scene = new Scene(root, 600, 400);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void startProcess() {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				LOGGER.info("Чтение паролей JSON");
				User.setAuthorization(ReadKeyJson.readJson("conf.json"));
				
				while (true) {
					try {
						LOGGER.info("Перебор массива паролей");
						int minCooldown = 60;
						for (String key : User.getAuthorization()) {
							FetchData data = new FetchData(key);
							Request req = new Request(key);
							
							LOGGER.info("Загрузка syncInfo");
							appendLog("===USER==="); // Запись в TextArea
							data.syncInfo();
							LOGGER.info("Загрузка syncInfo прошла");
							
							LOGGER.info("Загрузка App.data.main(card.List())");
							List<Card> cardList = data.cardList();
							LOGGER.info("App.main.cardList: " + cardList.toString());
							
							List<Integer> cooldownArr = new ArrayList<>();
							for (Card card : cardList) {
								if (card.getPrice() <= User.getBalanceDiamonds()) {
									cooldownArr.add(card.getCooldownSeconds());
								}
							}
							LOGGER.info("cooldownArr: " + cooldownArr);
							minCooldown = Collections.min(cooldownArr);
							
							if (PRINT_INFO) {
								for (Card card : cardList) {
									appendLog("===CARD===\n" +
											"ID: " + card.getId() + "\n" +
											"Cooldown: " + card.getCooldownSeconds() + "\n" +
											"Name: " + card.getName() + "\n" +
											"Available: " + card.isAvailable() + "\n" +
											"Expired: " + card.isExpired() + "\n" +
											"Price: " + card.getPrice() + "\n" +
											"Profit per hour Delta: " + card.getProfitPerHourDelta() + "\n" +
											"Max level: " + card.getMaxLevel() + "\n" +
											"Level: " + card.getLevel() + "\n" +
											"Payback period: " + card.getPayback() + "\n" +
											"--------");
								}
							}
							req.UpgradeCard(cardList);
						}
						int sleepTime = Math.min(minCooldown * 1000, 3 * 60 * 60 * 1000);
						
						long second = (sleepTime / 1000) % 60;
						long minute = (sleepTime / (1000 * 60)) % 60;
						long hours = (sleepTime / (1000 * 60 * 60)) % 24;
						
						String formattedTime = String.format("H.%02d:M.%02d:S.%02d", hours, minute, second);
						LOGGER.log(Level.INFO, "Сон на " + "\"" + formattedTime + "\"");
						
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						LOGGER.log(Level.SEVERE, "InterruptedException" + e.getMessage(), e);
						break;
					} catch (Exception e) {
						LOGGER.log(Level.SEVERE, "Critical damage!" + e.getMessage(), e);
						break;
					}
				}
				return null;
			}
		};
		new Thread(task).start();
	}
	
	private void appendLog(String message) {
		Platform.runLater(() -> logArea.appendText(message + "\n"));
	}
}


