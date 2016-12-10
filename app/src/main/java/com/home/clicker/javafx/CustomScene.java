package com.home.clicker.javafx;

import com.home.clicker.events.EventRouter;
import com.home.clicker.events.SCEventHandler;
import com.home.clicker.events.custom.ActualWritersChangeEvent;
import com.home.clicker.events.custom.FrameStateChangeEvent;
import com.home.clicker.events.custom.SendMessageEvent;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.pushingpixels.trident.Timeline;

import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * Created by Константин on 10.12.2016.
 */
public class CustomScene extends Scene {
    private TabPane chatPane;
    public CustomScene(@NamedArg("root") Parent root) {
        super(root);
        Platform.setImplicitExit( false );
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Pane rootPanel = new Pane();
        rootPanel.setPrefSize(screenSize.getWidth(),screenSize.getHeight());
        rootPanel.setStyle("-fx-background-color: rgba(0,0,0,0)");
        setRoot(rootPanel);
        setFill(Color.TRANSPARENT);
        rootPanel.setVisible(true);

        createChatButton();
        createChatPanel();
        chatPane.getTabs().add(getNewChatTab("Tester"));

        EventRouter.registerHandler(FrameStateChangeEvent.class, event -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    changeState(chatPane,((FrameStateChangeEvent)event).getState());
                }
            });
        });

        EventRouter.registerHandler(ActualWritersChangeEvent.class, event -> {
            Platform.runLater(() -> {
                List<String> writers = ((ActualWritersChangeEvent)event).getWriters();
                for (String writer :writers) {
                    chatPane.getTabs().add(getNewChatTab(writer));
//                    Button button = new Button(writer);
//                    button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//                        @Override
//                        public void handle(MouseEvent event1) {
//                            String message = button.getText();
//                            EventRouter.fireEvent(new SendMessageEvent(message));
//                        }
//                    });
//                    rootPanel.getChildren().add(button);
                }
            });
        });
    }

    private void createChatButton() {
        ImageView image = new ImageView("chatImage.png");
        image.setFitHeight(40);
        image.setFitWidth(50);
        image.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changeState(chatPane,FrameStates.SHOW);
            }
        });
        image.setLayoutX(0);
        image.setLayoutY(680);
        ((Pane)getRoot()).getChildren().add(image);

    }

    private void createChatPanel(){
        this.chatPane = new TabPane();
        chatPane.setPrefSize(450,300);
        chatPane.setLayoutX(0);
        chatPane.setLayoutY(378.0);
        ((Pane)getRoot()).getChildren().add(chatPane);
        chatPane.setOpacity(0);
        chatPane.setVisible(true);

        chatPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("dragged");
                chatPane.setLayoutX(event.getSceneX());
                chatPane.setLayoutY(event.getSceneY());
            }
        });
    }
    private Tab getNewChatTab(String whisperName){
        Tab tab = new Tab();
        tab.setText(whisperName);
        HBox hBox = new HBox();
//        hBox.setStyle("-fx-background-color: aliceblue");
        hBox.getChildren().add(new Label("TEST"));
        hBox.setAlignment(Pos.TOP_LEFT);
        tab.setContent(hBox);
        return tab;
    }
    private void changeState(Node target, FrameStates states){
        switch (states){
            case SHOW: {
                if(target.getOpacity() == 0) {
//                    Timeline opacityTimeLine = new Timeline(chatPane);
//                    opacityTimeLine.addPropertyToInterpolate("opacity",0f,1f);
//                    opacityTimeLine.setDuration(200);
//                    opacityTimeLine.play();
                    FadeTransition ft = new FadeTransition(Duration.millis(200),target);
                    ft.setFromValue(0.0);
                    ft.setToValue(0.8f);
                    ft.play();
                }
                break;
            }
            case HIDE:{
                if(target.getOpacity() == 1) {
//                    Timeline opacityTimeLine = new Timeline(chatPane);
//                    opacityTimeLine.addPropertyToInterpolate("opacity",1f,0f);
//                    opacityTimeLine.setDuration(200);
//                    opacityTimeLine.play();
                    FadeTransition ft = new FadeTransition(Duration.millis(200),target);
                    ft.setFromValue(0.8f);
                    ft.setToValue(0.0);
                    ft.play();
                }
            }
        }
    }
}
