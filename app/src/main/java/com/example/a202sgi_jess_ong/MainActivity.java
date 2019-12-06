package com.example.a202sgi_jess_ong;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a202sgi_jess_ong.profile.ProfileFragment;
import com.example.a202sgi_jess_ong.profile.ViewPagerFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
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
    private long backPressedTime;
    private Toast backToast;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private RecyclerView mRecyclerView;
    private ArrayList<Note> mList;
    private NoteAdapter mNotesAdapter;

    int loggedIn = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth.getCurrentUser() == null) {
            loggedIn = 0;
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Please sign in to continue", Snackbar.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ViewPagerFragment()).addToBackStack(null).commit();
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
                //set up fab animation
                Animation animation = AnimationUtils.loadAnimation(getBaseContext(),R.anim.bounce_btn);
                BounceBtnInterpolation btnInterpolation = new BounceBtnInterpolation(0.2, 20);
                animation.setInterpolator(btnInterpolation);
                fab.startAnimation(animation);
                if (mFirebaseAuth.getCurrentUser() != null) {
                    //intent to create new note activity
                    startActivity(new Intent(MainActivity.this, NewNoteActivity.class));
                }else {
                    //request user to sign in
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Please sign in to continue", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpRecyclerView() {
        mRecyclerView = findViewById(R.id.notes_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //set up animation for recyclerView to display
        Context context = mRecyclerView.getContext();
        LayoutAnimationController controller = null;
        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_fall_right);

        mList = new ArrayList<Note>();
        //retrieve data from firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Notes")
                .child(mFirebaseAuth.getCurrentUser().getUid());
        final LayoutAnimationController finalController = controller;
        mDatabaseReference.keepSynced(true);

        //sort order by timestamp
        mDatabaseReference.orderByChild("noteDate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mList = new ArrayList<Note>();
                Note n;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    //get note id
                    String noteID = dataSnapshot1.getKey();
                    //get both note text and note date
                    n = dataSnapshot1.getValue(Note.class);
                    //save into array list
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

    //... menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        if (loggedIn == 1) {
            final SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setIconifiedByDefault(false);
            searchView.requestFocus();

            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    imm.showSoftInput(getCurrentFocus(), 0);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    imm.hideSoftInputFromWindow((IBinder) getParent(), 0);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mNotesAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }else {
            searchItem.setVisible(false);
        }
        return true;
    }

    //nav menu activity
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:
                // this will back to main page with list of note
                finish();
                startActivity(getIntent());
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.profile:
                if (mFirebaseAuth.getCurrentUser() == null){
                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).addToBackStack(null).commit();
                }
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
    public void onBackPressed() {
        //leave app if press back button 2 times within a certain time frame
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}
