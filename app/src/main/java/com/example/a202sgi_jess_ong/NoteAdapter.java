package com.example.a202sgi_jess_ong;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements Filterable {

    Context mContext;
    ArrayList<Note> notes;
    ArrayList<Note> notesFull;

    public NoteAdapter(Context c, ArrayList<Note> n){
        mContext = c;
        notes = n;
        notesFull = new ArrayList<>(notes);
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(mContext).inflate(R.layout.note_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, final int position) {
        holder.noteText.setText(notes.get(position).getNoteText());
        holder.noteDate.setText(NoteUtils.dateFromLong(notes.get(position).getNoteDate()));
        holder.singleNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = notes.get(position).getNoteId();
                Intent intent = new Intent(mContext,NewNoteActivity.class);
                intent.putExtra("noteId",id);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteText, noteDate;
        LinearLayout singleNote;

        public NoteViewHolder(@NonNull final View itemView) {
            super(itemView);
            noteText = (TextView) itemView.findViewById(R.id.note_text);
            noteDate = (TextView) itemView.findViewById(R.id.note_date);
            singleNote = (LinearLayout)itemView.findViewById(R.id.list_item);
        }

    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(notesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Note item : notesFull) {
                    if (item.getNoteText().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notes.clear();
            notes.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
