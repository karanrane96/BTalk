package com.example.android.chatmini;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainChatScreen extends AppCompatActivity {
    FloatingActionButton mainFab, addPeople, peopleList;
    Animation fabOpen, fabClose, rotateClockwise, rotateAntiClockwise;
    boolean isOpen = false;
    private FirebaseAuth mAuth;
    private Button logoutButton;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat_screen);
        setTitle("BTolk");



        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                    startActivity(new Intent(MainChatScreen.this,LoginScreen.class));
            }
        };
        logoutButton=findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });


        //Checking Auth



        mainFab = findViewById(R.id.floatBtn);
        addPeople = findViewById(R.id.float_add_ppl);
        peopleList = findViewById(R.id.float_ppl_list);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotateClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        rotateAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthStateListener);
     if(currentUser== null)
     {
         Intent LoginScreenIntent= new Intent(MainChatScreen.this,LoginScreen.class);
         startActivity(LoginScreenIntent);
         finish();
     }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()) {
            case R.id.reqStatus:
                Toast.makeText(this,"req",Toast.LENGTH_LONG).show();
                break;
            case R.id.search:
                Toast.makeText(this,"search",Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_profile:
                Intent profSetting = new Intent(MainChatScreen.this, ProfileSetting.class);
                profSetting.putExtra("uid",mAuth.getCurrentUser().getUid().toString());
                startActivity(profSetting);
                break;
            case R.id.menu_req_st:
                Toast.makeText(this,"req in menu",Toast.LENGTH_LONG).show();
                break;
            default:
               return false;

        }

        return super.onOptionsItemSelected(menuItem);
    }

    public void onFabClick(View view) {
        Toast.makeText(this, "mainFab clicked", Toast.LENGTH_SHORT).show();
        if(isOpen) {
            addPeople.startAnimation(fabClose);
            peopleList.startAnimation(fabClose);
            mainFab.startAnimation(rotateAntiClockwise);
            addPeople.setClickable(false);
            peopleList.setClickable(false);
            isOpen = false;
        } else {
            addPeople.startAnimation(fabOpen);
            peopleList.startAnimation(fabOpen);
            mainFab.startAnimation(rotateClockwise);
            addPeople.setClickable(true);
            peopleList.setClickable(true);
            isOpen = true;
        }
    }

    //it is for all ppl
    public void onAddpplFabClick(View view){
        Intent intent = new Intent(getApplicationContext(), AllUser.class);
        startActivity(intent);
    }


    //it is for friend list
    public void onAllPplFabClick(View view){
        Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
        startActivity(intent);
    }



}
