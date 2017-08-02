package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.components.panel.tree.AdrMouseOverListener;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import rx.Subscription;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public abstract class AbstractAdrComponentFrame<T extends AdrComponentDescriptor> extends AbstractAdrFrame<T>{
    private int x;
    private int y;

    protected DraggedFrameMouseListener mouseListener;
    protected DraggedFrameMotionListener motionListener;
    protected AdrMouseOverListener mouseOverListener;

    private Subscription adrRepaintSubscription;
    private Subscription adrVisibleSubscription;

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
        this.adrRepaintSubscription = MercuryStoreUI.adrRepaintSubject.subscribe(state -> {
            this.repaint();
            this.pack();
        });
        this.adrVisibleSubscription = MercuryStoreCore.adrVisibleSubject.subscribe(state -> {
            switch (state) {
                case SHOW: {
                    this.processingHideEvent = false;
                    break;
                }
                case HIDE: {
                    this.processingHideEvent = true;
                    break;
                }
            }
        });
        MercuryStoreUI.onDestroySubject.subscribe(state -> this.onDestroy());
    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        this.addMouseListener(this.mouseListener);
        this.addMouseListener(this.mouseOverListener);
        this.addMouseMotionListener(this.motionListener);
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        this.removeMouseListener(this.mouseListener);
        this.removeMouseListener(this.mouseOverListener);
        this.removeMouseMotionListener(this.motionListener);
    }

    @Override
    public void onDestroy() {
        this.mouseOverListener.onDestroy();
        this.adrRepaintSubscription.unsubscribe();
        this.adrVisibleSubscription.unsubscribe();
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
                MercuryStoreUI.adrUpdateSubject.onNext(descriptor);
                MercuryStoreCore.saveConfigSubject.onNext(true);
            }
        }
    }
}
