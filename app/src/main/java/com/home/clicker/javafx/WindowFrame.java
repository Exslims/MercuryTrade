package com.home.clicker.javafx;

import com.home.clicker.PrivateMessageManager;
import com.home.clicker.events.EventRouter;
import com.home.clicker.events.SCEvent;
import com.home.clicker.events.SCEventHandler;
import com.home.clicker.events.custom.ActualWritersChangeEvent;
import com.home.clicker.events.custom.FrameStateChangeEvent;
import com.home.clicker.events.custom.SendMessageEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

/**
 * Created by Константин on 09.12.2016.
 */
public class WindowFrame extends Application {
    private StackPane nicknamesPanel;
    private Stage mainStage;
    private Runnable stateChangeThread;
    private FrameStates currentState;
    private boolean busy = false;
    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Button btn = new Button();
        btn.setText("TEST'");

        nicknamesPanel = new StackPane();
        nicknamesPanel.getChildren().add(btn);

        mainStage = new Stage();
        Scene scene = new Scene(nicknamesPanel, 300, 250);

        mainStage.setAlwaysOnTop(true);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.setScene(scene);
        mainStage.setOpacity(0.7f);
        mainStage.hide();
        primaryStage.hide();

        //TODO FILE CHOOSER
//        new PrivateMessageManager(primaryStage);

        EventRouter.registerHandler(FrameStateChangeEvent.class, new SCEventHandler<FrameStateChangeEvent>() {
            @Override
            public void handle(FrameStateChangeEvent event) {
                currentState = event.getState();
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        changeState(currentState);
//                    }
//                });
                changeState(currentState);
            }
        });

        EventRouter.registerHandler(ActualWritersChangeEvent.class, new SCEventHandler<ActualWritersChangeEvent>() {
            public void handle(ActualWritersChangeEvent event) {
                nicknamesPanel.getChildren().clear();
                List<String> writers = event.getWriters();
                for (String writer :writers) {
                    final Button button = new Button(writer);
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            String message = button.getText();
                            EventRouter.fireEvent(new SendMessageEvent(message));
                        }
                    });
                    nicknamesPanel.getChildren().add(button);
                }
            }
        });
    }
    private void changeState(FrameStates states){
        switch (states){
            case SHOW: {
                if(!mainStage.isShowing()) {
                    mainStage.show();
                }
                break;
            }
            case HIDE:{
                if(mainStage.isShowing()) {
                    mainStage.hide();
                }
            }
        }
    }
}
