package com.mercury.platform.ui.components.datatable.renderer;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;

import javax.swing.*;

public class PlainDoubleRenderer implements MCellRenderer<Double> {
    private ComponentsFactory componentsFactory = new ComponentsFactory();

    @Override
    public JComponent getComponent(Double data) {
        return this.componentsFactory.getTextLabel(data.toString(), FontStyle.REGULAR); //todo
    }
}
