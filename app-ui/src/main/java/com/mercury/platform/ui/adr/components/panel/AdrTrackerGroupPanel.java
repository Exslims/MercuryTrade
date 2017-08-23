package com.mercury.platform.ui.adr.components.panel;

import com.mercury.platform.shared.config.descriptor.adr.AdrDurationComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupType;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import rx.Subscription;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdrTrackerGroupPanel extends AdrComponentPanel<AdrTrackerGroupDescriptor> {
    private List<AdrComponentPanel> cells;
    private Subscription adrPostOpSubscription;

    public AdrTrackerGroupPanel(AdrTrackerGroupDescriptor descriptor, ComponentsFactory componentsFactory) {
        super(descriptor, componentsFactory);
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setBorder(null);
    }

    @Override
    public void subscribe() {
        super.subscribe();
        this.adrPostOpSubscription = MercuryStoreUI.adrPostOperationsComponentSubject.subscribe(target -> {
            if (this.cells.stream()
                    .map(AdrComponentPanel::getDescriptor)
                    .collect(Collectors.toList())
                    .size() != this.descriptor.getCells().size()) {
                this.removeAll();
                this.onViewInit();
                MercuryStoreCore.saveConfigSubject.onNext(true);
            }
            MercuryStoreUI.adrRepaintSubject.onNext(true);
        });
    }

    private void init() {
        switch (this.descriptor.getOrientation()) {
            case VERTICAL: {
                if (this.descriptor.getGroupType().equals(AdrTrackerGroupType.STATIC)) {
                    this.setLayout(new GridLayout(0, 1, this.descriptor.getHGap(), this.descriptor.getVGap()));
                } else {
                    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                }
                break;
            }
            case HORIZONTAL: {
                if (this.descriptor.getGroupType().equals(AdrTrackerGroupType.STATIC)) {
                    this.setLayout(new GridLayout(1, 0, this.descriptor.getHGap(), this.descriptor.getVGap()));
                } else {
                    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                }
                break;
            }
        }
        this.cells.forEach(item -> {
            item.setPreferredSize(this.descriptor.getSize());
            item.setLocation(this.descriptor.getLocation());
        });
        this.setLocation(descriptor.getLocation());
        MercuryStoreUI.adrRepaintSubject.onNext(true);
    }

    @Override
    public void onViewInit() {
        this.cells = new ArrayList<>();
        this.setLayout(new GridLayout());
        this.descriptor.getCells().forEach(component -> {
            AdrDurationCellPanel adrDurationCellPanel = new AdrDurationCellPanel((AdrDurationComponentDescriptor) component, this.componentsFactory);
            this.add(adrDurationCellPanel);
            this.cells.add(adrDurationCellPanel);
            if (inSettings) {
                adrDurationCellPanel.enableSettings();
            }
        });
        this.init();
    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        this.setVisible(true);
        this.cells.forEach(AdrComponentPanel::enableSettings);
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        this.cells.forEach(AdrComponentPanel::disableSettings);
    }

    @Override
    public void onSelect() {
    }

    @Override
    public void onUnSelect() {
    }

    @Override
    protected void onUpdate() {
        this.init();
    }

    @Override
    public void onDestroy() {
        this.adrPostOpSubscription.unsubscribe();
        this.cells.forEach(AdrComponentPanel::onDestroy);
    }
}
