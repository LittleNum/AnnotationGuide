package com.mobile.hero.annotationguide;

import android.app.Application;

import com.mobile.hero.api.GuideUtil;
import com.mobile.hero.api.config.GuideConfig;

public class GuideApplication extends Application {
    public GuideApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GuideUtil.initialConfig(GuideConfig.newBuild()
                .gap(2000)
                .firstDelay(2000)
                .build());
        GuideUtil.initialGuides();
    }
}
