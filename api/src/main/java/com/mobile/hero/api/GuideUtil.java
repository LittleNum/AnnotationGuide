package com.mobile.hero.api;

import android.content.Context;
import android.text.TextUtils;

import com.mobile.hero.api.config.GuideConfig;

import java.util.HashMap;
import java.util.Map;

public class GuideUtil {
    private static Map<String, GroupGuide> sGroupGuides = new HashMap<>();
    private static Map<String, String> sGuideNames;
    private static Map<String, String> sGuideGroup;
    private static Map<String, Integer> sGuidePriority;
    private static Map<String, Integer> sGuideAnchor;
    private static GuideConfig sGuideConfig;

    public static void initialConfig(GuideConfig config) {
        sGuideConfig = config;
    }

    public static void initialGuides() {
        //init
        try {
            Class guideMgr = Class.forName("com.mobile.hero.annotationguide" + ".GuideCollector");
            GuideCollect collect = (GuideCollect) guideMgr.newInstance();
            sGuideNames = collect.getGuidesName();
            sGuideGroup = collect.getGuidesGroup();
            sGuidePriority = collect.getGuidesPriority();
            sGuideAnchor = collect.getGuidesAnchor();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void bindGuide(Context context, String group) {
        if (sGuideNames == null || sGuideNames.size() == 0) {
            return;
        }
        GroupGuide groupGuide = new GroupGuide(group);
        sGroupGuides.put(group, groupGuide);
        for (String g : sGuideNames.keySet()) {
            String gGroup = sGuideGroup.containsKey(g) ? sGuideGroup.get(g) : null;
            if (TextUtils.equals(group, gGroup)) {
                try {
                    IGuide newGuide = (IGuide) Class.forName(sGuideNames.get(g)).newInstance();
                    int priority = sGuidePriority.containsKey(g) ? sGuidePriority.get(g) : 0;
                    int anchor = sGuideAnchor.containsKey(g) ? sGuideAnchor.get(g) : 0;
                    newGuide.setContext(context);
                    newGuide.setPriority(priority);
                    newGuide.setGroup(group);
                    newGuide.setAnchorViewId(anchor);
                    groupGuide.addGuideToGuide(newGuide);
                    groupGuide.setConfig(sGuideConfig);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void triggerGuidesByGroup(String group) {
        if (sGroupGuides.containsKey(group)) {
            GroupGuide groupGuide = sGroupGuides.get(group);
            groupGuide.triggerGuide();
        }
    }

    public static void unBindGuide(String group) {
        if (sGroupGuides.containsKey(group)) {
            sGroupGuides.remove(group);
        }
    }
}
