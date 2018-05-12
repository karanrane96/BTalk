package com.example.android.chatmini;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import static java.lang.Thread.sleep;


public class SplashScreen extends AppCompatActivity {
    private ImageView iv;
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

            iv=(ImageView)findViewById(R.id.iv);
            Animation myAnimation= AnimationUtils.loadAnimation(this,R.anim.my_transition);
            iv.startAnimation(myAnimation);
              Intent loginIntent= new Intent(SplashScreen.this,MainChatScreen.class);
              startActivity(loginIntent);
//            Thread timer= new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//
//                    }
//                    finally {
//                        startActivity(loginIntent);
//                        finish();
//                    }
//
//                }
//            };

//        loginBtn = findViewById(R.id.loginBtn);
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MainChatScreen.class);
//                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            }
//        });
    }

}
