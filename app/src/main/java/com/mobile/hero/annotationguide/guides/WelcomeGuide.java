package com.mobile.hero.annotationguide.guides;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.mobile.hero.annotation.Guide;
import com.mobile.hero.api.IGuide;

@Guide(group = "home",priority = 10)
public class WelcomeGuide implements IGuide {
    @Override
    public boolean ableShow() {
        return true;
    }

    @Override
    public void show(Context context) {
        Toast.makeText(context, "welcome to my APP!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void show(Context context, View anchor) {

    }

    @Override
    public void dismiss() {

    }

    @Override
    public void dismiss(Context context) {
        Toast.makeText(context, "goodbye, my dear guest...", Toast.LENGTH_LONG).show();
    }
}
