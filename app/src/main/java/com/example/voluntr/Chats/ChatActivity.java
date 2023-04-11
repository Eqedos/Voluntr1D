package com.example.voluntr.Chats;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.voluntr.BaseActivity;
import com.example.voluntr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChatActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        currentUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mChatLayoutManager =new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChats(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);
        getuserchatId();





    }

    private void getuserchatId() { //To get a users chatId
        DatabaseReference chatdb = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(currentUserID).child("connections").child("Yes");
        chatdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot chat: snapshot.getChildren()){
                        GetChatInfo(chat.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetChatInfo(String key) { //To get information of the chat
        DatabaseReference volndb = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(key);
        volndb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userId = snapshot.getKey();
                    String name = "hi";
                    String profileImageUrl ="hi";
                    if (snapshot.child("name").getValue()!=null){
                        name=snapshot.child("name").getValue().toString();
                    }
                    if (snapshot.child("profileImageUrl").getValue()!=null) {
                        profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                    }

                    DetailsOfOrg oj = new DetailsOfOrg(userId,name,profileImageUrl);
                    resultsChat.add(oj);
                    mChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList<DetailsOfOrg> resultsChat= new ArrayList<DetailsOfOrg>(); //Arraylist for organisation details in chat
    private List<DetailsOfOrg> getDataSetChats() {
        return resultsChat;
    } //Gets data of set chats

}