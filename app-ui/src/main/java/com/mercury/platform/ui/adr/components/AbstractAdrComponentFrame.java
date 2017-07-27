package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.components.panel.tree.AdrMouseOverListener;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public abstract class AbstractAdrComponentFrame<T extends AdrComponentDescriptor> extends AbstractAdrFrame<T>{
    private int x;
    private int y;

    protected DraggedFrameMouseListener mouseListener;
    protected DraggedFrameMotionListener motionListener;
    protected MouseAdapter mouseOverListener;
    
    public AbstractAdrComponentFrame(T descriptor) {
        super(descriptor);
        this.setBackground(AppThemeColor.TRANSPARENT);

        this.mouseListener = new DraggedFrameMouseListener();
        this.motionListener = new DraggedFrameMotionListener();
        this.mouseOverListener = new AdrMouseOverListener<>(this.getRootPane(),this.descriptor,false);
    }

    @Override
    protected void initialize() {
        this.setLocation(descriptor.getLocation());
        this.setOpacity(descriptor.getOpacity());
        this.componentsFactory.setScale(descriptor.getScale());
    }

    @Override
    public void subscribe() {
        MercuryStoreUI.adrRepaintSubject.subscribe(state -> {
            this.repaint();
            this.pack();
        });
    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(1,1,1,1,AppThemeColor.ADR_DEFAULT_BORDER));
        this.addMouseListener(this.mouseListener);
        this.addMouseListener(this.mouseOverListener);
        this.addMouseMotionListener(this.motionListener);
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        this.removeMouseListener(this.mouseListener);
        this.removeMouseListener(this.mouseOverListener);
        this.removeMouseMotionListener(this.motionListener);
    }
    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    public class DraggedFrameMotionListener extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                e.translatePoint(AbstractAdrComponentFrame.this.getLocation().x - x, AbstractAdrComponentFrame.this.getLocation().y - y);
                Point point = e.getPoint();
                AbstractAdrComponentFrame.this.setLocation(point);
            }
        }
    }
    public class DraggedFrameMouseListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                x = e.getX();
                y = e.getY();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                if (getLocationOnScreen().y + getSize().height > dimension.height) {
                    setLocation(getLocationOnScreen().x, dimension.height - getSize().height);
                }
                descriptor.setLocation(getLocationOnScreen());
                System.out.println(descriptor.getLocation());
                MercuryStoreUI.adrUpdateSubject.onNext(descriptor);
                MercuryStoreCore.saveConfigSubject.onNext(true);
            }
        }
    }
}
