package com.example.voluntr;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class chatboxviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public chatboxviewholder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }
}
