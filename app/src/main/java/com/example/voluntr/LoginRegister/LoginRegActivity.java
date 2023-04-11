package com.example.voluntr.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import com.example.voluntr.BaseActivity;
import com.example.voluntr.R;

public class LoginRegActivity extends BaseActivity {

    private Button mLogin, mRegister;
    private HorizontalScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg); //TODO: update everything in xml and remove scrollview

        mLogin = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);
       /** scrollView = (HorizontalScrollView) findViewById(R.id.scrollview);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollView.scrollTo(scrollView.getRight()/2, 0);
                scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }); **/


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginRegActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;


            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginRegActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });

    }
}