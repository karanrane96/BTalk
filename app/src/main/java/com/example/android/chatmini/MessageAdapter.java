package com.example.android.chatmini;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by Karan on 15-05-2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>

{
    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;
    CircleImageView messageImage;


    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout ,parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;


        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);

        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
         mAuth=FirebaseAuth.getInstance();
        Messages c = mMessageList.get(i);
        try {
            Log.d("message", c.getMessage());
            viewHolder.messageText.setText(c.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String CurrentUser=mAuth.getCurrentUser().getUid();

       String from_user = c.getFrom();
        Log.d("db null?", from_user);

        if (from_user.equals(CurrentUser))
       {
        viewHolder.messageText.setBackgroundColor(WHITE);
        viewHolder.messageText.setTextColor(Color.BLACK);


       }
       else{
           viewHolder.messageText.setBackgroundResource(R.drawable.message_single_background);
           viewHolder.messageText.setTextColor(Color.WHITE);
       }
        String message_type = c.getType();
//
//
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
        Log.d("db null?", from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String prof = dataSnapshot.child("profile_pic").getValue().toString();
                //String image = dataSnapshot.child("thumb_image").getValue().toString();
                if (prof.length() > 0 && !prof.toString().equals("default")){
                    new ImageLoadTask(prof, viewHolder.profileImage).execute();
                }


                viewHolder.displayName.setText(name);

                //Picasso.with(viewHolder.profileImage.getContext()).load(image)
                //        .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals("text")) {

            try {
                viewHolder.messageText.setText(c.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //viewHolder.messageImage.setVisibility(View.INVISIBLE);


        } else {

            viewHolder.messageText.setVisibility(View.INVISIBLE);
            try {
                Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage())
                        .placeholder(R.drawable.ic_default_profile).into(viewHolder.messageImage);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
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
