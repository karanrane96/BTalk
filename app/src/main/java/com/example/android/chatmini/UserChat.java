package com.example.android.chatmini;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserChat extends AppCompatActivity {
        private  String mChatUser;

    private DatabaseReference mRootRef;
    String mCurrentUserId;
    private ImageButton mChatAddButton;
    private ImageButton mChatSendButton;
    private EditText mChatMsgView;

    private final List<Messages> messagesList = new ArrayList<>();

    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;

    private RecyclerView mMessagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        mChatUser=getIntent().getStringExtra("userId");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mChatAddButton= (ImageButton)findViewById(R.id.chat_add_btn);
        mChatSendButton=(ImageButton)findViewById(R.id.chat_send_btn);
        mChatMsgView=(EditText)findViewById(R.id.chat_message_view);

        mAdapter= new MessageAdapter(messagesList);

        mMessagesList=(RecyclerView) findViewById(R.id.messages_list);
        mLinearLayout= new LinearLayoutManager(this);
        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdapter);

       loadMessages();



       // -----------------------Sending Chats -----------------------
        mChatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();

            }
        });


        mCurrentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        mRootRef= FirebaseDatabase.getInstance().getReference();

        mRootRef.child("Users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String chat_username= dataSnapshot.child("name").getValue().toString();
                getSupportActionBar().setTitle(chat_username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRootRef.child("Chats").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(mChatUser))
                {
                    Map chatAddMap =  new HashMap();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap= new HashMap();
                    chatUserMap.put("Chat/" + mCurrentUserId + "/" + mChatUser,chatAddMap);
                    chatUserMap.put("Chat/" + mChatUser + "/" + mCurrentUserId,chatAddMap);

                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError!=null)
                            {
                                Log.d("Chat Log",databaseError.getMessage());
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadMessages() {
        Log.d("Users","curr: "+mCurrentUserId);
        mRootRef.child("messages").child(mCurrentUserId).child(mChatUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message= dataSnapshot.getValue(Messages.class);
                messagesList.add(message);
                mAdapter.notifyDataSetChanged();
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

            }
        });
    }

    private void sendMessage() {
        String message= mChatMsgView.getText().toString();

        if(!TextUtils.isEmpty(message))
        {
            String currentUserRef= "messages/" + mCurrentUserId + "/" + mChatUser;
            String chatUserRef=  "messages/" + mChatUser + "/" + mCurrentUserId;
            Map messageMap= new HashMap();
            messageMap.put("message", message );
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);

            DatabaseReference user_message_push= mRootRef.child("messages").child(mCurrentUserId).child(mChatUser).push();

            String push_id= user_message_push.getKey();

            Map messageUserMap = new HashMap();
            messageUserMap.put(currentUserRef + "/" + push_id,messageMap);
            messageUserMap.put(chatUserRef + "/" + push_id,messageMap);

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError!=null)
                    {
                        Log.d("Chat Log",databaseError.getMessage());
                    }
                }
            });
        }
    }


}
