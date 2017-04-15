package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.entity.StashTab;

import java.util.List;
public class StashTabsContainer {
    private List<StashTab> stashTabs;
    public StashTabsContainer() {
        stashTabs = ConfigManager.INSTANCE.getStashTabs();
    }
    public void addTab(StashTab tab){
        stashTabs.add(tab);
        save();
    }
    public void save(){
        ConfigManager.INSTANCE.saveStashTabs(stashTabs);
    }

    public List<StashTab> getStashTabs() {
        return stashTabs;
    }
    public boolean containsTab(String tabTitle){
        return stashTabs.stream().anyMatch(tab -> tab.getTitle().equals(tabTitle));
    }
    public StashTab getStashTab(String tabTitle){
        return stashTabs.stream().filter(tab -> tab.getTitle().equals(tabTitle)).findFirst().get();
    }
    public void removeTab(StashTab tab){
        stashTabs.remove(tab);
        save();
    }
}
