package com.example.voluntr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatBoxAdapter extends RecyclerView.Adapter<chatboxviewholder> {
    private List<ChatBoxObject> chatList;
    private Context context;

    public ChatBoxAdapter(List<ChatBoxObject> chatList, Context context){
        this.chatList = chatList;
        this.context = context;
    }
    @NonNull
    @Override
    public chatboxviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutsv = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem, null, false);
        RecyclerView.LayoutParams layp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutsv.setLayoutParams(layp);
        chatboxviewholder rcv = new chatboxviewholder((layoutsv));

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull chatboxviewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
