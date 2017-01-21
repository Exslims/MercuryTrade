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

        Note note1 = new Note("Overview","MercuryTrade is a Path of Exile overlay application. It's designed to facilitate all the possible trading interactions and within the game providing a much better user experience for the players. The program went through countless design interactions and made by active Path of Exile players who use this tool themselves on a regular basis.","app/app-icon.png",NoteLayout.VERTICAL);
        notes.add(note1);
        Note note2 = new Note("Messages","When you receive a trade related message this notification panel pops up. It has all the information you might need to make a decision.","notes/first/1.png",NoteLayout.VERTICAL);
        notes.add(note2);
        Note note3 = new Note("Task bar","###################","notes/first/2.png",NoteLayout.VERTICAL);
        notes.add(note3);
        Note note4 = new Note("History","Each notification you receive is stored by MercuryTrade. History panel allows you to access that file in a useful way to interact with the customers.","notes/first/3.png",NoteLayout.HORIZONTAL);
        notes.add(note4);
        Note note5 = new Note("Trade modes","Changing trade modes forces notification panels to NOT stack like cards - all notification you receive will be displayed unfolded.","notes/first/4.png",NoteLayout.VERTICAL);
        notes.add(note5);
        Note note6 = new Note("Quick response buttons","Select an existing button or create a new one -> label it -> write in your message.","notes/first/5.png",NoteLayout.VERTICAL);
        notes.add(note6);
        Note note7 = new Note("Other customization settings","Other settings todo","notes/first/6.png",NoteLayout.VERTICAL);
        notes.add(note7);
        return notes;
    }
}
