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

    void setAnchorViewId(int id);

    int getAnchorViewId();

    void setAnchorView(View anchor);

    View getAnchorView();

    boolean ableShow();

    void show();

    void dismiss();

    void addOnDismiss(OnDismiss onDismiss);

    void notifyDismiss();

    interface OnDismiss {
        void onGuideDismiss(IGuide guide);
    }
}
