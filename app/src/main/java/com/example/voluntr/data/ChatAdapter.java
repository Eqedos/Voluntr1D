package com.example.voluntr.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.voluntr.DetailsOfOrg;
import com.example.voluntr.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<chatviewholder> {
    private List<DetailsOfOrg> OrgList;
    private Context context;

    public ChatAdapter(List<DetailsOfOrg> orgList, Context context){
        this.OrgList = orgList;
        this.context = context;
    }
    @NonNull
    @Override
    public chatviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutsv = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem, null, false);
        RecyclerView.LayoutParams layp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutsv.setLayoutParams(layp);
        chatviewholder rcv = new chatviewholder((layoutsv));

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull chatviewholder holder, int position) {
        holder.mChatId.setText(OrgList.get(position).getUserId());
        holder.mChatName.setText(OrgList.get(position).getName());
        if (!OrgList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(OrgList.get(position).getProfileImageUrl()).into(holder.mOrgPic);
        }


    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
