package com.mercury.platform.shared.config.descriptor;


import java.util.Arrays;
import java.util.stream.Collectors;

public enum HotKeyType {
    INVITE_PLAYER {
        @Override
        public String getIconPath() {
            return "app/invite.png";
        }
    },
    TRADE_PLAYER {
        @Override
        public String getIconPath() {
            return "app/trade.png";
        }
    },
    KICK_PLAYER {
        @Override
        public String getIconPath() {
            return "app/kick.png";
        }
    },
    STILL_INTERESTING {
        @Override
        public String getIconPath() {
            return "app/still-interesting.png";
        }
    },
    CLOSE_NOTIFICATION {
        @Override
        public String getIconPath() {
            return "app/close.png";
        }
    };
//    EXPAND_ALL {
//        @Override
//        public String getIconPath() {
//            return null;
//        }
//    };

    public abstract String getIconPath();
    public static boolean contains(String entry){
        return Arrays.stream(HotKeyType.values())
                .filter(it -> it.name().equals(entry))
                .collect(Collectors.toList())
                .size() != 0;
    }
}
