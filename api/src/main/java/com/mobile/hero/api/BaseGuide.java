package com.mobile.hero.api;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BaseGuide implements IGuide {
    private Context mCtx;
    private View mAnchor;
    private String group;
    private int priority;
    private List<OnDismiss> mOnDismiss;

    @Override
    public Context getContext() {
        return mCtx;
    }

    @Override
    public void setContext(Context context) {
        this.mCtx = context;
    }

    @Override
    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public void setAnchorView(View anchor) {
        this.mAnchor = anchor;
    }

    @Override
    public boolean ableShow() {
        return false;
    }

    @Override
    public void show() {
    }

    @Override
    public void dismiss() {

    }

    @Override
    public void addOnDismiss(OnDismiss onDismiss) {
        if (mOnDismiss == null) {
            mOnDismiss = new ArrayList<>();
        }
        if (mOnDismiss.contains(onDismiss)) {
            return;
        }
        mOnDismiss.add(onDismiss);
    }
}
