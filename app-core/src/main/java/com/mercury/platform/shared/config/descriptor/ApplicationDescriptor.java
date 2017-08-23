package com.mercury.platform.shared.config.descriptor;

import com.mercury.platform.core.misc.WhisperNotifierStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApplicationDescriptor implements Serializable {
    private WhisperNotifierStatus notifierStatus;
    private int minOpacity;
    private int maxOpacity;
    private int fadeTime;
    private String gamePath;
    private boolean showOnStartUp;
    private boolean itemsGridEnable;
    private boolean checkOutUpdate;
}
