package com.mercury.platform.shared.entity.message;

import lombok.Data;

@Data
public class PlainMessageDescriptor {
    boolean incoming;
    String nickName;
    String message;
}
