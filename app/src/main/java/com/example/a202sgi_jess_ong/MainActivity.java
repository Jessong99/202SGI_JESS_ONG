package com.example.a202sgi_jess_ong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    Toolbar mToolbar;
    NavigationView mNavigationView;

    private FirebaseAuth mFirebaseAuth;

    //TODO : Change App Icon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth.getCurrentUser() != null) {

            //TODO : set which fragment to use
            //choose which activity to run
            //inflater.inflate(R.layout.fragment_sign_in,container,false);
            //inflater.inflate(R.layout.fragment_register,container,false);
            //inflater.inflate(R.layout.fragment_profile,container,false);
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
                break;
            case R.id.about_us:
                Toast.makeText(this,"About Us",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AboutUsFragment()).commit();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
