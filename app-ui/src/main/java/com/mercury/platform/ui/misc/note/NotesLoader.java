package com.mercury.platform.ui.misc.note;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class NotesLoader {
    private final Logger logger = LogManager.getLogger(NotesLoader.class.getSimpleName());

    public NotesLoader() {
    }

    public List<Note> getNotesOnFirstStart() {
        return getNotes("notes/first/first-start.json");
    }

    public List<Note> getPatchNotesFromString(String source) {
        return getNotesFromString(source);
    }

    public List<Note> getPatchNotes() {
        return getNotes("notes/patch/patch-notes.json");
    }

    public String getVersionFrom(String source) {
        String version = "";
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(source);
            version = (String) root.get("version");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return version;
    }

    private List<Note> getNotes(String filePath) {
        List<Note> notes = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filePath)));
            JSONArray notesArray = (JSONArray) root.get("notes");
            notes = getNotesList(notesArray);
        } catch (Exception e) {
            logger.error(e);
        }
        return notes;
    }

    private List<Note> getNotesFromString(String source) {
        List<Note> notes = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(source);
            JSONArray notesArray = (JSONArray) root.get("notes");
            notes = getNotesList(notesArray);
        } catch (Exception e) {
            logger.error(e);
        }
        return notes;
    }

    private List<Note> getNotesList(JSONArray notesArray) {
        List<Note> notes = new ArrayList<>();
        for (JSONObject next : (Iterable<JSONObject>) notesArray) {
            Note note = new Note();
            note.setTitle((String) next.get("title"));
            note.setText((String) next.get("text"));
            note.setImagePath((String) next.get("image"));
            note.setLayout(NoteLayout.valueOf((String) next.get("layout")));
            notes.add(note);
        }
        return notes;
    }
}
