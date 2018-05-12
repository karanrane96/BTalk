package com.example.android.chatmini;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllUser extends AppCompatActivity {

    private RecyclerView userList;
    private DatabaseReference userDatabase;


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
                Toast.makeText(this,"profile_pic",Toast.LENGTH_LONG).show();
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


        FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.single_user_list_view,
                UserViewHolder.class,
                userDatabase
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, Users model, int position) {
                viewHolder.setValues(model.getName(), model.getDesig());
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

        public void setValues(String nameh, String desigh){
            TextView nameTv, desigTv;
            nameTv =  mView.findViewById(R.id.user_name);
            desigTv = mView.findViewById(R.id.user_desig);


            nameTv.setText(nameh);
            desigTv.setText(desigh);
        }
    }
}
