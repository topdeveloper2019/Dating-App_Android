package com.example.drago.vetroapp;


import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;

import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.drago.vetroapp.cropper.CropMainActivity;
import com.example.drago.vetroapp.cropper.CropResultActivity;
import com.example.drago.vetroapp.models.Post;
import com.example.drago.vetroapp.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marcoscg.dialogsheet.DialogSheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.drago.vetroapp.services.MyUploadService;

import static java.io.File.*;

/**
 * Activity to upload and download photos from Firebase Storage.
 *
 * See {@link MyUploadService} for upload example.
 */
public class NewMessageActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{

    private static final int REQUEST_CODE = 211;
    public static Bitmap mImage;
    private int sampleSize = 0;
    private Uri imageUri = null;
    private String fileName = null;
    private String url = null;

    private int image_width = 0;
    private int image_height = 0;

    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase;
    private StorageReference imageReference;
    private StorageReference fileRef;

    @BindView(R.id.cancelText)
    TextView _cancelButton;
    @BindView(R.id.postText)
    TextView _postButton;
    @BindView(R.id.messageText)
    EditText _messageText;
    @BindView(R.id.uploadImage)
    ImageView _uploadImage;
    @BindView(R.id.loadButton)
    TextView _loadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        ButterKnife.bind(this);
        _cancelButton.setOnClickListener(this);

        _postButton.setOnClickListener(this);

        _loadButton.setOnClickListener(this);

        _uploadImage.setOnClickListener(this);

        _uploadImage.setOnLongClickListener(this);
        if(_messageText.requestFocus()) {
            //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();
        imageReference = FirebaseStorage.getInstance().getReference().child("posts");
        fileRef = null;

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) throws NullPointerException {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "result ok", Toast.LENGTH_SHORT).show();
        Drawable bitmapDrawable;
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                if (mImage != null) {
                    imageUri = getImageUri(mImage);
                    image_width = mImage.getWidth();
                    image_height = mImage.getHeight();
                    Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show();
                    bitmapDrawable = new BitmapDrawable(getResources(),mImage);
                    _uploadImage.setBackground(bitmapDrawable);
                    assert data != null;
                    sampleSize = data.getIntExtra("SAMPLE_SIZE", 1);

                } else {
                    assert data != null;
                    imageUri = data.getParcelableExtra("URI");
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        bitmapDrawable = Drawable.createFromStream(inputStream, imageUri.toString() );
                        if (imageUri != null) {
                            image_height = bitmapDrawable.getMinimumHeight();
                            image_width = bitmapDrawable.getMinimumWidth();
                            _uploadImage.setBackground(bitmapDrawable);
                        }

                    }catch (FileNotFoundException e){
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.cancelText:
                finish();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.postText:

                requestNewPost();

                break;
            case R.id.loadButton:
               loadImage();
                break;

            case R.id.uploadImage:

                if(sampleSize != 0){
                    Intent resultIntent = new Intent(this, CropResultActivity.class);
                    resultIntent.putExtra("SAMPLE_SIZE", sampleSize);
                    if (imageUri != null) {
                        resultIntent.putExtra("URI",imageUri);
                    } else {
                        CropResultActivity.mBitmapImage = mImage;
                    }
                    startActivity(resultIntent);
                }
                break;
        }
    }
    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.uploadImage:
                new DialogSheet(this)
                        .setTitle("Notification")
                        .setMessage("Do you want to delete Image?")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogSheet.OnPositiveClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Your action
                                loadImage();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogSheet.OnNegativeClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Your action
                            }
                        })

                        .setButtonsColorRes(R.color.colorPrimary)  // Default color is accent
                        .show();
        }
        return false;
    }

    private void loadImage(){
        Intent intent = new Intent(this,CropMainActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }
    private void requestNewPost(){
        if(imageUri != null){
            uploadFile();
        } else {
            submitPost();
        }
    }
    private void submitPost() {

        // Body is required
        if (TextUtils.isEmpty(_messageText.getText().toString())) {
            _messageText.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user != null) {
                            writeNewPost(userId, user.username, _messageText.getText().toString());

                        }  // Write new post


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        // [END single_value_read]
    }
    // [START write_fan_out]
    private void writeNewPost(String userId, String username, String body) {

        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, body,fileName,url,image_width,image_height);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        finish();
        Intent intent1 = new Intent(this,MainActivity.class);
        startActivity(intent1);
    }
    // [END write_fan_out]
    private Uri getImageUri( Bitmap inImage) {
        File file = createImageFile();
        if (file != null) {
            FileOutputStream fout;
            try {
                fout = new FileOutputStream(file);
                inImage.compress(Bitmap.CompressFormat.JPEG, 70, fout);
                fout.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Uri.fromFile(file);
        }
        return null;
    }
    public File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String root = this.getDir("my_sub_dir", Context.MODE_PRIVATE).getAbsolutePath();
        File myDir = new File(root + "/Img");
        if (!myDir.exists()) myDir.mkdirs();
        File mFileTemp = null;
        try {
            String imageFileName = "JPEG_" + timeStamp + "_";
            mFileTemp = createTempFile(imageFileName, ".jpg", myDir.getAbsoluteFile());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return mFileTemp;

    }
    private void uploadFile() {
        if (imageUri != null && imageUri.getLastPathSegment() != null) {
            fileRef = imageReference.child(imageUri.getLastPathSegment());
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    fileName = imageUri.getLastPathSegment();
                                    //                                   Uri downloadUrl = uri;
                                    url = uri.toString();
                                    //Do what you want with the url
                                    submitPost();
                                }
                            });

                            Toast.makeText(NewMessageActivity.this, "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {


                            Toast.makeText(NewMessageActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    .addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Upload is paused!");
                        }
                    });
        } else {
            Toast.makeText(NewMessageActivity.this, "No File!", Toast.LENGTH_LONG).show();
        }
    }

}

