package com.mercury.platform.ui.frame.other;

import com.mercury.platform.shared.entity.message.PlainMessageDescriptor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;
import java.util.List;

@Data
@AllArgsConstructor
public class ChatHistoryDefinition {
    private List<PlainMessageDescriptor> messages;
    private Point location;
}
