package com.example.voluntr;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;


import android.app.Activity;
import android.app.Instrumentation;
import android.app.appsearch.StorageInfo;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AccountProfile extends AppCompatActivity {
    private EditText mName3,mPhone,mAge,mBio;
    private Button mBack,mConfirm;
    private ImageView mProfilePic;
    private FirebaseAuth mAuth;
    private DatabaseReference mVolDb;
    private String userId, name, phone, age, bio, userStatus;

    private Uri resultUri;
    private String profileImageUrl;
    private ActivityResultLauncher<String> mTakePhotoLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileacticity);
        mName3 = (EditText) findViewById(R.id.name3);
        mPhone = (EditText) findViewById(R.id.Phone);
        mAge = (EditText) findViewById(R.id.age);
        mBio = (EditText) findViewById(R.id.Bio);
        mProfilePic = (ImageView) findViewById(R.id.pfp);
        mBack = (Button) findViewById(R.id.back);
        mConfirm = (Button) findViewById(R.id.confirm_button);

        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        mVolDb = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(userId);
        mTakePhotoLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // handle the result here
                        resultUri = uri;
                        mProfilePic.setImageURI(resultUri);
                    }
                });
        getUserInfo();
        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTakePhotoLauncher.launch("image/*");
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
    }
    private void getUserInfo(){
        mVolDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    Map<String,Object> map = (Map<String, Object>) snapshot.getValue();
                    if(map.get("name")!=null) {
                        name = map.get("name").toString();
                        mName3.setText(name);
                    }
                    if(map.get("phone")!=null) {
                        phone = map.get("phone").toString();
                        mPhone.setText(phone);
                    }
                    if(map.get("bio")!=null) {
                        bio = map.get("bio").toString();
                        mBio.setText(bio);
                    }
                    if(map.get("status")!=null) {
                        userStatus = map.get("status").toString();
                    }
                    if(map.get("phone")!=null) {
                        phone = map.get("phone").toString();
                        mPhone.setText(phone);
                    Glide.clear(mProfilePic);
                    if(map.get("profileImageUrl")!=null) {
                        profileImageUrl = map.get("profileImageUrl").toString();}
                        switch (profileImageUrl){
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.logofinal).into(mProfilePic);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfilePic);
                                break;
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveUserInfo() {
        name = mName3.getText().toString();
        phone=mPhone.getText().toString();
        age = mAge.getText().toString();
        bio = mBio.getText().toString();
        Map userInfo = new HashMap();
        userInfo.put("name",name);
        userInfo.put("phone",phone);
        userInfo.put("age",age);
        userInfo.put("bio",bio);
        mVolDb.updateChildren(userInfo);
        if(resultUri!=null){
            StorageReference filepath = FirebaseStorage.getInstance("gs://voluntr-f211c.appspot.com").getReference().child("profileimages").child(userId);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20,baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Handle successful download URL retrieval
                    Uri downloadUrl = uri;
                    Map userInfo = new HashMap();
                    userInfo.put("profileImageUrl", downloadUrl.toString());
                    mVolDb.updateChildren(userInfo);
                    Toast.makeText(AccountProfile.this,"Works",Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                    // Do something with the download URL
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle unsuccessful download URL retrieval
                    Toast.makeText(AccountProfile.this,"Doesnt work",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });


        }
    }

    public void logoutUser(android.view.View view) {
        mAuth.signOut();
        Intent intent = new Intent(AccountProfile.this,LoginRegActivity.class);
        startActivity(intent);
        finish();
    }
}