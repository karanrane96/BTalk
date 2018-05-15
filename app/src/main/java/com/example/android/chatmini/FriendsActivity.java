package com.example.android.chatmini;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView frndList;
    private DatabaseReference frndDB, userDB;
    String currUser;
//    public AVLoadingIndicatorView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        setTitle("Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        frndList = (RecyclerView) findViewById(R.id.frnd_rec_view);
        frndList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        frndList.setLayoutManager(linearLayoutManager);

        currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        frndDB = FirebaseDatabase.getInstance().getReference().child("Friends").child("");
        frndDB.keepSynced(true);
        userDB = FirebaseDatabase.getInstance().getReference().child("Users");
        userDB.keepSynced(true);
        //        progressBar = (AVLoadingIndicatorView) findViewById(R.id.progress_bar);


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
                Intent profSetting = new Intent(FriendsActivity.this, ProfileSetting.class);
                profSetting.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                startActivity(profSetting);
                break;
            case R.id.menu_req_st:
                Toast.makeText(this,"req in menu",Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                break;

            default:
                return false;

        }

        return super.onOptionsItemSelected(menuItem);
    }

    //main logic

    @Override
    protected void onStart() {
        super.onStart();

//        progressBar.show();
        // loadingMsg.setVisibility(View.VISIBLE);

        frndDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                progressBar.hide();
                //loadingMsg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled","can");
            }
        });

        FirebaseRecyclerAdapter<Friend,FrndViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friend, FrndViewHolder>(
                Friend.class,
                R.layout.single_user_list_view,
                FrndViewHolder.class,
                frndDB
        ) {

            @Override
            protected void populateViewHolder(final FrndViewHolder viewHolder, Friend model, int position) {

                final String date = model.getDate();
                final String list_user_id = getRef(position).getKey();

                userDB.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name, profilePic;
                        name = dataSnapshot.child("name").getValue().toString();
                        profilePic = dataSnapshot.child("profile_pic").getValue().toString();
                        viewHolder.setValues(name, date, profilePic);
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
                        Intent chatInt = new Intent(FriendsActivity.this, UserChat.class);
                        chatInt.putExtra("userId", list_user_id);
                        chatInt.putExtra("currentId",currUser);
                        startActivity(chatInt);

                    }
                });
            }
        };

        frndList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class FrndViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        public FrndViewHolder(View itemView) {
            super(itemView);

            this.mView = itemView;
        }


        public void setValues(String nameh, String date, String profile) {
            TextView nameTv, desigTv;
            nameTv =  mView.findViewById(R.id.tx_name);
            desigTv = mView.findViewById(R.id.user_desig);
            CircleImageView profilePic = mView.findViewById(R.id.user_profile_pic);
            nameTv.setText(nameh);
            desigTv.setText("since "+date);
            if (profile.length() > 0 && !profile.toString().equals("default")){
                new ImageLoadTask(profile, profilePic).execute();
                //profilePic.setImageURI(mUri);
            }

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
