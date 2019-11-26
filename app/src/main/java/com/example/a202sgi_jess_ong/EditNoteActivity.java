package com.example.a202sgi_jess_ong;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class EditNoteActivity extends AppCompatActivity {

    private EditText inputNote;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private static final String TAG = EditNoteActivity.class.getSimpleName();
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        inputNote = (EditText)findViewById(R.id.input_note);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(mFirebaseAuth.getCurrentUser().getUid());

        btnSave = (Button)findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = inputNote.getText().toString().trim();
                if (!TextUtils.isEmpty(text)){
                    onSaveNote(text);
                }else {
                    Snackbar.make(view,"It is a empty note",Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void onSaveNote(String text) {

        // TODO: Check xia (delete or not)
        /*FirebaseFirestore.getInstance()
                    .collection("notes")
                    .add(note)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "onSuccess: Successfully added the note... ");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditNoteActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
*/
        if (mFirebaseAuth.getCurrentUser() != null) {

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
                                Toast.makeText(EditNoteActivity.this, "Note Added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditNoteActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            mainThread.start();
        } else {
            Toast.makeText(this, "Please Sign In To Save Note", Toast.LENGTH_SHORT).show();
        }
        finish();

    }
}
