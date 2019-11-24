package com.example.a202sgi_jess_ong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {

    private ArrayList<Note> notes;
    private Context mContext;

    public NotesAdapter(Context context, ArrayList<Note> notes) {
        this.mContext = context;
        this.notes = notes;
    }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.note_recyclerview,parent,false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        Note note = getNote(position);
        if (note != null){
            holder.noteText.setText(note.getNoteText());
            holder.noteDate.setText(NoteUtils.dateFromLong(note.getNoteDate()));

        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private Note getNote(int position){
        return notes.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder{

        TextView noteText, noteDate;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            noteDate = itemView.findViewById(R.id.note_date);
            noteText = itemView.findViewById(R.id.note_text);
        }
    }
}
