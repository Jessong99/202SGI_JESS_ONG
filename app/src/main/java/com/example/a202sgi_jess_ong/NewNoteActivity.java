package com.example.a202sgi_jess_ong;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewNoteActivity extends AppCompatActivity {

    private EditText inputNote;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    private Menu mMenu;
    Toolbar mToolbar;
    AlertDialog.Builder builder;
    private String noteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        try {
            noteID = getIntent().getStringExtra("noteId");
        }catch (Exception e){
            e.printStackTrace();
        }

        //set up toolbar
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setOverflowIcon(getDrawable(R.drawable.overflow_icon));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputNote = (EditText)findViewById(R.id.input_note);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(mFirebaseAuth.getCurrentUser().getUid());

        if (noteID!= null){
            showCurrentData();
        }
    }

    private void showCurrentData() {
        mDatabaseReference.child(noteID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inputNote.setText(dataSnapshot.child("noteText").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (noteID!= null){
            getMenuInflater().inflate(R.menu.edit_note_menu,menu);
        }else {
            getMenuInflater().inflate(R.menu.new_note_menu,menu);
        }
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.delete_note:
                if (noteID!= null){
                    deleteNote();
                }
                break;
            case R.id.save_note:
                saveNote();
                break;
            case R.id.update_note:
                saveNote();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote(){
        if (mFirebaseAuth.getCurrentUser() != null) {
            String text = inputNote.getText().toString().trim();
            if (!TextUtils.isEmpty(text)) {
                if (noteID != null) {
                    //update current note
                    final Map updateNoteMap = new HashMap();
                    updateNoteMap.put("noteText", text);
                    updateNoteMap.put("noteDate", ServerValue.TIMESTAMP);
                    mDatabaseReference.child(noteID).updateChildren(updateNoteMap);
                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Updated", Snackbar.LENGTH_SHORT).show();
                } else {
                    //create new note
                    final DatabaseReference newNoteRef = mDatabaseReference.push();
                    final Map noteMap = new HashMap();
                    noteMap.put("noteText", text);
                    noteMap.put("noteDate", ServerValue.TIMESTAMP);

                    Thread mainThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(NewNoteActivity.this, "Note Added", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(NewNoteActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    mainThread.start();
                    finish();
                }
            } else {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "It is a empty note", Snackbar.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(NewNoteActivity.this,"Please sign in to save note.",Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteNote(){
        //alert box before delete action
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_delete_title);
        builder.setMessage(R.string.alert_delete_text)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDatabaseReference.child(noteID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(NewNoteActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Log.e("NewNoteActivity", task.getException().toString());
                                    Toast.makeText(NewNoteActivity.this,"ERROR: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User select cancel and close the dialog box
                    }
                });
        builder.create();
        builder.show();
    }
}
