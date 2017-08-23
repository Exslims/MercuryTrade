package com.mercury.platform.ui.adr.components.panel.ui.impl;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentOrientation;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconAlignment;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;


public enum ProgressBarUI implements ProgressBarUIFactory {
    HORIZONTAL_LEFT_ICON {
        @Override
        public boolean isSuitable(AdrProgressBarDescriptor descriptor) {
            return descriptor.isIconEnable()
                    && descriptor.getIconAlignment().equals(AdrIconAlignment.LEFT)
                    && descriptor.getOrientation().equals(AdrComponentOrientation.HORIZONTAL);
        }

        @Override
        public MercuryProgressBarTrackerUI getUI() {
            return new LIconHProgressBarTrackerUI();
        }
    },
    HORIZONTAL_RIGHT_ICON {
        @Override
        public boolean isSuitable(AdrProgressBarDescriptor descriptor) {
            return descriptor.isIconEnable()
                    && descriptor.getIconAlignment().equals(AdrIconAlignment.RIGHT)
                    && descriptor.getOrientation().equals(AdrComponentOrientation.HORIZONTAL);
        }

        @Override
        public MercuryProgressBarTrackerUI getUI() {
            return new RIconHProgressBarTrackerUI();
        }
    },
    VERTICAL_TOP_ICON {
        @Override
        public boolean isSuitable(AdrProgressBarDescriptor descriptor) {
            return descriptor.isIconEnable()
                    && descriptor.getIconAlignment().equals(AdrIconAlignment.TOP)
                    && descriptor.getOrientation().equals(AdrComponentOrientation.VERTICAL);
        }

        @Override
        public MercuryProgressBarTrackerUI getUI() {
            return new TIconVProgressBarTrackerUI();
        }
    },
    VERTICAL_BOTTOM_ICON {
        @Override
        public boolean isSuitable(AdrProgressBarDescriptor descriptor) {
            return descriptor.isIconEnable()
                    && descriptor.getIconAlignment().equals(AdrIconAlignment.BOTTOM)
                    && descriptor.getOrientation().equals(AdrComponentOrientation.VERTICAL);
        }

        @Override
        public MercuryProgressBarTrackerUI getUI() {
            return new BIconVProgressBarTrackerUI();
        }
    },
    HORIZONTAL {
        @Override
        public boolean isSuitable(AdrProgressBarDescriptor descriptor) {
            return !descriptor.isIconEnable() && descriptor.getOrientation().equals(AdrComponentOrientation.HORIZONTAL);
        }

        @Override
        public MercuryProgressBarTrackerUI getUI() {
            return new MercuryProgressBarTrackerUI();
        }
    },
    VERTICAL {
        @Override
        public boolean isSuitable(AdrProgressBarDescriptor descriptor) {
            return !descriptor.isIconEnable() && descriptor.getOrientation().equals(AdrComponentOrientation.VERTICAL);
        }

        @Override
        public MercuryProgressBarTrackerUI getUI() {
            return new VProgressBarTrackerUI();
        }
    };

    public static MercuryProgressBarTrackerUI getUIBy(AdrProgressBarDescriptor descriptor) {
        for (ProgressBarUI progressBarUI : ProgressBarUI.values()) {
            if (progressBarUI.isSuitable(descriptor)) {
                return progressBarUI.getUI();
            }
        }
        return new MercuryProgressBarTrackerUI();
    }
}
