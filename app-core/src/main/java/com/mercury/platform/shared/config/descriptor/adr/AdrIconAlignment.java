package com.mercury.platform.shared.config.descriptor.adr;


public enum AdrIconAlignment {
    LEFT {
        @Override
        public String asPretty() {
            return "Left side";
        }
    },
    RIGHT {
        @Override
        public String asPretty() {
            return "Right side";
        }
    };

    public abstract String asPretty();
    public static AdrIconAlignment valueOfPretty(String s){
        for (AdrIconAlignment adrIconAlignment : AdrIconAlignment.values()) {
            if(adrIconAlignment.asPretty().equals(s)){
                return adrIconAlignment;
            }
        }
        return null;
    }
}
