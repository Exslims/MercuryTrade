package com.mercury.platform.shared.config.descriptor;

import com.mercury.platform.shared.entity.message.FlowDirections;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class NotificationSettingsDescriptor implements Serializable{
    private boolean incNotificationEnable = true;
    private boolean outNotificationEnable = true;
    private boolean scannerNotificationEnable = true;
    private int limitCount = 3;
    private int unfoldCount = 2;
    private FlowDirections flowDirections = FlowDirections.DOWNWARDS;
    private boolean dismissAfterKick = true;
    private boolean dismissAfterLeave = true;
    private boolean showLeague;
    private List<ResponseButtonDescriptor> buttons = new ArrayList<>();
    private List<ResponseButtonDescriptor> outButtons = new ArrayList<>();
    private String playerNickname = "Set up your nickname in settings";
}
