package com.example.voluntr.data;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voluntr.R;

import org.w3c.dom.Text;

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

    }
}
