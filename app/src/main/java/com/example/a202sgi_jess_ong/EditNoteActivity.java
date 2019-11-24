package com.example.a202sgi_jess_ong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {

    private EditText inputNote;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        inputNote = (EditText)findViewById(R.id.input_note);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.save_note)
            onSaveNote();
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {
        String text = inputNote.getText().toString();
        if (!text.isEmpty()){
            long date = new Date().getTime();
            Note note = new Note(text,date);
            mDatabaseReference.child(user.getUid()).setValue(note);

            finish();
        }

    }
}
