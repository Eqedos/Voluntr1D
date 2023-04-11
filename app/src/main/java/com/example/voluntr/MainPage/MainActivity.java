package com.example.voluntr.MainPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.voluntr.BaseActivity;
import com.example.voluntr.Chats.ChatActivity;
import com.example.voluntr.Profile.AccountProfile;
import com.example.voluntr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {


    private OrgCards cards_data[];
    private ArrayAdapter arrayAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;

    ListView listView;
    List<OrgCards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Makes main page
        setContentView(R.layout.activity_main); //Makes main page
        usersDb = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users");
        mAuth=FirebaseAuth.getInstance(); //Gets the current user
        String currentUId=mAuth.getCurrentUser().getUid();
        checkUserPreferences(); //Checks if user is organiser/volunteer
        rowItems = new ArrayList<OrgCards>();//s


        arrayAdapter = new ArrayAdapter(this, R.layout.item, rowItems );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                OrgCards object1 = (OrgCards) dataObject;
                String userId = object1.getUserId();
                usersDb.child(userId).child("connections").child("No").child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this,"left", Toast.LENGTH_SHORT).show(); //Makes small bubble with the text
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                OrgCards object1 = (OrgCards) dataObject;
                String userId = object1.getUserId();
                usersDb.child(userId).child("connections").child("Yes").child(currentUId).setValue(true);
                usersDb.child(currentUId).child("connections").child("Yes").child(userId).setValue(true);
                DatabaseReference eventsNumRef = usersDb.child(currentUId).child("eventsnum");
                eventsNumRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long eventsNum;

                        if (dataSnapshot.exists()) {
                            eventsNum = (long) dataSnapshot.getValue();
                            eventsNum++;
                        } else {
                            eventsNum = 1;
                        }

                        eventsNumRef.setValue(eventsNum);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                    }
                });

                usersDb.child(userId).child("connections").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("ChatId").exists()){
                            String key = snapshot.child("ChatId").getValue().toString();
                            usersDb.child(userId).child("connections").child("ChatId").setValue(key);
                            usersDb.child(currentUId).child("connections").child("ChatIds").child(userId).setValue(key);
                        }
                        else{
                            String key = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Chat").push().getKey();
                            usersDb.child(userId).child("connections").child("ChatId").setValue(key);
                            usersDb.child(currentUId).child("connections").child("ChatIds").child(userId).setValue(key);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                    String imageUrl = object1.getProfileImageUrl();
                });
                showImage("voluntrlogofinal");
                Toast.makeText(MainActivity.this,"You have been added to the organisations chat", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this,"click", Toast.LENGTH_SHORT).show();
                OrgCards object1 = (OrgCards) dataObject;
                String userId = object1.getUserId();
                DatabaseReference bioRef = usersDb.child(userId).child("bio");
                bioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String bio = dataSnapshot.getValue().toString();
                            showBioDialog(bio);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors
                    }
                });
            }
        });

    }

    private String userStatus;
    private String notuserStatus;
    public void checkUserPreferences(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //Gets the user object of user
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getKey().equals(user.getUid())){
                    if (snapshot.child("status")!=null){
                        userStatus=snapshot.child("status").getValue().toString();
                        notuserStatus="Organiser";
                        switch (userStatus){
                            case "Volunteer":
                                notuserStatus="Organiser";
                                break;
                            case "Organiser":
                                notuserStatus="Volunteer";
                                break;
                        }
                        getOppositeStatusUser();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void showImage(String imgpath) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.onrightswipe);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView rightSwipeImage = dialog.findViewById(R.id.right_swipe_image);
        int drawableResourceId = getResources().getIdentifier(imgpath, "drawable", getPackageName());
        Glide.with(MainActivity.this)
                .load(drawableResourceId)
                .into(rightSwipeImage);

        dialog.show();

        // Set a timer to automatically dismiss the dialog after a certain duration (e.g., 2 seconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1000);
    }

    public void getOppositeStatusUser(){
        String currentUId=mAuth.getCurrentUser().getUid();
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.child("status").getValue()!= null){
                String currentUId = mAuth.getCurrentUser().getUid();
                if (snapshot.exists() && !snapshot.child("connections").child("No").hasChild(currentUId) && !snapshot.child("connections").child("Yes").hasChild(currentUId) && snapshot.child("status").getValue().toString().equals(notuserStatus)) {
                    String profileImageUrl = "default";
                    if (!snapshot.child("profileImageUrl").getValue().equals("default") && !snapshot.child("profileImageUrl").getValue().equals(null)) {
                        profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                    }
                    String name = "N/A";
                    if (snapshot.child("name").exists() && snapshot.child("name").getValue() != null) {
                        name = snapshot.child("name").getValue().toString();
                    }
                    String age = "N/A";
                    if (snapshot.child("age").exists() && snapshot.child("age").getValue() != null) {
                        age = snapshot.child("age").getValue().toString();
                    }
                    OrgCards item = new OrgCards(snapshot.getKey(), name, profileImageUrl, age);
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }

            }
        }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
        public void goToProfile(android.view.View view) {
        Intent intent = new Intent(MainActivity.this, AccountProfile.class);
        startActivity(intent);
        return;
        }

    public void goToChat(android.view.View view) {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        startActivity(intent);
        return;
    }
    private void showBioDialog(String bio) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.bio_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView bioTextView = dialog.findViewById(R.id.bio_text_view);
        bioTextView.setText(bio);

        Button closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}