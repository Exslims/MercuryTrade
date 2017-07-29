package com.mercury.platform.shared.config.configration;

import java.net.URL;
import java.util.Map;

public interface IconBundleConfigurationService extends ListConfigurationService<String> {
    URL getIcon(String iconPath);
    Map<String,URL> getIconBundle();
    void addIcon(String iconPath);
    void removeIcon(String iconPath);
}
