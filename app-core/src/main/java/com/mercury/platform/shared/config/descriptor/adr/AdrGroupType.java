package com.mercury.platform.shared.config.descriptor.adr;


public enum AdrGroupType {
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

    public abstract String asPretty();
    public static AdrGroupType valueOfPretty(String s){
        for (AdrGroupType adrGroupType : AdrGroupType.values()) {
            if(adrGroupType.asPretty().equals(s)){
                return adrGroupType;
            }
        }
        return null;
    }
}
