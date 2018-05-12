package com.example.android.chatmini;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class MainChatScreen extends AppCompatActivity {
    FloatingActionButton mainFab, addPeople, peopleList;
    Animation fabOpen, fabClose, rotateClockwise, rotateAntiClockwise;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat_screen);
        setTitle("BTolk");
        mainFab = findViewById(R.id.floatBtn);
        addPeople = findViewById(R.id.float_add_ppl);
        peopleList = findViewById(R.id.float_ppl_list);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotateClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        rotateAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

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
                Toast.makeText(this,"profile_pic",Toast.LENGTH_LONG).show();
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

    public void onAddpplFabClick(View view){
        Toast.makeText(this, "add ppl fab clicked", Toast.LENGTH_SHORT).show();

    }

    public void onAllPplFabClick(View view){
        Toast.makeText(this, "all ppl fab clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AllUser.class);
        startActivity(intent);
    }

}
