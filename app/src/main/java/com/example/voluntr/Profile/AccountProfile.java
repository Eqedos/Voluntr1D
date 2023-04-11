package com.example.voluntr.Profile;

import android.content.Context;
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

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.voluntr.BaseActivity;
import com.example.voluntr.LoginRegister.LoginRegActivity;
import com.example.voluntr.MainPage.MainActivity;
import com.example.voluntr.R;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountProfile extends BaseActivity {
    private EditText mName3, mPhone, mAge, mBio;
    private Button mBack, mConfirm;
    private ImageView mProfilePic;
    private FirebaseAuth mAuth;
    private DatabaseReference mVolDb;
    private String userId, name, phone, age, bio, userStatus;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private Uri resultUri;
    private String profileImageUrl;
    private ActivityResultLauncher<String> mTakePhotoLauncher;
    private RecyclerView recyclerView;
    private IconAdapter iconAdapter;
    private List<String> iconUrls;
    private StorageReference storageReference;
    private RecyclerView.LayoutManager EventLayoutManager;
    private ImageView badgeImage;
    private final ArrayList<String> resultEvents = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);
        mName3 = (EditText) findViewById(R.id.name3);
        recyclerView = (RecyclerView) findViewById(R.id.eventrecycle);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        EventLayoutManager = new LinearLayoutManager(AccountProfile.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(EventLayoutManager);
        iconAdapter = new IconAdapter(AccountProfile.this, getDataEvents());
        recyclerView.setAdapter(iconAdapter);
        mPhone = (EditText) findViewById(R.id.Phone);
        mAge = (EditText) findViewById(R.id.age);
        mBio = (EditText) findViewById(R.id.Bio);
        mProfilePic = (ImageView) findViewById(R.id.pfp);
        mConfirm = (Button) findViewById(R.id.confirm_button);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        badgeImage = (ImageView) findViewById(R.id.badge_image);
        mVolDb = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(userId);
        mVolDb.child("eventsnum").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int eventsNum = dataSnapshot.getValue(Integer.class);

                    if (eventsNum > 1 && eventsNum < 3) {
                        badgeImage.setImageResource(R.drawable.badge1); //silver
                    } else if (eventsNum == 3) {
                        badgeImage.setImageResource(R.drawable.badge2); //gold
                    } else if (eventsNum > 3) {
                        badgeImage.setImageResource(R.drawable.badge4); //platinum
                    } else {
                        badgeImage.setImageResource(R.drawable.badge3); //bronze
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });

        getUserEventIDs();
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
                Intent intent = new Intent(AccountProfile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }

    private void getUserEventIDs() {
        DatabaseReference chatdb = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(userId).child("connections").child("Yes");
        chatdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot chat : snapshot.getChildren()) {
                        GetEventInfo(chat.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetEventInfo(String key) {
        DatabaseReference volndb = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(key);
        volndb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profileImageUrl = "default";
                    if (snapshot.child("profileImageUrl").getValue() != null) {
                        profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                    }

                    resultEvents.add(profileImageUrl);
                    iconAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<String> getDataEvents() {
        return resultEvents;
    }

    private void getUserInfo() {
        mVolDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (map.get("name") != null) {
                        name = map.get("name").toString();
                        mName3.setText(name);
                    }
                    if (map.get("phone") != null) {
                        phone = map.get("phone").toString();
                        mPhone.setText(phone);
                    }
                    if (map.get("bio") != null) {
                        bio = map.get("bio").toString();
                        mBio.setText(bio);
                    }
                    if (map.get("age") != null) {
                        age = map.get("age").toString();
                        mAge.setText(age);
                    }
                    if (map.get("status") != null) {
                        userStatus = map.get("status").toString();
                    }
                    if (map.get("phone") != null) {
                        phone = map.get("phone").toString();
                        mPhone.setText(phone);
                        Context context = getApplicationContext();
                        Glide.with(context).clear(mProfilePic);
                        if (map.get("profileImageUrl") != null) {
                            profileImageUrl = map.get("profileImageUrl").toString();
                        }
                        switch (profileImageUrl) {
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.logofinal).into(mProfilePic);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).transform(new CircleCrop()).into(mProfilePic);
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
        phone = mPhone.getText().toString();
        age = mAge.getText().toString();
        bio = mBio.getText().toString();
        Map userInfo = new HashMap();
        userInfo.put("name", name);
        userInfo.put("phone", phone);
        userInfo.put("age", age);
        userInfo.put("bio", bio);
        mVolDb.updateChildren(userInfo);
        if (resultUri != null) {
            StorageReference filepath = FirebaseStorage.getInstance("gs://voluntr-f211c.appspot.com").getReference().child("profileimages").child(userId);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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
                    Toast.makeText(AccountProfile.this, "Works", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                    // Do something with the download URL
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle unsuccessful download URL retrieval
                    Toast.makeText(AccountProfile.this, "Doesnt work", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });


        }
        Toast.makeText(AccountProfile.this, "Profile Updated", Toast.LENGTH_SHORT);
    }

    public void logoutUser(android.view.View view) {
        mAuth.signOut();
        Intent intent = new Intent(AccountProfile.this, LoginRegActivity.class);
        startActivity(intent);
        finish();
    }
}