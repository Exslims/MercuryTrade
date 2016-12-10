package com.home.clicker.javafx;

import com.home.clicker.utils.User32;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
        primaryStage.requestFocus();
        primaryStage.show();

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
