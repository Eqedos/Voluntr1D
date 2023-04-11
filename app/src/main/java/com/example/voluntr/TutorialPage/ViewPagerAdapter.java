package com.example.voluntr.TutorialPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.voluntr.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    int images[]={ //add image ids in array
            R.drawable.feature1,
    R.drawable.feature2,
    R.drawable.feature3,
    R.drawable.feature4 };
    int headings[] ={ //headings in array
            R.string.feature1,
            R.string.feature2,
            R.string.feature3,
            R.string.feature4 };


    int description[] = {   //description of every feature in array
            R.string.desc1,
            R.string.desc2,
            R.string.desc3,
            R.string.desc4  };


    public ViewPagerAdapter(Context context) {
        this.context=context;
    }
    //implement methods
    @Override
    public int getCount() { //how many layouts we put in ViewPager
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    //called when ViewPager is scrolled
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= layoutInflater.inflate(R.layout.tutorial_slider, container, false);
        ImageView slidetitleimage = (ImageView)view.findViewById(R.id.titleImage);
        TextView slideheading = (TextView) view.findViewById(R.id.text_title);
        TextView slidedescription = (TextView) view.findViewById(R.id.textdescription);
        //set image, heading, description according to slide number
        slidetitleimage.setImageResource(images[position]);
        slideheading.setText(headings[position]);
        slidedescription.setText(description[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }


}
