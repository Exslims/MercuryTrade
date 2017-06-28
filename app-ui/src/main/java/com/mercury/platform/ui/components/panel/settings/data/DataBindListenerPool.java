package com.mercury.platform.ui.components.panel.settings.data;


import java.util.ArrayList;
import java.util.List;

public class DataBindListenerPool {
    private List<DataActionListener> listeners = new ArrayList<>();
    private void add(DataActionListener listener){
        this.listeners.add(listener);
    }
    private void save(){
        this.listeners.forEach(DataActionListener::onSave);
    }
}
