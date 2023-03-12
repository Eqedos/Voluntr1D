package com.example.voluntr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.View;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private orgcards cards_data[];
    private arrayAdapter arrayAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;

    ListView listView;
    List<orgcards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Makes main page
        setContentView(R.layout.activity_main); //Makes main page
        usersDb = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users");
        mAuth=FirebaseAuth.getInstance(); //Gets the current user
        String currentUId=mAuth.getCurrentUser().getUid();
        checkUserStatus(); //Checks if user is organiser/volunteer
        rowItems = new ArrayList<orgcards>();//s

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

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
                orgcards object1 = (orgcards) dataObject;
                String userId = object1.getUserId();
                usersDb.child(notuserStatus).child(userId).child("connections").child("No").child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this,"left", Toast.LENGTH_SHORT).show(); //Makes small bubble with the text
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                orgcards object1 = (orgcards) dataObject;
                String userId = object1.getUserId();
                usersDb.child(notuserStatus).child(userId).child("connections").child("Yes").child(currentUId).setValue(true);
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
            }
        });

    }

    private String userStatus;
    private String notuserStatus;
    public void checkUserStatus(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //Gets the user object of user
        DatabaseReference volDb = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child("Volunteer");
        volDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals(user.getUid())){
                    userStatus="Volunteer";
                    notuserStatus="Organiser";
                    getOppositeStatusUser();
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

    public void getOppositeStatusUser(){
        String currentUId=mAuth.getCurrentUser().getUid();
        DatabaseReference oppositeStatusDb = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(notuserStatus);
        oppositeStatusDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String currentUId=mAuth.getCurrentUser().getUid();
                if (snapshot.exists() && !snapshot.child("connections").child("No").hasChild(currentUId) && !snapshot.child("connections").child("Yes").hasChild(currentUId)){
                    orgcards item = new orgcards(snapshot.getKey(),snapshot.child("Name").getValue().toString());
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
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
    public void logoutUser(android.view.View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this,LoginRegActivity.class);
        startActivity(intent);
        finish();
        }
        public void goToProfile(android.view.View view) {
        Intent intent = new Intent(MainActivity.this,AccountProfile.class);
            startActivity(intent);


        }
}