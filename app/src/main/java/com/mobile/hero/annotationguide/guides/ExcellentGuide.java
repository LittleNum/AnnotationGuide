package com.mobile.hero.annotationguide.guides;

import android.util.Log;
import android.widget.Toast;

import com.mobile.hero.annotation.Guide;
import com.mobile.hero.api.BaseGuide;
import com.mobile.hero.api.utis.AndroidMainHandler;

import static com.mobile.hero.annotationguide.utils.Constants.DismissDelay;
import static com.mobile.hero.annotationguide.utils.Constants.LogGuideTag;

@Guide(group = "home", priority = 40)
public class ExcellentGuide extends BaseGuide {
    @Override
    public boolean ableShow() {
        return true;
    }

    @Override
    public void show() {
        Log.i(LogGuideTag, getClass().getSimpleName() + " show");
        Toast.makeText(getContext(), "Excellent guys~~~", Toast.LENGTH_SHORT).show();
        AndroidMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, DismissDelay);
    }
}
