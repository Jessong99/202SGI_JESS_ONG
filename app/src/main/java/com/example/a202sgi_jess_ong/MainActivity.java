package com.example.a202sgi_jess_ong;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    Toolbar mToolbar;
    NavigationView mNavigationView;

    private FirebaseAuth mFirebaseAuth;

    //TODO : Change App Icon
    // TODO: 26-Nov-19 Change icon to ios icon

    private RecyclerView mRecyclerView;
    private ArrayList<Note> mList;
    private NoteAdapter mNotesAdapter;
    private DatabaseReference mDatabaseReference;

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

        //set up toolbar
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setOverflowIcon(getDrawable(R.drawable.overflow_icon));
        setSupportActionBar(mToolbar);

        //set up drawer menu
        mDrawerLayout = findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open_drawer,R.string.close_drawer);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();

        //set up navigationView
        mNavigationView = findViewById(R.id.navigationView);
        mNavigationView.setNavigationItemSelectedListener(this);

        //set up RecyclerView
        if (mFirebaseAuth.getCurrentUser() != null) {
            setUpRecyclerView();
        }

        //set up FAB
        final FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.bounce_btn);
                BounceBtnInterpolation btnInterpolation = new BounceBtnInterpolation(0.2, 20);
                animation.setInterpolator(btnInterpolation);
                fab.startAnimation(animation);
                onAddNewNote();
            }
        });

    }

    private void setUpRecyclerView() {
        mRecyclerView = findViewById(R.id.notes_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //todo check
        Context context = mRecyclerView.getContext();
        LayoutAnimationController controller = null;
        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_fall_right);

        mList = new ArrayList<Note>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Notes")
                .child(mFirebaseAuth.getCurrentUser().getUid());
        final LayoutAnimationController finalController = controller;
        mDatabaseReference.orderByChild("noteDate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mList = new ArrayList<Note>();
                Note n;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String noteID = dataSnapshot1.getKey();
                    n = dataSnapshot1.getValue(Note.class);
                    n.setNoteId(noteID);
                    mList.add(n);
                }
                mNotesAdapter = new NoteAdapter(MainActivity.this, mList);
                mRecyclerView.setAdapter(mNotesAdapter);
                mRecyclerView.setLayoutAnimation(finalController);
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mRecyclerView.scheduleLayoutAnimation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void onAddNewNote() {
        startActivity(new Intent(this, NewNoteActivity.class));
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
        }
        return super.onOptionsItemSelected(item);
    }

    //nav menu activity
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.home:
                // this will back to main page with list of note
                // TODO: 26-Nov-19 Some sign in and wont back stack to here
                // after sign in wont come here
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

}
