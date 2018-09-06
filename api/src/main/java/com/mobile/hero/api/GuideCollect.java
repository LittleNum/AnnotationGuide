package com.mobile.hero.api;

import java.util.Map;

public interface GuideCollect {
    Map<String, String> getGuidesName();

    Map<String, String> getGuidesGroup();

    Map<String, Integer> getGuidesPriority();

    Map<String, Integer> getGuidesAnchor();
}
