package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.descriptor.adr.AdrCaptureDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.components.panel.AdrComponentPanel;
import com.mercury.platform.ui.adr.components.panel.tree.AdrMouseOverListener;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;
import rx.Subscription;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AdrCaptureOutComponentFrame extends AbstractAdrFrame<AdrCaptureDescriptor> {
    private int x;
    private int y;
    @Getter
    private AdrComponentPanel component;

    private CaptureDraggedFrameMotionListener motionListener;
    private CaptureDraggedFrameMouseListener mouseListener;
    private AdrMouseOverListener mouseOverListener;

    private Subscription adrReloadSubscription;

    public AdrCaptureOutComponentFrame(AdrCaptureDescriptor descriptor) {
        super(descriptor);

        this.mouseListener = new CaptureDraggedFrameMouseListener();
        this.motionListener = new CaptureDraggedFrameMotionListener();
        this.mouseOverListener = new AdrMouseOverListener<>(this.getRootPane(), this.descriptor, false);
    }

    @Override
    public void setPanel(AdrComponentPanel panel) {
        this.component = panel;
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new GridLayout(1, 1);
    }

    @Override
    protected void initialize() {
        this.setLocation(descriptor.getCaptureLocation());
        this.componentsFactory.setScale(descriptor.getScale());
        this.setLayout(new GridLayout(1, 1));
        this.add(this.component);
        this.pack();
    }

    @Override
    public void subscribe() {
        super.subscribe();
        this.adrReloadSubscription = MercuryStoreUI.adrReloadSubject.subscribe(descriptor -> {
            if (descriptor.equals(this.descriptor)) {
                this.setLocation(this.descriptor.getCaptureLocation());
                this.setPreferredSize(this.descriptor.getCaptureSize());
                this.repaint();
                this.pack();
            }
        });
    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        this.component.enableSettings();

        this.addMouseListener(this.mouseListener);
        this.addMouseListener(this.mouseOverListener);
        this.addMouseMotionListener(this.motionListener);

        this.component.addMouseListener(this.mouseListener);
        this.component.addMouseListener(this.mouseOverListener);
        this.component.addMouseMotionListener(this.motionListener);
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        this.component.disableSettings();
        this.removeMouseListener(this.mouseListener);
        this.removeMouseListener(this.mouseOverListener);
        this.removeMouseMotionListener(this.motionListener);

        this.component.removeMouseListener(this.mouseListener);
        this.component.removeMouseListener(this.mouseOverListener);
        this.component.removeMouseMotionListener(this.motionListener);

        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        this.pack();
        this.repaint();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.adrReloadSubscription.unsubscribe();
        this.component.onDestroy();
    }

    @Override
    public void onViewInit() {

    }


    public class CaptureDraggedFrameMotionListener extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                e.translatePoint(AdrCaptureOutComponentFrame.this.getLocation().x - x, AdrCaptureOutComponentFrame.this.getLocation().y - y);
                Point point = e.getPoint();
                AdrCaptureOutComponentFrame.this.setLocation(point);
            }
        }
    }

    public class CaptureDraggedFrameMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                x = e.getX();
                y = e.getY();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                if (getLocationOnScreen().y + getSize().height > dimension.height) {
                    setLocation(getLocationOnScreen().x, dimension.height - getSize().height);
                }
                descriptor.setCaptureLocation(getLocationOnScreen());
                MercuryStoreUI.adrUpdateSubject.onNext(descriptor);
                MercuryStoreCore.saveConfigSubject.onNext(true);
            }
        }
    }
}
