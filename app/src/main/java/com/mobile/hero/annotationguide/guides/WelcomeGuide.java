package com.mobile.hero.annotationguide.guides;

import android.util.Log;
import android.widget.Toast;

import com.mobile.hero.annotation.Guide;
import com.mobile.hero.api.BaseGuide;
import com.mobile.hero.api.utis.AndroidMainHandler;

import static com.mobile.hero.annotationguide.utils.Constants.DismissDelay;
import static com.mobile.hero.annotationguide.utils.Constants.LogGuideTag;

@Guide(group = "home", priority = 10)
public class WelcomeGuide extends BaseGuide {
    @Override
    public boolean ableShow() {
        return true;
    }

    @Override
    public void show() {
        Log.i(LogGuideTag, getClass().getSimpleName() + " show");
        Toast.makeText(getContext(), "welcome to my APP!!!", Toast.LENGTH_SHORT).show();
        AndroidMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, DismissDelay);
    }

    @Override
    public void dismiss() {
        Log.i(LogGuideTag, getClass().getSimpleName() + " dismiss");
        super.dismiss();
        Toast.makeText(getContext(), "goodbye，my dear!!!", Toast.LENGTH_SHORT).show();
    }
}
