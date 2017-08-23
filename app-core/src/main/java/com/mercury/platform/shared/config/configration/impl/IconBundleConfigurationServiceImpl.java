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

public class IconBundleConfigurationServiceImpl extends BaseConfigurationService<List<String>> implements IconBundleConfigurationService {
    private static final String ICONS_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\icons\\";
    private Map<String, URL> iconBundle = new HashMap<>();

    public IconBundleConfigurationServiceImpl(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public URL getIcon(String iconPath) {
        URL url = this.iconBundle.get(iconPath);
        if (url == null) {
            url = this.iconBundle.get("default_icon.png");
        }
        return url;
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
            ImageIO.write(image, "png", file);
            this.getEntities().add(iconName);
            this.iconBundle.put(iconName, url);
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
                "default_icon.png",
                "Abyssal_Cry_skill_icon.png",
                "Ancestral_Protector_skill_icon.png",
                "Ancestral_Warchief_skill_icon.png",
                "Decoy_Totem_skill_icon.png",
                "Devouring_Totem_skill_icon.png",
                "Dominating_Blow_skill_icon.png",
                "Earthquake_skill_icon.png",
                "Enduring_Cry_skill_icon.png",
                "Flame_Totem_skill_icon.png",
                "Immortal_Call_skill_icon.png",
                "Molten_Shell_skill_icon.png",
                "Punishment_skill_icon.png",
                "Rallying_Cry_skill_icon.png",
                "Rejuvenation_Totem_skill_icon.png",
                "Searing_Bond_skill_icon.png",
                "Shockwave_Totem_skill_icon.png",
                "Static_Strike_skill_icon.png",
                "Warlord's_Mark_skill_icon.png",
                "Animate_Weapon_skill_icon.png",
                "Bear_Trap_skill_icon.png",
                "Blade_Vortex_skill_icon.png",
                "Blink_Arrow_skill_icon.png",
                "Caustic_Arrow_skill_icon.png",
                "Desecrate_skill_icon.png",
                "Explosive_Arrow_skill_icon.png",
                "Fire_Trap_skill_icon.png",
                "Freeze_Mine_skill_icon.png",
                "Ice_Shot_skill_icon.png",
                "Ice_Trap_skill_icon.png",
                "Mirror_Arrow_skill_icon.png",
                "Poacher's_Mark_skill_icon.png",
                "Projectile_Weakness_skill_icon.png",
                "Puncture_skill_icon.png",
                "Siege_Ballista_skill_icon.png",
                "Smoke_Mine_skill_icon.png",
                "Temporal_Chains_skill_icon.png",
                "Viper_Strike_skill_icon.png",
                "Arctic_Breath_skill_icon.png",
                "Assassin's_Mark_skill_icon.png",
                "Blight_skill_icon.png",
                "Bone_Offering_skill_icon.png",
                "Conductivity_skill_icon.png",
                "Contagion_skill_icon.png",
                "Conversion_Trap_skill_icon.png",
                "Convocation_skill_icon.png",
                "Elemental_Weakness_skill_icon.png",
                "Enfeeble_skill_icon.png",
                "Essence_Drain_skill_icon.png",
                "Fire_Nova_Mine_skill_icon.png",
                "Firestorm_skill_icon.png",
                "Flame_Dash_skill_icon.png",
                "Flammability_skill_icon.png",
                "Flesh_Offering_skill_icon.png",
                "Frost_Bomb_skill_icon.png",
                "Frost_Wall_skill_icon.png",
                "Frostbite_skill_icon.png",
                "Herald_of_Thunder_skill_icon.png",
                "Lightning_Trap_skill_icon.png",
                "Lightning_Warp_skill_icon.png",
                "Orb_of_Storms_skill_icon.png",
                "Scorching_Ray_skill_icon.png",
                "Spark_skill_icon.png",
                "Spirit_Offering_skill_icon.png",
                "Storm_Call_skill_icon.png",
                "Summon_Raging_Spirit_skill_icon.png",
                "Summon_Skeletons_skill_icon.png",
                "Tempest_Shield_skill_icon.png",
                "Vortex_skill_icon.png",
                "Vulnerability_skill_icon.png",
                "Wither_skill_icon.png",
                "Vaal_Glacial_Hammer_skill_icon.png",
                "Vaal_Immortal_Call_skill_icon.png",
                "Vaal_Molten_Shell_skill_icon.png",
                "Vaal_Cyclone_skill_icon.png",
                "Vaal_Grace_skill_icon.png",
                "Vaal_Haste_skill_icon.png",
                "Vaal_Lightning_Strike_skill_icon.png",
                "Vaal_Rain_of_Arrows_skill_icon.png",
                "Vaal_Clarity_skill_icon.png",
                "Vaal_Cold_Snap_skill_icon.png",
                "Vaal_Discipline_skill_icon.png",
                "Vaal_Lightning_Trap_skill_icon.png",
                "Vaal_Lightning_Warp_skill_icon.png",
                "Vaal_Spark_skill_icon.png",
                "Vaal_Storm_Call_skill_icon.png",
                "Vaal_Summon_Skeletons_skill_icon.png",
                "Phase_Run_skill_icon.png",
                "kappa_icon.png",
                "Arctic_Armour_skill_icon.png",
                "Bismuth_Flask_status_icon.png",
                "Bleeding_Immunity.png",
                "Blood_Rage_skill_icon.png",
                "Chill_And_Freeze_Immunity.png",
                "Diamond_Flask_status_icon.png",
                "Basalt_Flask_status_icon.png",
                "Sulphur_Flask_status_icon.png",
                "Sapphire_Flask_status_icon.png",
                "Quartz_Flask_status_icon.png",
                "Amethyst_Flask_status_icon.png",
                "Aquamarine_Flask_status_icon.png",
                "Granite_Flask_status_icon.png",
                "Jade_Flask_status_icon.png",
                "Quicksilver_Flask_status_icon.png",
                "Ruby_Flask_status_icon.png",
                "Silver_Flask_status_icon.png",
                "Stibnite_Flask_status_icon.png",
                "Topaz_Flask_status_icon.png",
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
        if (this.selectedProfile.getIconBundleList() == null) {
            this.selectedProfile.setIconBundleList(this.getDefault());
        }
        this.initIconBundle(this.iconBundle);
    }

    private void initIconBundle(Map<String, URL> iconBundle) {
        this.getDefaultBundle().forEach(it -> {
            URL resource = this.getClass().getClassLoader().getResource("app/adr/icons/" + it);
            if (resource != null) {
                iconBundle.put(it, resource);
            }
        });
        this.getEntities().forEach(it -> {
            try {
                if (new File(ICONS_PATH + it).exists()) {
                    URL resource = new URL("file:///" + ICONS_PATH + it);
                    iconBundle.put(it, resource);
                } else {
                    URL resource = this.getClass().getClassLoader().getResource("app/adr/icons/" + "default_icon.png");
                    iconBundle.put(it, resource);
                }
            } catch (MalformedURLException e) {
                MercuryStoreCore.errorHandlerSubject.onNext(
                        new MercuryError("Error while initializing icon: " + it, e));
            }
        });
    }
}
