package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.config.descriptor.StashTabDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;

import java.util.List;

public class StashTabsContainer {
    private ListConfigurationService<StashTabDescriptor> stashTabConfig;

    public StashTabsContainer() {
        this.stashTabConfig = Configuration.get().stashTabConfiguration();
    }

    public void addTab(StashTabDescriptor tab) {
        this.stashTabConfig.getEntities().add(tab);
        this.save();
    }

    public void save() {
        MercuryStoreCore.saveConfigSubject.onNext(true);
    }

    public List<StashTabDescriptor> getStashTabDescriptors() {
        return this.stashTabConfig.getEntities();
    }

    public boolean containsTab(String tabTitle) {
        return this.stashTabConfig.getEntities().stream().anyMatch(tab -> tab.getTitle().equals(tabTitle));
    }

    public StashTabDescriptor getStashTab(String tabTitle) {
        return this.stashTabConfig.getEntities().stream().filter(tab -> tab.getTitle().equals(tabTitle)).findFirst().get();
    }

    public void removeTab(StashTabDescriptor tab) {
        this.stashTabConfig.getEntities().remove(tab);
        this.save();
    }
}
