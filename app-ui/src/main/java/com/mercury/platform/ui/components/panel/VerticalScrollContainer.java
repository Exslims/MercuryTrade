package com.mercury.platform.ui.components.panel;

import javax.swing.*;
import java.awt.*;

public class VerticalScrollContainer extends JPanel implements Scrollable {

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return super.getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
