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
import java.util.*;
import java.util.stream.Collectors;

public class PictureBundleConfigurationServiceImpl extends BaseConfigurationService<List<String>> implements IconBundleConfigurationService {
    private static final String PICTURES_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\pictures\\";
    private Map<String, URL> pictureBundle = new HashMap<>();

    public PictureBundleConfigurationServiceImpl(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public URL getIcon(String iconPath) {
        URL url = this.pictureBundle.get(iconPath);
        if (url == null) {
            url = this.pictureBundle.get("default_syndicate.png");
        }
        return url;
    }

    @Override
    public Map<String, URL> getIconBundle() {
        return this.pictureBundle;
    }

    @Override
    public void addIcon(String iconPath) {
        try {
            URL url = new URL("file:///" + iconPath);
            String iconName = StringUtils.substringAfterLast(iconPath, "\\");

            File file = new File(PICTURES_PATH + iconName);
            BufferedImage image = ImageIO.read(url);
            ImageIO.write(image, "png", file);
            this.getEntities().add(iconName);
            this.pictureBundle.put(iconName, url);
        } catch (IOException e) {
            MercuryStoreCore.errorHandlerSubject.onNext(
                    new MercuryError("Error while add icon: " + iconPath, e));
        }
    }

    @Override
    public void removeIcon(String iconPath) {
    }

    @Override
    public List<String> getDefault() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getDefaultBundle() {
        return Arrays.stream(new String[]{
                "default_syndicate.png",
                "syndicate_colored.png",
                "betrayal_reference_guide.png",
        }).collect(Collectors.toList());
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setPictureBundleList(this.getDefault());
    }

    @Override
    public List<String> getEntities() {
        return this.selectedProfile.getPictureBundleList();
    }

    @Override
    public void validate() {
        if (this.selectedProfile.getPictureBundleList() == null) {
            this.selectedProfile.setPictureBundleList(this.getDefault());
        }
        this.initPictureBundle(this.pictureBundle);
    }

    private void initPictureBundle(Map<String, URL> pictureBundle) {
        this.getDefaultBundle().forEach(it -> {
            URL resource = this.getClass().getClassLoader().getResource("app/adr/pictures/" + it);
            if (resource != null) {
                pictureBundle.put(it, resource);
            }
        });
        this.getEntities().forEach(it -> {
            try {
                if (new File(PICTURES_PATH + it).exists()) {
                    URL resource = new URL("file:///" + PICTURES_PATH + it);
                    pictureBundle.put(it, resource);
                } else {
                    URL resource = this.getClass().getClassLoader().getResource("app/adr/pictures/" + "default_syndicate.png");
                    pictureBundle.put(it, resource);
                }
            } catch (MalformedURLException e) {
                MercuryStoreCore.errorHandlerSubject.onNext(
                        new MercuryError("Error while initializing icon: " + it, e));
            }
        });
    }
}
