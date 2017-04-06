package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoundNotificationEvent implements MercuryEvent {
    private Float db = 0f;

    @NoArgsConstructor
    public static class WhisperSoundNotificationEvent extends SoundNotificationEvent {
        public WhisperSoundNotificationEvent(Float db) {
            super(db);
        }
    }
    @NoArgsConstructor
    public static class ChatScannerSoundNotificationEvent extends SoundNotificationEvent {
        public ChatScannerSoundNotificationEvent(Float db) {
            super(db);
        }
    }
    @NoArgsConstructor
    public static class ClicksSoundNotificationEvent extends SoundNotificationEvent {
        public ClicksSoundNotificationEvent(Float db) {
            super(db);
        }
    }
    @NoArgsConstructor
    public static class UpdateSoundNotificationEvent extends SoundNotificationEvent {
        public UpdateSoundNotificationEvent(Float db) {
            super(db);
        }
    }
}
