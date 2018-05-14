package com.example.android.chatmini;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
import com.roger.catloadinglibrary.CatLoadingView;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUser extends AppCompatActivity {

    private RecyclerView userList;
    private DatabaseReference userDatabase;
    public CatLoadingView avi;
    TextView loadingMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        setTitle("AlL users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userList = (RecyclerView) findViewById(R.id.user_rec_view);
        userList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        userList.setLayoutManager(linearLayoutManager);
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        avi = new CatLoadingView();
        avi.setCanceledOnTouchOutside(false);
        avi.setText("Loading users...");

       // avi.show(getSupportFragmentManager(), "");

//        loadingMsg = findViewById(R.id.progress_txt);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //finish();
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
                Intent profSetting = new Intent(AllUser.this, ProfileSetting.class);
                profSetting.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
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

    @Override
    protected void onStart() {
        super.onStart();
        //avi.show();
       // loadingMsg.setVisibility(View.VISIBLE);

       userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // avi.hide();
                //loadingMsg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelled","can");
            }
        });

        FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.single_user_list_view,
                UserViewHolder.class,
                userDatabase
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, Users model, int position) {
                Log.d("data",model.getName());
                viewHolder.setValues(model.getName(), model.getDesig(), model.getCompany(), model.getProfile_pic().toString());

                final String oppUserId = getRef(position).getKey();
                Log.d("getKey: ",oppUserId);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent chatInt = new Intent(AllUser.this, ProfilePage.class);
                        chatInt.putExtra("userId", oppUserId);
                        startActivity(chatInt);

                    }
                });
            }
        };

        userList.setAdapter(firebaseRecyclerAdapter);

    }



     public static class UserViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        public UserViewHolder(View itemView) {
            super(itemView);

            this.mView = itemView;
        }

        public void setValues(String nameh, String desigh, String comp, String profile){
            TextView nameTv, desigTv;
            nameTv =  mView.findViewById(R.id.tx_name);
            desigTv = mView.findViewById(R.id.user_desig);
            CircleImageView profilePic = mView.findViewById(R.id.user_profile_pic);
            nameTv.setText(nameh);
            desigTv.setText(desigh+"@"+comp);
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
