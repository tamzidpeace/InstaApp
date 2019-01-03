package com.example.arafat.instaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewInstaApp;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ///
        recyclerViewInstaApp = findViewById(R.id.recycler_view_insta_app);
        recyclerViewInstaApp.setHasFixedSize(true);
        recyclerViewInstaApp.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("InstaApp");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, RegisterAcitivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        //to check that is user already logged in
        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<GetterSetter, InstaViewHolder> FBRA = new FirebaseRecyclerAdapter<GetterSetter, InstaViewHolder>(
                GetterSetter.class,
                R.layout.recycler_view_row,
                InstaViewHolder.class,
                mDatabase) {
            @Override
            protected void populateViewHolder(InstaViewHolder viewHolder, GetterSetter model, int position) {
                viewHolder.setTitle2(model.getTitle());
                viewHolder.setDesc2(model.getDescription());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setUsername(model.getUsername());
            }
        };
        recyclerViewInstaApp.setAdapter(FBRA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // adapter class

    public static class InstaViewHolder extends RecyclerView.ViewHolder {

        public InstaViewHolder(View itemView) {
            super(itemView);
            View mView = itemView;
        }

        public void setTitle2(String title) {

            TextView postTitle = itemView.findViewById(R.id.textTitle);
            postTitle.setText(title);
        }

        public void setDesc2(String desc) {

            TextView postDesc =itemView.findViewById(R.id.textDescription);
            postDesc.setText(desc);
        }

        public void setUsername(String username) {
            TextView username2 = itemView.findViewById(R.id.username);
            username2.setText(username);
        }

        public void setImage(Context ctx, String image) {
            ImageView postImage = itemView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(postImage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        else if (id == R.id.action_add) {
            startActivity(new Intent(this, PostActivity.class));

            return true;
        }

        else if(id == R.id.action_log_out) {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }
}
