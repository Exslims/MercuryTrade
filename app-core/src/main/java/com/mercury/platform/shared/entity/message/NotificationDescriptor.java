package com.mercury.platform.shared.entity.message;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationDescriptor {
    private String sourceString;
    private Date messageDate;
    private String whisperNickname;
    private NotificationType type;
}
