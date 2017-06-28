package com.mercury.platform.shared.config.descriptor;

import com.mercury.platform.core.misc.WhisperNotifierStatus;
import lombok.Data;

@Data
public class ApplicationDescriptor {
    private WhisperNotifierStatus notifierStatus;
    private int minOpacity;
    private int maxOpacity;
    private int fadeTime;
    private String gamePath;
    private boolean showPatchNotes;
    private boolean showOnStartUp;
    private boolean itemsGridEnable;
    private boolean checkOutUpdate;
    private boolean inGameDnd;
    private String dndResponseText;
}
