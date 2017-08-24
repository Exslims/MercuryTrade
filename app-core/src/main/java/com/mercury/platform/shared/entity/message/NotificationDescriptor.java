package com.mercury.platform.shared.entity.message;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class NotificationDescriptor {
    private String sourceString;
    private Date messageDate;
    private String whisperNickname;
    private NotificationType type;
    private List<PlainMessageDescriptor> relatedMessages = new ArrayList<>();
}
