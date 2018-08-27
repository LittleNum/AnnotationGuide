package com.mobile.hero.api;

import android.content.Context;
import android.text.TextUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class GuideUtil {
    public static Map<String, GroupGuide> sGroupGuides = new HashMap<>();
    private static Map<String, String> sGuideNames;
    private static Map<String, String> sGuideGroup;
    private static Map<String, Integer> sGuidePriority;

    public static void initialGuides() {
        //init
        try {
            Class guideMgr = Class.forName("com.mobile.hero.annotationguide.guides" + ".GuideManager");
            GuideCollect collect = (GuideCollect) guideMgr.newInstance();
            sGuideNames = collect.getGuidesName();
            sGuideGroup = collect.getGuidesGroup();
            sGuidePriority = collect.getGuidesPriority();
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
                    newGuide.setContext(context);
                    newGuide.setPriority(priority);
                    newGuide.setGroup(group);
                    groupGuide.addGuideToGuide(newGuide);
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
