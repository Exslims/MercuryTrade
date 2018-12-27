package com.mercury.platform.ui.frame.other;

import com.mercury.platform.shared.CloneHelper;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.IconBundleConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.TaskBarDescriptor;
import com.mercury.platform.shared.entity.message.MercuryError;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HelpIGFrame extends AbstractOverlaidFrame {
    private static int MARGIN = 200;
    private JLabel img = new JLabel();

    public HelpIGFrame() {
        super();
    }

    @Override
    protected void initialize() {
        this.componentsFactory.setScale(1.1f);
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.pictureChange.subscribe(message -> refreshImage());
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    @Override
    public void onViewInit() {
        refreshImage();
        this.add(img);
        this.repaint();
        this.pack();
    }

    private void refreshImage() {
        ImageIcon icon = this.componentsFactory.getImage("app/adr/pictures/default_syndicate.png");
        PlainConfigurationService<TaskBarDescriptor> taskBarService = Configuration.get().taskBarConfiguration();
        TaskBarDescriptor taskBarSnapshot = CloneHelper.cloneObject(taskBarService.get());
        IconBundleConfigurationService config = Configuration.get().pictureBundleConfiguration();
        try {
            URL url = config.getIcon(taskBarSnapshot.getHelpIGPath());
            BufferedImage img = ImageIO.read(url);
            icon = new ImageIcon(img);
        } catch (MalformedURLException e) {
            MercuryStoreCore.errorHandlerSubject.onNext(
                    new MercuryError("Error while initializing picture: " + taskBarSnapshot.getHelpIGPath(), e));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setOpacity(this.applicationConfig.get().getMaxOpacity() / 100f);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        Image image = icon.getImage(); // transform it
        Image newImg = image.getScaledInstance(dim.width - MARGIN, -1,  java.awt.Image.SCALE_SMOOTH);
        if(dim.height-MARGIN < newImg.getHeight(null)) {
            newImg = image.getScaledInstance(-1, dim.height - MARGIN,  java.awt.Image.SCALE_SMOOTH);
        }
        icon = new ImageIcon(newImg);

        img.setIcon(icon);
        this.repaint();
        this.pack();
        this.setLocation(dim.width/2-newImg.getWidth(null)/2, 0);
    }
}
