package com.mercury.platform.ui.adr.components.panel.page;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryLoading;

import java.awt.*;

public class AdrLoadingPage extends AdrPagePanel<AdrComponentDescriptor> {
    private MercuryLoading mercuryLoading;

    public AdrLoadingPage() {
        super();
        this.mercuryLoading = new MercuryLoading();
        this.add(mercuryLoading, BorderLayout.CENTER);
    }

    @Override
    protected void init() {
    }

    public void playLoop() {
        this.mercuryLoading.playLoop();
    }

    public void abort() {
        this.mercuryLoading.abort();
    }
}
