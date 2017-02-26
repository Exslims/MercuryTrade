package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ShowDonationAlert;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

/**
 * Created by Константин on 27.01.2017.
 */
public class DonationAlertFrame extends OverlaidFrame {
    private Timeline hideAnimation;
    private Timeline showAnimation;
    public DonationAlertFrame() {
        super("MT-DonationsAlert");
        this.setOpacity(0f);
        this.setBackground(AppThemeColor.TRANSPARENT);
    }

    @Override
    protected void initialize() {
        JButton donate = componentsFactory.getIconifiedTransparentButton("app/app-icon-donate.png","Donate");
        donate.setBackground(AppThemeColor.TRANSPARENT);
        donate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hideAnimation.abort();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hideAnimation.play();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=HJVSYP4YR7V88&lc=US&item_name=MercuryTrade&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted"));
                }catch (Exception e1){
                }
            }
        });

        hideAnimation = new Timeline(this);
        hideAnimation.setDuration(200);
        hideAnimation.setInitialDelay(4000);
        hideAnimation.addPropertyToInterpolate("opacity", 0.9f, 0f);
        hideAnimation.addCallback(new TimelineCallback() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if(newState.equals(Timeline.TimelineState.DONE)){
                    setAlwaysOnTop(false);
                    setVisible(false);
                }
            }
            @Override
            public void onTimelinePulse(float durationFraction, float timelinePosition) {
            }
        });

        showAnimation = new Timeline(this);
        showAnimation.setDuration(200);
        showAnimation.addPropertyToInterpolate("opacity", 0f, 0.9f);
        showAnimation.addCallback(new TimelineCallback() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if(newState.equals(Timeline.TimelineState.DONE)){
                    hideAnimation.play();
                }
            }
            @Override
            public void onTimelinePulse(float durationFraction, float timelinePosition) {
            }
        });
        this.add(donate);
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width - 180, dim.height - 180);
    }

    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(ShowDonationAlert.class, e-> {
            setAlwaysOnTop(true);
            setVisible(true);
            this.pack();
            hideAnimation.abort();
            showAnimation.abort();
            showAnimation.play();
        });
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new FlowLayout();
    }
}
