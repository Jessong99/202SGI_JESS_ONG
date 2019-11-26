package com.example.a202sgi_jess_ong;

public class Note {
    private String noteText;
    private long noteDate;

    public Note() {
    }

    public Note(String noteText, long noteDate) {
        this.noteText = noteText;
        this.noteDate = noteDate;
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
}
