package com.mobile.hero.annotationguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mobile.hero.api.GuideUtil;

public class MainActivity extends AppCompatActivity {
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GuideUtil.bindGuide(this, "home");
        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                Intent it = new Intent(MainActivity.this, DetailActivity.class);
                MainActivity.this.startActivity(it);
            }
        });
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
