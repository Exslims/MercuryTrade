package com.mercury.platform.ui.frame.adr;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.entity.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.entity.adr.AdrGroupDescriptor;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.components.panel.adr.ui.AdrListEntry;
import com.mercury.platform.ui.components.panel.adr.ui.AdrListEntryCellRenderer;
import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

public class AdrManagerFrame extends AbstractTitledComponentFrame{
    private JPanel auraTreeContainer;
    public AdrManagerFrame() {
        super("MercuryTrade");
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.add(getRootPanel(),BorderLayout.CENTER);

    }
    private JPanel getRootPanel(){
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        DefaultListModel<AdrListEntry> model = new DefaultListModel<>();
        AdrComponentDescriptor adrComponentDescriptor = Configuration.get().adrGroupConfiguration().getEntities().get(0).getContents().get(0);

        ((AdrGroupDescriptor)adrComponentDescriptor).getCells().forEach(it -> {
            model.addElement(new AdrListEntry(it.getIconPath()));
        });

        JList<AdrListEntry> list = new JList<>(model);
        list.setCellRenderer(new AdrListEntryCellRenderer());
        list.setBackground(AppThemeColor.TRANSPARENT);
        list.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,0,1,AppThemeColor.BORDER),
                BorderFactory.createEmptyBorder(4,4,4,4)));
        root.add(list,BorderLayout.LINE_START);

        return root;
    }
    private JPanel getAuraTree(){
        auraTreeContainer = new VerticalScrollContainer();
        auraTreeContainer.setBackground(AppThemeColor.TRANSPARENT);
        auraTreeContainer.setLayout(new BoxLayout(auraTreeContainer,BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(auraTreeContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(AppThemeColor.FRAME);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                repaint();
            }
        });

        auraTreeContainer.getParent().setBackground(AppThemeColor.TRANSPARENT);
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setBackground(AppThemeColor.SLIDE_BG);
        vBar.setUI(new MercuryScrollBarUI());
        vBar.setPreferredSize(new Dimension(15, Integer.MAX_VALUE));
        vBar.setUnitIncrement(3);
        vBar.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        vBar.addAdjustmentListener(e -> repaint());
        return null;
    }

    @Override
    public void subscribe() {
    }

    @Override
    protected String getFrameTitle() {
        return "placeholder";
    }

}
