package com.example.a202sgi_jess_ong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    Context mContext;
    ArrayList<Note> notes;

    public NoteAdapter(Context c, ArrayList<Note> n){
        mContext = c;
        notes = n;
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(mContext).inflate(R.layout.note_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.noteText.setText(notes.get(position).getNoteText());
        holder.noteDate.setText(NoteUtils.dateFromLong(notes.get(position).getNoteDate()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteText, noteDate;
        LinearLayout singleNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteText = (TextView) itemView.findViewById(R.id.note_text);
            noteDate = (TextView) itemView.findViewById(R.id.note_date);
            singleNote = (LinearLayout)itemView.findViewById(R.id.list_item);
        }

        public void onClick(int position){
            singleNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    
                }
            });
        }
    }
}
