package com.example.android.chatmini;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePage extends AppCompatActivity {

    public TextView status, userName, company, designation, email;
    CircleImageView profPic;
    Button sendReqBtn, decReqBtn;
    String uName, uCompany, uDesig, uEmail, uid, profilePic, userId;
    int frndshpStatus; // 0 = not frnd, 1 = sent, 2 = rec, 3 = frnd
    DatabaseReference databaseReference, userDb, frndReqDb, frndDb;
   // AVLoadingIndicatorView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userId = getIntent().getStringExtra("userId");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userDb = databaseReference.child("Users").child(userId);
        frndReqDb = databaseReference.child("Friend_req");
        frndDb = databaseReference.child("Friends");

        frndshpStatus = 0;
        status = findViewById(R.id.user_status);
        userName = findViewById(R.id.user_name);
        company = findViewById(R.id.user_company);
        designation = findViewById(R.id.user_desig);
        email = findViewById(R.id.user_email);
        profPic = findViewById(R.id.profile_pic);
        sendReqBtn = findViewById(R.id.send_req_btn);
        decReqBtn = findViewById(R.id.dec_req_btn);
        decReqBtn.setVisibility(View.INVISIBLE);
       // progress = findViewById(R.id.progress_bar);
        //progress.show();

        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uName = dataSnapshot.child("name").getValue().toString();
                uCompany = dataSnapshot.child("company").getValue().toString();
                uDesig = dataSnapshot.child("desig").getValue().toString();
                uEmail = dataSnapshot.child("email").getValue().toString();
                profilePic = dataSnapshot.child("profile_pic").getValue().toString();
                if(!profPic.equals("default") && profilePic.length() > 0){
                    new ImageLoadTask(profilePic, profPic).execute();
                }
                userName.setText(uName);
                setTitle(uName);
                company.setText(uCompany);
                email.setText(uEmail);
                designation.setText(uDesig);
                status.setText(uDesig+" @ "+uCompany);

                //Frnd req or Frnds
                frndReqDb.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(userId)){
                            String reqType = dataSnapshot.child(userId).child("request_type").getValue().toString();
                            if ((reqType.equals("received"))){
                                sendReqBtn.setText("Accept");
                                frndshpStatus = 2; // rec = 2
                                sendReqBtn.setEnabled(true);
                                decReqBtn.setVisibility(View.VISIBLE);
                            }else if (reqType.equals("sent")){
                                sendReqBtn.setText("Cancel");
                                frndshpStatus = 1; // sent = 1
                                sendReqBtn.setEnabled(true);
                            }
                        } else {
                            frndDb.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(userId)){

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d("db ref",databaseReference.toString());
                                }
                            });
                        }
                        //progress.hide();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReqBtn.setEnabled(false);
                 // not_frnd : status = 0
                if (frndshpStatus == 0){
                    Map reqMap = new HashMap();
                    reqMap.put(uid + "/" + userId +"/"+ "request_type","sent");
                    reqMap.put(userId + "/" + uid +"/"+ "request_type","received");
                    frndReqDb.updateChildren(reqMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null){
                                Log.d("db Err",databaseError.toException().toString());
                            }else {
                                sendReqBtn.setText("Cancel");
                                frndshpStatus = 1; // sent = 1
                                sendReqBtn.setEnabled(true);
                            }
                        }
                    });
                }
                // sent : status = 1
                if (frndshpStatus == 1){
                    Map reqMap = new HashMap();
                    reqMap.put(uid + "/" + userId + "/request_type",null);
                    reqMap.put(userId + "/" + uid + "/request_type",null);
                    frndReqDb.updateChildren(reqMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(final DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null){
                                Log.d("db Err",databaseError.toException().toString());
                            }else {

                                sendReqBtn.setText("Send");
                                frndshpStatus = 0; // not_frnd = 0
                                sendReqBtn.setEnabled(true);
                            }
                        }
                    });
                }

                // 2 = recd
                if (frndshpStatus == 2){
                    decReqBtn.setVisibility(View.VISIBLE);
                    String date = DateFormat.getDateInstance().format(new Date());

                    Map reqMap = new HashMap();
                    reqMap.put(uid + "/" + userId+"/date",date);
                    reqMap.put(userId + "/" + uid + "/date" ,date);
                    frndDb.updateChildren(reqMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null){
                                Log.d("db Err",databaseError.toException().toString());
                            }else {
                                sendReqBtn.setText("Unfriend");
                                frndshpStatus = 3; // frnd = 3
                                sendReqBtn.setEnabled(true);
                                decReqBtn.setVisibility(View.INVISIBLE);

                                Map reqMap1 = new HashMap();
                                reqMap1.put(uid + "/" + userId + "/request_type",null);
                                reqMap1.put(userId + "/" + uid + "/request_type",null);
                                frndReqDb.updateChildren(reqMap1);

                            }
                        }
                    });
                }

                //3 = already frnd
                if (frndshpStatus == 3 ){
                    decReqBtn.setVisibility(View.INVISIBLE);


                    Map unFrndMap = new HashMap();
                    unFrndMap.put(uid + "/" + userId,null);
                    unFrndMap.put(userId + "/" + uid ,null);

                    frndDb.updateChildren(unFrndMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null){
                                Log.d("db Err",databaseError.toException().toString());
                            }else {
                                sendReqBtn.setText("Send");
                                frndshpStatus = 0 ; // frnd = 3
                                sendReqBtn.setEnabled(true);

                            }
                        }
                    });
                }
            }
        });

        decReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(frndshpStatus == 2){
                    decReqBtn.setEnabled(false);

                    Map decMap = new HashMap();
                    decMap.put(uid + "/" + userId + "/request_type",null);
                    decMap.put(userId + "/" + uid + "/request_type",null);

                    frndReqDb.updateChildren(decMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null){
                                Log.d("db Err",databaseError.toException().toString());
                            }else {
                                sendReqBtn.setText("Send");
                                frndshpStatus = 0; // not_frnd = 0
                                sendReqBtn.setEnabled(true);
                                decReqBtn.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

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
