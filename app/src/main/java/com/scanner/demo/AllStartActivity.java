package com.scanner.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.scanner.demo.BrustMode.MainActivity;

public class AllStartActivity extends AppCompatActivity {
    private Button brustBtn,ourBtn;
    private Animation bottom, top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_single);
        ourBtn = findViewById(R.id.our_btn);
        brustBtn = findViewById(R.id.brust_btn);
        //Animation Assign
        bottom = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        top = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        ourBtn.setAnimation(bottom);
        brustBtn.setAnimation(top);
    }

    public void start(View view) {
        startActivity(new Intent(this,SingleMainActivity.class));
    }

    public void brust(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }
}
