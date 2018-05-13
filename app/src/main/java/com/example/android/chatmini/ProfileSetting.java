package com.example.android.chatmini;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetting extends AppCompatActivity {

    private static final String TAG = "ProfileSetting";
    public TextView status, userName, company, designation, email;
    CircleImageView profPic;
    ImageView editCompany, editDesig;
    String m_Text, uName, uCompany, uDesig, uEmail, uid;
    AVLoadingIndicatorView progress;
    DatabaseReference mDb;
    private DatabaseReference database;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        setTitle("Profile Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        mDb = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        Log.d(TAG,uid);
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
        getDbData();

        editCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                dialog = new Dialog(ProfileSetting.this);
                dialog.setContentView(R.layout.popup);
                TextView text = dialog.findViewById(R.id.popupMsg);
                text.setText("Enter your company name");
                final EditText editText = dialog.findViewById(R.id.editField);
                Button save = dialog.findViewById(R.id.okBtn);
                text.setEnabled(true);
                editText.setEnabled(true);
                save.setEnabled(true);
                dialog.show();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.hide();
                        progress.show();
                        m_Text = editText.getText().toString();
                        database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                        try {
                            database.child("company").setValue(m_Text).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    getDbData();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        editDesig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                dialog = new Dialog(ProfileSetting.this);
                dialog.setContentView(R.layout.popup);
                TextView text = dialog.findViewById(R.id.popupMsg);
                text.setText("Enter your designation");
                final EditText editText = dialog.findViewById(R.id.editField);
                Button save = dialog.findViewById(R.id.okBtn);
                text.setEnabled(true);
                editText.setEnabled(true);
                save.setEnabled(true);
                dialog.show();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.hide();
                        progress.show();

                        m_Text = editText.getText().toString();
                        database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                        try {
                            database.child("desig").setValue(m_Text).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    getDbData();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

        company.setText(m_Text);

    }

    public void getDbData(){


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

    }

}