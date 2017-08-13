package com.mercury.platform.core.misc;

public enum WhisperNotifierStatus {
    ALWAYS {
        @Override
        public String asPretty() {
            return "Always play a sound";
        }
    },
    ALTAB {
        @Override
        public String asPretty() {
            return "Only when tabbed out";
        }
    },
    NONE {
        @Override
        public String asPretty() {
            return "Never";
        }
    };

    public abstract String asPretty();
    public static WhisperNotifierStatus valueOfPretty(String s){
        for (WhisperNotifierStatus status : WhisperNotifierStatus.values()) {
            if(status.asPretty().equals(s)){
                return status;
            }
        }
        return null;
    }
}
