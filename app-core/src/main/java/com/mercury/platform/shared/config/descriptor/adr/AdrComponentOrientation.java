package com.mercury.platform.shared.config.descriptor.adr;


public enum AdrComponentOrientation {
    HORIZONTAL {
        @Override
        public String asPretty() {
            return "Horizontal";
        }
    },
    VERTICAL {
        @Override
        public String asPretty() {
            return "Vertical";
        }
    };

    public static AdrComponentOrientation valueOfPretty(String s) {
        for (AdrComponentOrientation orientation : AdrComponentOrientation.values()) {
            if (orientation.asPretty().equals(s)) {
                return orientation;
            }
        }
        return null;
    }

    public abstract String asPretty();
}
