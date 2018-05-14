package com.example.android.chatmini;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class KeepSync extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
