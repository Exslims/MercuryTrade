package com.mercury.platform.shared.entity.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"relatedMessages"})
public class NotificationDescriptor {
    private String sourceString;
    private String whisperNickname;
    private NotificationType type;
    private List<PlainMessageDescriptor> relatedMessages = new ArrayList<>();
}
