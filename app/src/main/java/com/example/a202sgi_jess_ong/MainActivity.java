package com.example.a202sgi_jess_ong;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a202sgi_jess_ong.profile.ProfileFragment;
import com.example.a202sgi_jess_ong.profile.SignInFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    Toolbar mToolbar;
    NavigationView mNavigationView;

    private FirebaseAuth mFirebaseAuth;

    //TODO : Change App Icon

    private RecyclerView mRecyclerView;
    private ArrayList<Note> mNotes;
    private NotesAdapter mNotesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth.getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SignInFragment()).addToBackStack(null).commit();
        }

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //set ... as white color
        mToolbar.setOverflowIcon(getDrawable(R.drawable.overflow_icon));
        setSupportActionBar(mToolbar);

        //drawer menu settings
        mDrawerLayout = findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open_drawer,R.string.close_drawer);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();

        //nav setting
        mNavigationView = findViewById(R.id.navigationView);
        mNavigationView.setNavigationItemSelectedListener(this);

        //RecyclerView
        mRecyclerView = findViewById(R.id.notes_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadNotes();
        //FAB
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddNewNote();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Note, NotesAdapter.NoteHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Note, NotesAdapter.NoteHolder>() {
            NotesAdapter.NoteHolder.

            @Override
            protected void onBindViewHolder(@NonNull NotesAdapter.NoteHolder noteHolder, int i, @NonNull Note note) {
                Note
            }

            @NonNull
            @Override
            public NotesAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        }}

    private void loadNotes() {
        this.mNotes = new ArrayList<>();


        mNotesAdapter = new NotesAdapter(this,mNotes);
        mRecyclerView.setAdapter(mNotesAdapter);
        //todo mNotesAdapter.notifyDataSetChanged();
    }

    private void onAddNewNote() {
        startActivity(new Intent(this,EditNoteActivity.class));
    }

    //... menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    //activity for ... menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort:
                Toast.makeText(this,"Sort Btn Clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this,"Setting Btn Clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact:
                Toast.makeText(this,"Contact Btn Clicked",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //nav menu activity
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.home:
                // TODO: set Back to main activity
                FragmentManager fm = getSupportFragmentManager();
                if(fm.getBackStackEntryCount()>0) {
                    fm.popBackStack();
                }
                break;
            case R.id.profile:
                // TODO: 26-Nov-19 Check all backstack Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).addToBackStack(null).commit();
                break;
            case R.id.about_us:
                Toast.makeText(this,"About Us",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AboutUsFragment()).addToBackStack(null).commit();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotes();
    }


}
