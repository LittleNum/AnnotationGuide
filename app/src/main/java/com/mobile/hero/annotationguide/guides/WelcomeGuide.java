package com.mobile.hero.annotationguide.guides;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.mobile.hero.annotation.Guide;
import com.mobile.hero.api.BaseGuide;
import com.mobile.hero.api.IGuide;

@Guide(group = "home", priority = 10)
public class WelcomeGuide extends BaseGuide {
    @Override
    public boolean ableShow() {
        return true;
    }

    @Override
    public void show() {
        Toast.makeText(getContext(), "welcome to my APP!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void dismiss() {
        Toast.makeText(getContext(), "goodbye，my dear!!!", Toast.LENGTH_LONG).show();
    }
}
