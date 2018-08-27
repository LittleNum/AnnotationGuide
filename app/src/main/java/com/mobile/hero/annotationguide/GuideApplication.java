package com.mobile.hero.annotationguide;

import android.app.Application;

import com.mobile.hero.api.GuideUtil;

public class GuideApplication extends Application {
    public GuideApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GuideUtil.initialGuides();
    }
}
