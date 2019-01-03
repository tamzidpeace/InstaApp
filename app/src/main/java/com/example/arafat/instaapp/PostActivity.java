package com.example.arafat.instaapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class PostActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 2;
    private Uri uri = null;
    private ImageButton imageButton;
    private EditText titleEditText, descriptionEditText;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("InstaApp");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

    }

    public void imageButtonClicked(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("Image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            uri = data.getData();
            imageButton = findViewById(R.id.image_button_post_image);
            imageButton.setImageURI(uri);
        }
    }


    public void onPostClickListener(View view) {

        titleEditText = findViewById(R.id.edit_text_post_title);
        descriptionEditText = findViewById(R.id.edit_text_post_description);

        final String title = titleEditText.getText().toString().trim();
        final String description = descriptionEditText.getText().toString().trim();

        if (!title.equals("") && !description.equals("")) {

            StorageReference filePath = storageReference.child("PostImage").child(Objects.requireNonNull(uri.getLastPathSegment()));
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(PostActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    // Write a message to the database

                    final DatabaseReference myRef = databaseReference.push();

                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {

                            mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());


                            myRef.child("title").setValue(title);
                            myRef.child("description").setValue(description);
                            myRef.child("image").setValue(downloadUrl.toString());
                            myRef.child("uid").setValue(mCurrentUser.getUid());
                            myRef.child("username").setValue(dataSnapshot.child("name").getValue())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Intent mainActivityIntent = new Intent(PostActivity.this, MainActivity.class);
                                                startActivity(mainActivityIntent);

                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            });


            Toast.makeText(getApplicationContext(), title + description, Toast.LENGTH_SHORT).show();
            titleEditText.setText("");
            descriptionEditText.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "enter information", Toast.LENGTH_SHORT).show();
        }
    }

}
