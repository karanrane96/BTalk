package com.example.android.chatmini;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePage extends AppCompatActivity {

    public TextView status, userName, company, designation, email;
    CircleImageView profPic;
    Button sendReqBtn, decReqBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        String userId = getIntent().getStringExtra("userId");
        status = findViewById(R.id.user_status);
        userName = findViewById(R.id.user_name);
        company = findViewById(R.id.user_company);
        designation = findViewById(R.id.user_desig);
        email = findViewById(R.id.user_email);
        profPic = findViewById(R.id.profile_pic);
        sendReqBtn = findViewById(R.id.send_req_btn);
        decReqBtn = findViewById(R.id.dec_req_btn);
    }
}
