package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Константин on 05.01.2017.
 */
public class AboutPanel extends ConfigurationPanel implements HasUI {
    public AboutPanel() {
        super();
        createUI();
    }

    @Override
    public void processAndSave() {

    }

    @Override
    public void createUI() {
        JButton donate = componentsFactory.getIconifiedTransparentButton("app/paypal.png","Donate");
        donate.setBackground(AppThemeColor.SLIDE_BG);
        donate.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=HJVSYP4YR7V88&lc=US&item_name=MercuryTrade&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        this.add(donate);
        this.add(componentsFactory.getTextLabel("About program and contacts here."));
        this.add(componentsFactory.getTextField("TYPE HERE", FontStyle.BOLD,16));
    }

    @Override
    protected LayoutManager getPanelLayout() {
        return new FlowLayout(FlowLayout.LEFT);
    }
}
