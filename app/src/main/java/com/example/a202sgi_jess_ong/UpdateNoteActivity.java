package com.example.a202sgi_jess_ong;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UpdateNoteActivity extends AppCompatActivity {
    private Menu mMenu;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setOverflowIcon(getDrawable(R.drawable.overflow_icon));
        setSupportActionBar(mToolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note_menu,menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
