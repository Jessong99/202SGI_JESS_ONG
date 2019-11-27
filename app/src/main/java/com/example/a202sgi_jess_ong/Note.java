package com.example.a202sgi_jess_ong;

public class Note {
    private String noteText;
    private long noteDate;
    private String noteId;

    public Note() {
    }

    public Note(String noteText, long noteDate, String noteId) {
        this.noteText = noteText;
        this.noteDate = noteDate;
        this.noteId = noteId;
    }

    public String getNoteText() { return noteText; }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }


    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
}
