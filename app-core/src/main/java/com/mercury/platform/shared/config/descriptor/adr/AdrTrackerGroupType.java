package com.mercury.platform.shared.config.descriptor.adr;


public enum AdrTrackerGroupType {
    DYNAMIC {
        @Override
        public String asPretty() {
            return "Dynamic";
        }
    },
    STATIC {
        @Override
        public String asPretty() {
            return "Static";
        }
    };

    public static AdrTrackerGroupType valueOfPretty(String s) {
        for (AdrTrackerGroupType adrTrackerGroupType : AdrTrackerGroupType.values()) {
            if (adrTrackerGroupType.asPretty().equals(s)) {
                return adrTrackerGroupType;
            }
        }
        return null;
    }

    public abstract String asPretty();
}
