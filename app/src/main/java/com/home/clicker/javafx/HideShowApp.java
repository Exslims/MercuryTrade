package com.home.clicker.javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by Константин on 09.12.2016.
 */
public class HideShowApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        final Stage window = new Stage();
        window.setX(10);
        Scene innerScene = new Scene(new Label("inner window"));
        window.setScene(innerScene);

        HBox root = new HBox(10d);
        Button showButton = new Button("show");
        showButton.setOnAction(event -> window.show());
        Button hideButton = new Button("hide");
        hideButton.setOnAction(event -> window.hide());
        root.getChildren().add(showButton);
        root.getChildren().add(hideButton);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
