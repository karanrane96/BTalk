package com.example.android.chatmini;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainChatScreen extends AppCompatActivity {


    FloatingActionButton mainFab, addPeople, peopleList;
    Animation fabOpen, fabClose, rotateClockwise, rotateAntiClockwise;
    boolean isOpen = false;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private RecyclerView convoList;
    private DatabaseReference convoDB, userDB, msgDB;
    String currUser;


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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //Checking Auth
        mainFab = findViewById(R.id.floatBtn);
        addPeople = findViewById(R.id.float_add_ppl);
        peopleList = findViewById(R.id.float_ppl_list);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotateClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        rotateAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);
        convoList = findViewById(R.id.con_rec_view);
        convoList.setHasFixedSize(true);
        currUser = mAuth.getCurrentUser().getUid();
        convoDB = FirebaseDatabase.getInstance().getReference().child("Chat").child(currUser);
        msgDB = FirebaseDatabase.getInstance().getReference().child("messages").child(currUser);
        userDB = FirebaseDatabase.getInstance().getReference().child("Users");
        convoDB.keepSynced(true);
        userDB.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        convoList.setLayoutManager(linearLayoutManager);

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



     // for conversation view
        Query convoQuery = convoDB.orderByChild("timestamp");
        FirebaseRecyclerAdapter<Conversation, ConvoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Conversation, ConvoViewHolder>(
                Conversation.class,
                R.layout.single_user_list_view,
                ConvoViewHolder.class,
                convoQuery
        ) {
            @Override
            protected void populateViewHolder(final ConvoViewHolder viewHolder, Conversation model, int position) {
                final String list_user_id = getRef(position).getKey();

                userDB.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name, profilePic;
                        name = dataSnapshot.child("name").getValue().toString();
                        profilePic = dataSnapshot.child("profile_pic").getValue().toString();
                        Log.d("userData: ",name+" ... "+profilePic);
                        viewHolder.setNameh(name);
                        viewHolder.setProfile(profilePic);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("db ref",databaseError.toString());

                    }
                });

                Query lastMsgQuery = msgDB.child(list_user_id).limitToLast(1);
                lastMsgQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String msg = dataSnapshot.child("message").getValue().toString();
                        viewHolder.setMsg(msg);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("db ref",databaseError.toString());

                    }
                });

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //TODO friend chat
                        Intent chatInt = new Intent(MainChatScreen.this, UserChat.class);
                        chatInt.putExtra("userId", list_user_id);
                        chatInt.putExtra("currentId",currUser);
                        startActivity(chatInt);

                    }
                });
            }
        };

        convoList.setAdapter(firebaseRecyclerAdapter);
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
            case R.id.logout:
                mAuth.signOut();
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


    //for convo screen view
    public static class ConvoViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        public ConvoViewHolder(View itemView) {
            super(itemView);

            this.mView = itemView;
        }

        public void setProfile(String profile){
            CircleImageView profilePic = mView.findViewById(R.id.user_profile_pic);
            if (profile.length() > 0 && !profile.toString().equals("default")){
                new ImageLoadTask(profile, profilePic).execute();
            }

        }

        public void setNameh(String name){
            TextView nameTv;
            nameTv =  mView.findViewById(R.id.tx_name);
            nameTv.setText(name);


        }
        public void setMsg(String msg) {
            TextView msgView;
            msgView = mView.findViewById(R.id.user_desig);
            msgView.setText(msg);
        }
    }



    public static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }

}
