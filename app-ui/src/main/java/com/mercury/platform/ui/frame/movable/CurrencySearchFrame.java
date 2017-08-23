package com.mercury.platform.ui.frame.movable;

import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by Константин on 02.03.2017.
 */
public class CurrencySearchFrame extends AbstractMovableComponentFrame {
    private JPanel setUpPanel;
    private JPanel loadingPanel;
    private JPanel resultPanel;

    public CurrencySearchFrame() {
        super();
        prevState = FrameVisibleState.SHOW;
    }

    @Override
    protected void initialize() {
        super.initialize();
        loadingPanel = getLoadingPanel();
        setUpPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        setUpPanel.setBackground(AppThemeColor.FRAME);
        setUpPanel.setLayout(new BoxLayout(setUpPanel, BoxLayout.Y_AXIS));

        JPanel navBar = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel topPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        JButton search = componentsFactory.getBorderedButton("Search");
        search.setPreferredSize(new Dimension(90, 24));
        componentsFactory.setUpToggleCallbacks(search,
                () -> {
                    this.removeAll();
                    this.add(setUpPanel, BorderLayout.CENTER);
                    this.pack();
                    this.repaint();
                    search.setText("Search");
                },
                () -> {
                    this.removeAll();
                    this.add(loadingPanel, BorderLayout.CENTER);
                    this.pack();
                    this.repaint();
                    search.setText("Back");
                },
                false
        );
        navBar.add(search);

        JPanel wantTitlePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        wantTitlePanel.add(componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP, 22f, "What do you want:"));
        topPanel.add(navBar, BorderLayout.LINE_END);
        topPanel.add(wantTitlePanel, BorderLayout.CENTER);

        JPanel downPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel haveTitlePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        haveTitlePanel.add(componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP, 22f, "What do you have:"));
        downPanel.add(haveTitlePanel, BorderLayout.CENTER);

        setUpPanel.add(topPanel);
        setUpPanel.add(downPanel);
        this.add(setUpPanel, BorderLayout.CENTER);
        this.pack();
    }

    private JPanel getLoadingPanel() {
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        JLabel loadingIcon = componentsFactory.getIconLabel("app/reload.gif");
        root.add(loadingIcon, BorderLayout.CENTER);
        return root;
    }

    @Override
    public void subscribe() {

    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    @Override
    protected JPanel getPanelForPINSettings() {
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP, 20f, "Currency searcher"));
        root.setPreferredSize(new Dimension(600, 300));
        JLabel leftArrow = componentsFactory.getIconLabel("app/arrows/left-arrow.png", 68);
        JPanel upPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel upArrow = componentsFactory.getIconLabel("app/arrows/up-arrow.png", 62);
        upPanel.add(upArrow);
        JLabel rightArrow = componentsFactory.getIconLabel("app/arrows/right-arrow.png", 68);
        JLabel downArrow = componentsFactory.getIconLabel("app/arrows/down-arrow.png", 62);
        JPanel downPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        downPanel.add(downArrow);

        root.add(leftArrow, BorderLayout.LINE_START);
        root.add(upPanel, BorderLayout.PAGE_START);
        root.add(rightArrow, BorderLayout.LINE_END);
        root.add(downPanel, BorderLayout.PAGE_END);
        root.add(labelPanel, BorderLayout.CENTER);
        root.setBackground(AppThemeColor.FRAME);
        return root;
    }

    @Override
    protected void registerDirectScaleHandler() {

    }

    @Override
    protected void performScaling(Map<String, Float> scaleData) {

    }

    @Override
    public void onViewInit() {

    }

    @Override
    protected JPanel defaultView(ComponentsFactory factory) {
        return null;
    }
}
