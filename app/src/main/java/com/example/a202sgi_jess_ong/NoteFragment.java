package com.example.a202sgi_jess_ong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class NoteFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<Note> mNotes;
    private NotesAdapter mNotesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        mRecyclerView = view.findViewById(R.id.notes_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadNotes();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 24-Nov-19 Add new Note
                onAddNewNote();
            }
        });

        return view;
    }

    private void loadNotes() {
        this.mNotes = new ArrayList<>();
        for (int i = 0; i < 12; i++){
            mNotes.add(new Note("this hi ajakhdhkasjbk", new Date().getTime()));
        }

        mNotesAdapter = new NotesAdapter(getContext(),mNotes);
        mRecyclerView.setAdapter(mNotesAdapter);
        //mNotesAdapter.notifyDataSetChanged();
    }

    private void onAddNewNote() {
        if(mNotes != null){
            mNotes.add(new Note("This is a note", new Date().getTime()));
        }
        if (mNotesAdapter != null){
            mNotesAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        loadNotes();
    }
}
