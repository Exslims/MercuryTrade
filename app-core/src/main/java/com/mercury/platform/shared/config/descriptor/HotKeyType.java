package com.mercury.platform.shared.config.descriptor;


import com.mercury.platform.shared.IconConst;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum HotKeyType implements Serializable {
    N_TRADE_PLAYER {
        @Override
        public String getIconPath() {
            return IconConst.TRADE;
        }

        @Override
        public String getTooltip() {
            return "Offer trade";
        }
    },
    N_OPEN_CHAT {
        @Override
        public String getIconPath() {
            return IconConst.CHAT_OPEN;
        }

        @Override
        public String getTooltip() {
            return "Message this player";
        }
    },
    N_CLOSE_NOTIFICATION {
        @Override
        public String getIconPath() {
            return IconConst.CLOSE;
        }

        @Override
        public String getTooltip() {
            return "Close notification";
        }
    },
    //Incoming notification
    N_INVITE_PLAYER {
        @Override
        public String getIconPath() {
            return IconConst.INVITE;
        }

        @Override
        public String getTooltip() {
            return "Invite player";
        }
    },
    N_KICK_PLAYER {
        @Override
        public String getIconPath() {
            return IconConst.KICK;
        }

        @Override
        public String getTooltip() {
            return "Kick player";
        }
    },
    N_STILL_INTERESTING {
        @Override
        public String getIconPath() {
            return IconConst.STILL_INTERESTING;
        }

        @Override
        public String getTooltip() {
            return "Still interested button";
        }
    },
    N_REPEAT_MESSAGE {
        @Override
        public String getIconPath() {
            return IconConst.RELOAD_HISTORY;
        }

        @Override
        public String getTooltip() {
            return "Repeat message";
        }
    },
    N_SWITCH_CHAT {
        @Override
        public String getIconPath() {
            return IconConst.CHAT_HISTORY;
        }

        @Override
        public String getTooltip() {
            return "Chat history";
        }
    },
    //Outgoing/scanner notification
    N_VISITE_HIDEOUT {
        @Override
        public String getIconPath() {
            return IconConst.VISIT_HIDEOUT;
        }

        @Override
        public String getTooltip() {
            return "Visit player hideout";
        }
    },
    N_LEAVE {
        @Override
        public String getIconPath() {
            return IconConst.LEAVE;
        }

        @Override
        public String getTooltip() {
            return "Leave from party";
        }
    },
    N_BACK_TO_HIDEOUT {
        @Override
        public String getIconPath() {
            return IconConst.BACK_TO_HIDEOUT;
        }

        @Override
        public String getTooltip() {
            return null;
        }
    },
    N_WHO_IS {
        @Override
        public String getIconPath() {
            return IconConst.WHO_IS;
        }

        @Override
        public String getTooltip() {
            return "Who is?";
        }
    },
    //scanner
    N_QUICK_RESPONSE {
        @Override
        public String getIconPath() {
            return IconConst.CHAT_SCANNER_RESPONSE;
        }

        @Override
        public String getTooltip() {
            return "Quick response";
        }
    },
    T_TO_HIDEOUT {
        @Override
        public String getIconPath() {
            return IconConst.HIDEOUT;
        }

        @Override
        public String getTooltip() {
            return "To hideout";
        }
    },
    T_DND {
        @Override
        public String getIconPath() {
            return IconConst.VISIBLE_DND_MODE;
        }

        @Override
        public String getTooltip() {
            return "Dnd";
        }
    };

    public static boolean contains(HotKeyType entry) {
        return Arrays.stream(HotKeyType.values())
                       .filter(it -> it.equals(entry))
                       .collect(Collectors.toList())
                       .size() != 0;
    }

    public abstract String getIconPath();

    public abstract String getTooltip();
}
