package com.example.voluntr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voluntr.ChatBoxActivity;
import com.example.voluntr.R;

public class chatviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mChatId, mChatName;
    public ImageView mOrgPic;
    public chatviewholder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mChatId = (TextView) itemView.findViewById(R.id.ChatID);
        mChatName = (TextView) itemView.findViewById(R.id.chatname);
        mOrgPic = (ImageView) itemView.findViewById(R.id.orgpic);

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
