package org.example;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Her1 extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primeryStage) {
		FlowPane flowPane = new FlowPane();
		Scene scene = new Scene(flowPane, 400, 400);
		flowPane.setHgap(10);
		flowPane.setVgap(10);
		
		double minSquareSize = 80;
		
		User.setAuthorization(ReadKeyJson.readJson("conf.json"));
		int sizeCard = 80;
		for (String key : User.getAuthorization()) {
			Text text = new Text(key);
			Rectangle rectangle = new Rectangle();
			rectangle.setArcWidth(20);
			rectangle.setArcHeight(20);
			rectangle.setFill(Color.WHITE);
			rectangle.setStroke(Color.DARKBLUE);
			rectangle.setStrokeWidth(2);
			rectangle.widthProperty()
					.bind(Bindings.createDoubleBinding(() ->
							Math.max((scene.getWidth() - 40) / 5, minSquareSize), scene.widthProperty()));
			rectangle.heightProperty()
					.bind(rectangle.widthProperty());
			
			Label label = new Label(key);
			label.setMaxWidth(sizeCard);
			label.setWrapText(true);
			
			StackPane stackPane = new StackPane();
			stackPane.getChildren()
					.addAll(rectangle, label);
			
			flowPane.getChildren()
					.add(stackPane);
		}
		
		primeryStage.setScene(scene);
		primeryStage.show();
	}
}
