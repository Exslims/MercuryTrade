package com.home.clicker.javafx;

import com.home.clicker.PrivateMessageManager;
import com.home.clicker.events.EventRouter;
import com.home.clicker.events.SCEvent;
import com.home.clicker.events.SCEventHandler;
import com.home.clicker.events.custom.ActualWritersChangeEvent;
import com.home.clicker.events.custom.FrameStateChangeEvent;
import com.home.clicker.events.custom.SendMessageEvent;
import com.home.clicker.utils.User32;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

/**
 * Created by Константин on 09.12.2016.
 */
public class FXFrame extends Application {
    private User32 user32 = User32.INSTANCE;
    private boolean isEndedDelay = true;
    private WinDef.HWND cachedWindow;
    @Override
    public void start(final Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        final Button btn = new Button();
        btn.setText("TEST'");


        primaryStage.setAlwaysOnTop(true);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(new CustomScene(new Group()));
        primaryStage.show();

        setForegroundWindow("Path of Exile");

    }
    private void setForegroundWindow(final String titleName){
        if(cachedWindow == null) {
            user32.EnumWindows(new WinUser.WNDENUMPROC() {
                int count = 0;

                public boolean callback(WinDef.HWND hWnd, Pointer arg1) {
                    byte[] windowText = new byte[512];
                    user32.GetWindowTextA(hWnd, windowText, 512);
                    String wText = Native.toString(windowText);

                    if (wText.isEmpty()) {
                        return true;
                    }
//
                    System.out.println("Found window with text " + hWnd
                            + ", total " + ++count + " Text: " + wText);
                    if (wText.equals(titleName)) {
                        cachedWindow = hWnd;
                        user32.SetForegroundWindow(hWnd);
                        return false;
                    }
                    return true;
                }
            }, null);
        }else {
            user32.SetForegroundWindow(cachedWindow);
        }
    }
}
