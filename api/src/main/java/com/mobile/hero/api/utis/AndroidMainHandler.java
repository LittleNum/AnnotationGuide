package com.mobile.hero.api.utis;

import android.os.Handler;
import android.os.Looper;

public class AndroidMainHandler {
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void post(Runnable action) {
        sHandler.post(action);
    }

    public static void postDelayed(Runnable action, long delayMills) {
        sHandler.postDelayed(action, delayMills);
    }

    public static void removeCallbacks(Runnable action) {
        sHandler.removeCallbacks(action);
    }

    public static void removeCallbacks(Runnable action, Object token) {
        sHandler.removeCallbacks(action, token);
    }

    public static void removeCallbacksAndMessages(Object token) {
        sHandler.removeCallbacksAndMessages(token);
    }
}
