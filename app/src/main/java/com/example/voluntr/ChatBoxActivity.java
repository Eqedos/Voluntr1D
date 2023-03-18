package com.example.voluntr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatBoxActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatBoxAdapter;
    private RecyclerView.LayoutManager mChatBoxLayoutManager;

    private EditText mSendEditText;
    private Button mSendButton;
    private String currentUserID,chatId,chatboxId;

    DatabaseReference mDbUser,mDbUserChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        chatId=getIntent().getExtras().getString("chatId");
        currentUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDbUser = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(currentUserID).child("connections").child("ChatIds").child(chatId);
        mDbUserChat = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Chat");
        getChatBoxId();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mChatBoxLayoutManager =new LinearLayoutManager(ChatBoxActivity.this);
        mRecyclerView.setLayoutManager(mChatBoxLayoutManager);
        mChatBoxAdapter = new ChatBoxAdapter(getDataSetChatBox(), ChatBoxActivity.this);
        mRecyclerView.setAdapter(mChatBoxAdapter);
        mSendEditText = findViewById(R.id.message);
        mSendButton = findViewById(R.id.send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String sendMessageText=mSendEditText.getText().toString();
        if(!sendMessageText.isEmpty()){
            DatabaseReference newmsgdb = mDbUserChat.push();
            Map newMessage = new HashMap();
            newMessage.put("createdByUser",currentUserID);
            newMessage.put("text",sendMessageText);
            newmsgdb.setValue(newMessage);
        }
        mSendEditText.setText(null);
    }
    private void getChatBoxId(){
        mDbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    chatboxId=snapshot.getValue().toString();
                    mDbUserChat=mDbUserChat.child(chatboxId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChatMessages() {
        mDbUserChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    String message=null;
                    String createdbyUser=null;

                    if(snapshot.child("text").getValue() != null){
                        message=snapshot.child("text").getValue().toString();
                    }
                    if(snapshot.child("createdByUser").getValue() != null){
                        createdbyUser=snapshot.child("createdByUser").getValue().toString();
                    }
                    if(message!=null && createdbyUser!=null){
                        Boolean curuserBoolean = false;
                        if(createdbyUser.equals(currentUserID)){
                            curuserBoolean=true;
                        }
                        ChatBoxObject newMessage = new ChatBoxObject(message,curuserBoolean);
                        resultsChatBox.add(newMessage);
                        mChatBoxAdapter.notifyDataSetChanged();

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

    private ArrayList<ChatBoxObject> resultsChatBox= new ArrayList<ChatBoxObject>();
    private List<ChatBoxObject> getDataSetChatBox() {
        return resultsChatBox;
    }
}