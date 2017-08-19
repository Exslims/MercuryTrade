package com.mercury.platform.shared.config.descriptor;


import java.util.Arrays;
import java.util.stream.Collectors;

public enum HotKeyType {
    N_TRADE_PLAYER {
        @Override
        public String getIconPath() {
            return "app/trade.png";
        }
    },
    N_OPEN_CHAT {
        @Override
        public String getIconPath() {
            return "app/openChat.png";
        }
    },
    N_CLOSE_NOTIFICATION {
        @Override
        public String getIconPath() {
            return "app/close.png";
        }
    },
    //Incoming notification
    N_INVITE_PLAYER {
        @Override
        public String getIconPath() {
            return "app/invite.png";
        }
    },
    N_KICK_PLAYER {
        @Override
        public String getIconPath() {
            return "app/kick.png";
        }
    },
    N_STILL_INTERESTING {
        @Override
        public String getIconPath() {
            return "app/still-interesting.png";
        }
    },
    N_SWITCH_CHAT {
        @Override
        public String getIconPath() {
            return "app/chat_history.png";
        }
    },
    //Outgoing/scanner notification
    N_VISITE_HIDEOUT {
        @Override
        public String getIconPath() {
            return "app/visiteHideout.png";
        }
    },
    N_LEAVE {
        @Override
        public String getIconPath() {
            return "app/leave.png";
        }
    },
    N_BACK_TO_HIDEOUT {
        @Override
        public String getIconPath() {
            return "app/backToHideout.png";
        }
    },
    //scanner
    N_QUICK_RESPONSE {
        @Override
        public String getIconPath() {
            return "app/chat_scanner_response.png";
        }
    },
    T_TO_HIDEOUT {
        @Override
        public String getIconPath() {
            return "app/hideout.png";
        }
    },
    T_DND {
        @Override
        public String getIconPath() {
            return "app/visible-dnd-mode.png";
        }
    };
    public abstract String getIconPath();
    public static boolean contains(HotKeyType entry){
        return Arrays.stream(HotKeyType.values())
                .filter(it -> it.equals(entry))
                .collect(Collectors.toList())
                .size() != 0;
    }
}
