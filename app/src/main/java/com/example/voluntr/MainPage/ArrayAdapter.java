package com.example.voluntr.MainPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.voluntr.R;

import java.util.List;

public class ArrayAdapter extends android.widget.ArrayAdapter<OrgCards> {

    Context context;

    public ArrayAdapter(Context context, int resourceId, List<OrgCards> items) {
        super(context, resourceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        OrgCards card_item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name2);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        name.setText(card_item.getName());
        distance.setText("   Distance:  "+card_item.getAge()+"m");
        switch (card_item.getProfileImageUrl()){
            case "default":
                Glide.with(convertView.getContext()).load(R.drawable.logofinal).into(image);
                break;
            default:
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }

        Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
        return convertView;

    }

}
