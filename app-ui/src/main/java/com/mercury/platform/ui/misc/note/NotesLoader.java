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

        Note note1 = new Note("Overview","MercuryTrade is an overlay application for Path of Exile. It's designed to facilitate all possible trading interactions in the game providing a much better user experience for the players. It also has some other minor features such as Timer and Chat Scanner. \n\nThe program went through countless design interactions and made by active Path of Exile players who use this tool themselves on a regular basis.","app/app-icon.png",NoteLayout.VERTICAL);
        notes.add(note1);

        Note note3 = new Note("Notification (1/2)","When you receive a trade related message this notification panel pops up. It has all the information you might need to make a decision. Let's take a look.","notes/first/1.png",NoteLayout.VERTICAL);
        notes.add(note3);
        Note note4 = new Note("Notification (2/2)","Header: \nName, Age   {image:\"app/invite.png\"} Invite  {image:\"app/kick.png\"} Kick  {image:\"app/trade.png\"} Trade  {image:\"app/openChat.png\"} Message  {image:\"app/close.png\"} Dismiss \nItem name, Cost \nResponse buttons","notes/first/1.png",NoteLayout.VERTICAL);
        notes.add(note4);
        Note note5 = new Note("Quick response buttons","As you can see, these are fully customizable. Select an already existing button (or create a new one) -> label it -> write in your message.","notes/first/5.png",NoteLayout.VERTICAL);
        notes.add(note5);
        Note note6 = new Note("History","Each notification you receive is stored by MercuryTrade in a separate file. You won't be losing trade offers due to client crashes anymore!","notes/first/3.png",NoteLayout.HORIZONTAL);
        notes.add(note6);
        Note note7 = new Note("SuperTradeMode","By default (on the left) each consecutive notification you receive is folded to save screen space. SuperTradeMode is a switch that will force notification panels to be fully displayed. \nThis is useful if you need to manage a lot of trades with multiple people at once.","notes/first/4.png",NoteLayout.VERTICAL);
        notes.add(note7);
        Note note8 = new Note("Accessibility status","MercuryTrade's notifications have an indication for when a person comes in or leaves your hideout - that way you can trade with him as soon as possible. Especially useful for SuperTradeMode.","",NoteLayout.VERTICAL);
        notes.add(note8);
        Note note9 = new Note("Do not Disturb","Activate this mode if you don't want to receive notification pop-ups for a while. The messages still get stored in your history allowing you to access them later.","",NoteLayout.VERTICAL);
        notes.add(note9);
        Note note10 = new Note("Taskbar","" +
                "\n1) Do Not Disturb mode button switch. Activate this mode if you don't want to receive notifications." +
                "\n2) SuperTrade mode button switch. Activates / Deactivates SuperTrade Mode - each consecutive notification will be displayed folded / unfolded" +
                "\n3) Chat scanner. " +
                "\n4) Timer. A simple timer with a few counters" +
                "\n5) History. Open / Close your History panel" +
                "\n6) Drag mode. Allows you to reposition panels on the screen." +
                "\n7) Settings." +
                "\n8) Exit.",
                "notes/first/2.png",NoteLayout.VERTICAL);
        notes.add(note10);



        Note note11 = new Note("Other settings","Other settings todo","notes/first/6.png",NoteLayout.VERTICAL);
        notes.add(note11);
        return notes;
    }
}
