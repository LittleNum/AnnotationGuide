package com.mobile.hero.api;

import android.content.Context;
import android.view.View;

public interface IGuide {
    Context getContext();

    void setContext(Context context);

    void setGroup(String group);

    String getGroup();

    void setPriority(int priority);

    int getPriority();

    void setAnchorView(View anchor);

    boolean ableShow();

    void show();

    void dismiss();

    void addOnDismiss(OnDismiss onDismiss);

    public interface OnDismiss {
        void onGuideDismiss(IGuide guide);
    }
}
