package com.example.voluntr.Chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voluntr.ChatBox.ChatBoxActivity;
import com.example.voluntr.R;

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int TYPE_HEAD=0;
    private static final int TYPE_LIST=1;
    int view_Type;
   public TextView mChatHead;
    public TextView mChatId, mChatName;
    public ImageView mOrgPic;

    public ChatViewHolder(@NonNull View itemView, int viewType) {
        super(itemView);
        itemView.setOnClickListener(this);
        if(viewType==TYPE_LIST){
            mChatId = (TextView) itemView.findViewById(R.id.ChatID);
            mChatName = (TextView) itemView.findViewById(R.id.chatname);
            mOrgPic = (ImageView) itemView.findViewById(R.id.orgpic);
            view_Type=1;
        }
        else if (viewType==TYPE_HEAD) {
            mChatHead =(TextView) itemView.findViewById(R.id.chathead);
            view_Type=0;
        }
        else{}




    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "it works but not really",Toast.LENGTH_LONG);
        Intent intent = new Intent(view.getContext(), ChatBoxActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("chatId",mChatId.getText().toString());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);


    }
}
