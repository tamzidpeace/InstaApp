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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private Button postBtn;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postBtn = findViewById(R.id.button_post);
        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("InstaApp");





        /*postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                titleEditText = findViewById(R.id.edit_text_post_title);
                descriptionEditText = findViewById(R.id.edit_text_post_description);

                final String title = titleEditText.getText().toString().trim();
                final String description = descriptionEditText.getText().toString().trim();

                if (!title.equals("") && !description.equals("")) {

                    StorageReference filePath = storageReference.child("PostImage").child(Objects.requireNonNull(uri.getLastPathSegment()));
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(PostActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                            // Write a message to the database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            DatabaseReference myRef2 = database.getReference().child("InstaApp").push();
                            myRef2.child("title").setValue(title);
                            myRef2.child("description").setValue(description);
                            myRef2.child("image").setValue(downloadUrl.toString());


                        }
                    });


                    Toast.makeText(getApplicationContext(), title + description, Toast.LENGTH_SHORT).show();
                    titleEditText.setText("");
                    descriptionEditText.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "enter information", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
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
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(PostActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    // Write a message to the database

                    DatabaseReference myRef = databaseReference.push();
                    myRef.child("title").setValue(title);
                    myRef.child("description").setValue(description);
                    myRef.child("image").setValue(downloadUrl.toString());


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
