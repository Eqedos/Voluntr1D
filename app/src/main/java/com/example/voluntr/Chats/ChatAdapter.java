package com.example.voluntr.Chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.voluntr.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private static final int TYPE_HEAD=0;
    private static final int TYPE_LIST=1;
    private List<DetailsOfOrg> OrgList;
    private Context context;

    public ChatAdapter(List<DetailsOfOrg> orgList, Context context){
        this.OrgList = orgList;
        this.context = context;
    }
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatViewHolder rcv = null;
        if(viewType==TYPE_LIST) {
            View layoutsv = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem, null, false);
            RecyclerView.LayoutParams layp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutsv.setLayoutParams(layp);
            rcv = new ChatViewHolder(layoutsv,viewType);
        }
        else if(viewType==TYPE_HEAD){
            View layoutsv = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatsheading, null, false);
            RecyclerView.LayoutParams layp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutsv.setLayoutParams(layp);
            rcv = new ChatViewHolder(layoutsv,viewType);

        }
        else{

        }


        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        if(holder.view_Type==TYPE_LIST){
            holder.mChatId.setText(OrgList.get(position-1).getUserId());
            holder.mChatName.setText(OrgList.get(position-1).getName());
            if (!OrgList.get(position-1).getProfileImageUrl().equals("default")){
                Glide.with(context).load(OrgList.get(position-1).getProfileImageUrl()).into(holder.mOrgPic);
            }
        }
        else if (holder.view_Type==TYPE_HEAD) {
            holder.mChatHead.setText("Chats");

        }



    }

    @Override
    public int getItemCount() {
        return OrgList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0) {
            return TYPE_HEAD;
        }
            return TYPE_LIST;
        }


}
