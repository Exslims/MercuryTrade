package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.IconBundleConfigurationService;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.entity.message.MercuryError;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IconBundleConfigurationServiceImpl extends BaseConfigurationService<List<String>> implements IconBundleConfigurationService {
    private Map<String, URL> iconBundle = new HashMap<>();
    private static final String ICONS_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\icons\\";
    public IconBundleConfigurationServiceImpl(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public URL getIcon(String iconPath) {
        return this.iconBundle.get(iconPath);
    }

    @Override
    public Map<String, URL> getIconBundle() {
        return this.iconBundle;
    }

    @Override
    public void addIcon(String iconPath) {
        try {
            URL url = new URL("file:///" + iconPath);
            String iconName = StringUtils.substringAfterLast(iconPath, "\\");

            File file = new File(ICONS_PATH + iconName);
            BufferedImage image = ImageIO.read(url);
            ImageIO.write(image,"png",file);
            this.getEntities().add(iconName);
            this.iconBundle.put(iconName, url);
        } catch (IOException e) {
            MercuryStoreCore.errorHandlerSubject.onNext(
                    new MercuryError("Error while add icon: " + iconPath,e));
        }
    }

    @Override
    public void removeIcon(String iconPath) {
    }
    @Override
    public List<String> getDefault() {
        return Arrays.stream(new String[] {
                "no_icon.png",
                "default_icon.png",
                "kappa_icon.png",
                "Arctic_Armour_skill_icon.png",
                "Bismuth_Flask.png",
                "Bleeding_Immunity.png",
                "Blood_Rage_skill_icon.png",
                "Chill_And_Freeze_Immunity.png",
                "Diamond_Flask.png",
                "Granite_Flask.png",
                "Increase_Movement_Speed.png",
                "Jade_Flask.png",
                "Phase_Run_skill_icon.png",
                "Quicksilver_Flask.png",
                "Ruby_Flask.png",
                "Silver_Flask.png",
                "Stibnite_Flask.png",
                "Topaz_Flask.png",
                "Witchfire_Brew.png"
        }).collect(Collectors.toList());
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setIconBundleList(this.getDefault());
    }

    @Override
    public List<String> getEntities() {
        return this.selectedProfile.getIconBundleList();
    }

    @Override
    public void validate() {
        if(this.selectedProfile.getIconBundleList() == null){
            this.selectedProfile.setIconBundleList(this.getDefault());
        }
        this.initIconBundle(this.iconBundle);
    }
    private void initIconBundle(Map<String,URL> iconBundle){
        this.getEntities().forEach(it -> {
            URL resource = this.getClass().getClassLoader().getResource("app/adr/icons/" + it);
            if(resource == null) {
                try {
                    resource = new URL("file:///" + ICONS_PATH + it);
                } catch (MalformedURLException e) {
                    MercuryStoreCore.errorHandlerSubject.onNext(
                            new MercuryError("Error while initializing icon: " + it,e));
                }
            }
            if(resource != null) {
                iconBundle.put(it, resource);
            }else {
                iconBundle.put(it, iconBundle.get("default_icon.png"));
            }
        });
    }
}
