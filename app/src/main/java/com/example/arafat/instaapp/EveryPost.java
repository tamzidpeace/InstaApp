package com.example.arafat.instaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EveryPost extends AppCompatActivity {

    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_post);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final ImageView everyPostImage = findViewById(R.id.every_post_image);
        final TextView everyPostTitle = findViewById(R.id.every_text_title);
        final TextView everyPostDescription = findViewById(R.id.every_text_description);


        String post_key = getIntent().getExtras().getString("Postid");
        mRef = FirebaseDatabase.getInstance().getReference().child("InstaApp");

        mRef.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_description = (String) dataSnapshot.child("description").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();

                everyPostTitle.setText(post_title);
                everyPostDescription.setText(post_description);
                Picasso.with(EveryPost.this).load(post_image).into(everyPostImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EveryPost.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onClickDelete(View view) {
        String post_key = getIntent().getExtras().getString("Postid");
        mRef.child(post_key).removeValue();
        startActivity(new Intent(EveryPost.this, MainActivity.class));
    }
}
