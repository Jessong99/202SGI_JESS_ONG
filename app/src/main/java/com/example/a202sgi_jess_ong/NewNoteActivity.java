package com.example.a202sgi_jess_ong;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class NewNoteActivity extends AppCompatActivity {

    private EditText inputNote;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    private Menu mMenu;
    Toolbar mToolbar;
    AlertDialog.Builder builder;
    private String noteID=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try {
            //get noteId from Intent
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
        mDatabaseReference.keepSynced(true);

        if (noteID!= null){
            showCurrentData();
        }
        inputNote.setSelection(inputNote.getText().length());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.copy:
                        copyNote();
                        break;
                    case R.id.shareNote:
                        shareNote();
                        break;
                    case R.id.delete_note:
                        if (noteID != null) {
                            deleteNote();
                        }else {
                            finish();
                        }
                        break;
                    case R.id.qrCode:
                        generateQRCode();
                        break;
                }
                return false;
            }
        });
    }


    private void showCurrentData() {
        mDatabaseReference.child(noteID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("noteText").getValue()!=null) {
                    inputNote.setText(dataSnapshot.child("noteText").getValue().toString());
                    inputNote.setSelection(inputNote.getText().length());
                }

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
                if (noteID!= null){
                    unsavedNote();
                }
                else{
                    finish();
                }
                break;
            case R.id.save_note:
            case R.id.update_note:
                saveNote();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareNote() {
        String shareBody = inputNote.getText().toString().trim();
        if (!TextUtils.isEmpty(shareBody)) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Note");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }else {
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "It is a empty note", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void unsavedNote() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
        builder.setMessage("Do you want to save your changes ?")
                .setTitle("You have unsaved changes for this note.")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveNote();
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User select cancel and close the dialog box
                    }
                })
                .setNeutralButton("Don't Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder.create();
        builder.show();
    }

    private void saveNote(){
        if (mFirebaseAuth.getCurrentUser() != null) {
            //get edit text
            String text = inputNote.getText().toString().trim();
            //hide Soft Input From Window
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            if (!TextUtils.isEmpty(text)) {
                if (noteID != null) {
                    //update current note
                    final Map updateNoteMap = new HashMap();
                    updateNoteMap.put("noteText", text);
                    updateNoteMap.put("noteDate", ServerValue.TIMESTAMP);
                    mDatabaseReference.child(noteID).updateChildren(updateNoteMap);

                    //notify user the note is updated
                    Toast.makeText(NewNoteActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //create new note
                    final DatabaseReference newNoteRef = mDatabaseReference.push();
                    final Map noteMap = new HashMap();
                    noteMap.put("noteText", text);
                    noteMap.put("noteDate", ServerValue.TIMESTAMP);
                    //save data into Firebase
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
                .setIcon(R.drawable.ic_delete_black_24dp)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDatabaseReference.child(noteID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(NewNoteActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                                    finish();//back to note list
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

    private void copyNote(){
        String text = inputNote.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("note text",inputNote.getText());
            clipboard.setPrimaryClip(clip);
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "The note is copied.", Snackbar.LENGTH_SHORT).show();
        }
        else {
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "It is a empty note", Snackbar.LENGTH_SHORT).show();
        }
    }
    
    private void generateQRCode() {
        String text = inputNote.getText().toString().trim();
        Bitmap bitmap = null;
        //check if the note is empty
        if (!TextUtils.isEmpty(text)) {
            QRGEncoder qrgEncoder = new QRGEncoder(text, null, QRGContents.Type.TEXT, 1000);
            try {
                // Getting QR-Code as Bitmap
                bitmap = qrgEncoder.encodeAsBitmap();
                // set up new dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
                LayoutInflater inflater = NewNoteActivity.this.getLayoutInflater();
                View view = inflater.inflate(R.layout.qr_code_dialog, null);
                ImageView imageView = (ImageView)view.findViewById(R.id.imageView4);
                // set image view from qr code bitmap
                imageView.setImageBitmap(bitmap);
                builder.setView(view)
                        .setTitle("QR Code")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                builder.create();
                builder.show();
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        else {
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "It is a empty note", Snackbar.LENGTH_SHORT).show();
        }
    }
}
