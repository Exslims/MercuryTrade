package com.mercury.platform.shared.entity.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MercuryError {
    private String errorMessage;
    private Throwable stackTrace;

    public MercuryError(Throwable stackTrace) {
        this.stackTrace = stackTrace;
    }
}
