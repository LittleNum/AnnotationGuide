package com.mobile.hero.annotationguide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mobile.hero.api.GuideUtil;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail_activity);
        GuideUtil.bindGuide(this, "detail");
    }

    @Override
    protected void onResume() {
        super.onResume();
        GuideUtil.triggerGuidesByGroup("detail");
    }

    @Override
    protected void onStop() {
        super.onStop();
        GuideUtil.unBindGuide("detail");
    }
}
