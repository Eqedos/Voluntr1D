package com.example.voluntr.TutorialPage;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class TossAnimation implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setCameraDistance(20000);
        if(position<0.5 && position>-0.5){
            page.setVisibility(View.VISIBLE);
        }
        else {
            page.setVisibility(View.INVISIBLE);
        }
        if(position<-1) {
            page.setAlpha(0);
        }
        else if(position<=0){
            page.setAlpha(1);
            page.setScaleX(Math.max(0.4f,(1-Math.abs(position))));
            page.setScaleY(Math.max(0.4f,(1-Math.abs(position))));
            page.setRotationX(1080*(1-Math.abs(position)+1));
            page.setTranslationY(-1000*Math.abs(position));
        }
        else if(position<=1){
            page.setAlpha(1);
            page.setScaleX(Math.max(0.4f,(1-Math.abs(position))));
            page.setScaleY(Math.max(0.4f,(1-Math.abs(position))));
            page.setRotationX(-1080*(1-Math.abs(position)+1));
            page.setTranslationY(-1000*Math.abs(position));

        }
        else {
            page.setAlpha(0);
        }
    }
}
