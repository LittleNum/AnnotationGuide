package com.mobile.hero.annotationguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mobile.hero.api.GuideUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GuideUtil.bindGuide(this, "home");
    }

    @Override
    protected void onResume() {
        super.onResume();
        GuideUtil.triggerGuidesByGroup("home");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GuideUtil.unBindGuide("home");
    }
}
