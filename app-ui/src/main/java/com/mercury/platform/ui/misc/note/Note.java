package com.mercury.platform.ui.misc.note;

import lombok.Data;

@Data
public class Note {
    private String title;
    private String text;
    private String imagePath;
    private NoteLayout layout;
}
