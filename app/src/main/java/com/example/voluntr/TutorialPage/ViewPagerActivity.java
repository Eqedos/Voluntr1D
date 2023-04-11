package com.example.voluntr.TutorialPage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.voluntr.LoginRegister.LoginRegActivity;
import com.example.voluntr.R;

public class ViewPagerActivity extends AppCompatActivity {
    Button skipbtn;
    ViewPager mSlideViewPager;
    LinearLayout mDotLayout;
    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorialslider);
        skipbtn = findViewById(R.id.skip);
        mSlideViewPager =(ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);
        viewPagerAdapter = new ViewPagerAdapter(this);
        mSlideViewPager.setAdapter(viewPagerAdapter);
        mSlideViewPager.setPageTransformer(true, new TossAnimation());
        ViewPagerCustomDuration vp = (ViewPagerCustomDuration) findViewById(R.id.slideViewPager);
        vp.setScrollDurationFactor(3); // make the animation three times as slow
        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ViewPagerActivity.this, LoginRegActivity.class);
                startActivity(i);
                finish();
                return;


            }
        });
        setUpIndicator(0); //set first dot as active
        mSlideViewPager.addOnPageChangeListener(viewListener);
    }

    public void setUpIndicator(int position){ //adding dots to layout tutorial_slider
        dots=new TextView[4]; //4 dots
        mDotLayout.removeAllViews();
        for(int i=0; i< dots.length;i++)
        {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226")); //html code to add dots
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dotinactive));
            mDotLayout.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.dotactive));
    }
    ViewPager.OnPageChangeListener viewListener =new ViewPager.OnPageChangeListener() { //when user scrolls
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setUpIndicator(position); //pass position --> whichever position respective color will be active for dot


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private int getitem(int i) { //whenever we pass something, slideViewPager gives position of viewPager
        return mSlideViewPager.getCurrentItem()+i;
    }



}

