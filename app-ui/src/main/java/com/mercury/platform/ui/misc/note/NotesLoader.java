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

        Note note1 = new Note("Overview","MercuryTrade is an overlay application for Path of Exile. It's designed to facilitate all possible trading interactions in the game providing a much better user experience for the players. This one minute overview will cover other features as well. \n\nAll future updates will be available for download from within the application!\n\nMercuryTrade is 100% legit: no macros, no injectors!","app/app-icon.png",NoteLayout.VERTICAL);
        notes.add(note1);

        Note note2 = new Note("Notification (1/2)","When you receive a trade related message this notification panel pops up. It has all the information you might need to make a decision. \n\nLet's take a look.","notes/first/1.png",NoteLayout.VERTICAL);
        notes.add(note2);

        Note note3 = new Note("Notification (2/2)","[new img request]","notes/first/1.png",NoteLayout.VERTICAL);
        notes.add(note3);

        Note note4 = new Note("Response buttons","Response buttons are fully customizable in settings. \nSelect an already existing button (or create a new one) -> label it -> write in your message.","notes/first/5.png",NoteLayout.VERTICAL);
        notes.add(note4);

        Note note5 = new Note("Accessibility status","MercuryTrade notifications have an indication for when a person comes in or leaves your hideout - that way you can trade with players as soon as possible (and keep track of them all!). \n\nEspecially useful because in-game stash UI takes priority over party frames.","",NoteLayout.VERTICAL);
        notes.add(note5);

        Note note6 = new Note("History","Each notification you receive is stored by MercuryTrade in a separate file. \nYou won't be losing trade offers due to client crashes or relogs anymore!","notes/first/3.png",NoteLayout.HORIZONTAL);
        notes.add(note6);

        Note note7 = new Note("SuperTradeMode","By default (on the left) each consecutive notification you receive is folded to save screen space. SuperTradeMode is a switch that will force notification panels to be fully displayed. \nEasier to manage multiple trades at once if needed.","notes/first/4.png",NoteLayout.VERTICAL);
        notes.add(note7);

        Note note8 = new Note("Do not Disturb","Activate this mode if you don't want to receive notification pop-ups for a while. The messages still get stored in your history allowing you to access them later.","",NoteLayout.VERTICAL);
        notes.add(note8);

        Note note9 = new Note("Taskbar panel","" +
                "\n1) Do Not Disturb mode button switch." +
                "\n2) SuperTrade mode button switch."+
                "\n3) Chat scanner." +
                "\n4) Timer." +
                "\n5) History." +
                "\n6) Drag mode." +
                "\n7) Settings." +
                "\n8) Exit.",
                "notes/first/2.png",NoteLayout.VERTICAL);
        notes.add(note9);

        Note note10 = new Note("Settings","Tailor Automatic notification fading, Transparency levels and Sound alerts to your taste.","notes/first/6.png",NoteLayout.VERTICAL);
        notes.add(note10);

        Note note11 = new Note("Feedback and suggestions","MercuryTrade(centered and bigger) \n\nThat's it! We expect you to enjoy the app as much as we do! \nMake sure to send us your feedback and suggestions. \n\nThanks!","app/app-icon.png",NoteLayout.VERTICAL);
        notes.add(note11);
        return notes;
    }
}
