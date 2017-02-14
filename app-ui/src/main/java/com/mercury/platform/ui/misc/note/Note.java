package com.mercury.platform.ui.misc.note;

/**
 * Created by Константин on 20.01.2017.
 */
public class Note {
    private String title;
    private String text;
    private String imagePath;
    private NoteLayout layout;

    public Note(){
    }
    public Note(String title,String text, String imagePath, NoteLayout layout) {
        this.title = title;
        this.text = text;
        this.imagePath = imagePath;
        this.layout = layout;
    }

    public String getText() {
        return text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public NoteLayout getLayout() {
        return layout;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setLayout(NoteLayout layout) {
        this.layout = layout;
    }
}
