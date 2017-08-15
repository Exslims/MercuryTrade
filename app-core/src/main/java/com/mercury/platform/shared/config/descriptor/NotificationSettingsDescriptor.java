package com.mercury.platform.shared.config.descriptor;

import com.mercury.platform.shared.entity.message.FlowDirections;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NotificationSettingsDescriptor implements Serializable{
    private boolean notificationEnable;
    private int limitCount;
    private int unfoldCount;
    private FlowDirections flowDirections;
    private boolean dismissAfterKick;
    private boolean showLeague;
    private List<ResponseButtonDescriptor> buttons;
}
