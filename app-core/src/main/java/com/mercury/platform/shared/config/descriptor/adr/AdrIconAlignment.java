package com.mercury.platform.shared.config.descriptor.adr;


public enum AdrIconAlignment {
    LEFT {
        @Override
        public String asPretty() {
            return "Left";
        }
    },
    RIGHT {
        @Override
        public String asPretty() {
            return "Right";
        }
    },
    TOP {
        @Override
        public String asPretty() {
            return "Top";
        }
    },
    BOTTOM {
        @Override
        public String asPretty() {
            return "Bottom";
        }
    };

    public static AdrIconAlignment valueOfPretty(String s) {
        for (AdrIconAlignment adrIconAlignment : AdrIconAlignment.values()) {
            if (adrIconAlignment.asPretty().equals(s)) {
                return adrIconAlignment;
            }
        }
        return null;
    }

    public abstract String asPretty();
}
