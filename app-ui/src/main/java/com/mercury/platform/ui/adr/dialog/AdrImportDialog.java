package com.mercury.platform.ui.adr.dialog;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;

import java.awt.*;
import java.util.*;
import java.util.List;


public class AdrImportDialog<T extends AdrComponentDescriptor> extends AdrDialog<T>{
    public AdrImportDialog(Component relative, T payload) {
        super(relative, payload);
    }
    @Override
    protected void createView() {

    }

    @Override
    protected void postConstruct() {

    }
}
