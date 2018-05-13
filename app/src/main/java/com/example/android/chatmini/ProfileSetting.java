package com.example.android.chatmini;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetting extends AppCompatActivity {

    private static final String TAG = "ProfileSetting";
    public TextView status, userName, company, designation, email;
    CircleImageView profPic;
    ImageView editCompany, editDesig;
    String m_Text, uName, uCompany, uDesig, uEmail;
    AVLoadingIndicatorView progress;
    DatabaseReference mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        setTitle("Profile Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mDb = FirebaseDatabase.getInstance().getReference().child("Users").child(intent.getStringExtra("uid"));

        status = findViewById(R.id.user_status);
        userName = findViewById(R.id.user_name);
        company = findViewById(R.id.user_company);
        designation = findViewById(R.id.user_desig);
        email = findViewById(R.id.user_email);
        profPic = findViewById(R.id.profile_pic);
        editCompany = findViewById(R.id.edit_company);
        editDesig = findViewById(R.id.edit_desig);
        progress = findViewById(R.id.progress_bar);
        progress.setIndicator("Loading Deatils...");
        progress.show();


        mDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uName = dataSnapshot.child("name").getValue().toString();
                uCompany = dataSnapshot.child("company").getValue().toString();
                uDesig = dataSnapshot.child("desig").getValue().toString();
                uEmail = dataSnapshot.child("email").getValue().toString();

                userName.setText(uName);
                company.setText(uCompany);
                email.setText(uEmail);
                designation.setText(uDesig);
                status.setText(uDesig+" @ "+uCompany);
                progress.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());

            }
        });

        editCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Title");

// Set up the input
                final EditText input = new EditText(getApplicationContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        editDesig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });

        company.setText(m_Text);

    }
}
