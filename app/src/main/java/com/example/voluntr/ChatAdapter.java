package com.example.voluntr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<chatviewholder> {
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
    public chatviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        chatviewholder rcv = null;
        if(viewType==TYPE_LIST) {
            View layoutsv = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem, null, false);
            RecyclerView.LayoutParams layp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutsv.setLayoutParams(layp);
            rcv = new chatviewholder(layoutsv,viewType);
        }
        else if(viewType==TYPE_HEAD){
            View layoutsv = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatsheading, null, false);
            RecyclerView.LayoutParams layp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutsv.setLayoutParams(layp);
            rcv = new chatviewholder(layoutsv,viewType);

        }
        else{

        }


        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull chatviewholder holder, int position) {
        if(holder.view_Type==TYPE_LIST){
            holder.mChatId.setText(OrgList.get(position).getUserId());
            holder.mChatName.setText(OrgList.get(position).getName());
            if (!OrgList.get(position).getProfileImageUrl().equals("default")){
                Glide.with(context).load(OrgList.get(position).getProfileImageUrl()).into(holder.mOrgPic);
            }
        }
        else if (holder.view_Type==TYPE_HEAD) {
            holder.mChatHead.setText("Chats");

        }



    }

    @Override
    public int getItemCount() {
        return OrgList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0) {
            return TYPE_HEAD;
        }
            return TYPE_LIST;
        }


}
