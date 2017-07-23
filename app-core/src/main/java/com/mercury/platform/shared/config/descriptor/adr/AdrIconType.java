package com.mercury.platform.shared.config.descriptor.adr;


public enum AdrIconType {
    SQUARE {
        @Override
        public String asPretty() {
            return "Square";
        }
    },
    ELLIPSE {
        @Override
        public String asPretty() {
            return "Ellipse";
        }
    };

    public abstract String asPretty();
    public static AdrIconType valueOfPretty(String s){
        for (AdrIconType iconType : AdrIconType.values()) {
            if(iconType.asPretty().equals(s)){
                return iconType;
            }
        }
        return null;
    }
}
