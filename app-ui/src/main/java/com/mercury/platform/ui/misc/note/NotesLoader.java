package com.mercury.platform.ui.misc.note;

import org.apache.tools.ant.taskdefs.condition.Not;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Константин on 20.01.2017.
 */
public class NotesLoader {
    public NotesLoader() {
    }
    public List<Note> getNotes(String path){
        List<Note> notes = new ArrayList<>();
        return testedNotes();
    }
    private List<Note> testedNotes(){
        List<Note> notes = new ArrayList<>();

        Note note1 = new Note("TEST","#######################\n#################\n###########################","notes/first/1.png",NoteLayout.HORIZONTAL);
        notes.add(note1);
        Note note2 = new Note("TEST","###################","",NoteLayout.HORIZONTAL);
        notes.add(note2);
        Note note3 = new Note("TEST","#######################\n#################\n","notes/first/2.png",NoteLayout.VERTICAL);
        notes.add(note3);
        return notes;
    }
}
