package com.mobile.hero.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mobile.hero.api.config.GuideConfig;
import com.mobile.hero.api.utis.AndroidMainHandler;

import java.util.Comparator;
import java.util.PriorityQueue;

import static com.mobile.hero.api.GuideConstant.InitialSize;
import static com.mobile.hero.api.GuideConstant.LogGuide;

public class GroupGuide {
    private String mGroup;
    private GuideConfig mConfig;
    private PriorityQueue<IGuideBridge> mGuideQueue = new PriorityQueue<>(InitialSize, new Comparator<IGuideBridge>() {
        @Override
        public int compare(IGuideBridge o1, IGuideBridge o2) {
            return Integer.valueOf(o1.getPriority()).compareTo(o2.getPriority());
        }
    });

    private GroupGuide() {

    }

    GroupGuide(@NonNull String group) {
        this();
        this.mGroup = group;
    }

    public GuideConfig getConfig() {
        return mConfig;
    }

    public void setConfig(GuideConfig mConfig) {
        this.mConfig = mConfig;
    }

    public void addGuideToGuide(IGuide guide) {
        if (guide != null && TextUtils.equals(guide.getGroup(), mGroup)) {
            mGuideQueue.add(new IGuideBridge(guide));
        }
    }

    public void triggerGuide() {
        IGuideBridge guide = pollGuideBridge();
        if (guide == null) {
            Log.i(LogGuide, getClass().getSimpleName() + " triggerGuide poll guide is null");
            return;
        }
        long delay = mConfig != null ? mConfig.getFirstDelay() : 0;
        if (delay > 0) {
            final IGuide target = guide;
            AndroidMainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    target.show();
                }
            }, delay);
        } else {
            guide.show();
        }
    }

    private IGuideBridge pollGuideBridge() {
        if (mGuideQueue != null && mGuideQueue.size() > 0) {
            IGuideBridge guide;
            while ((guide = mGuideQueue.poll()) == null || !guide.ableShow()) {
                if (mGuideQueue.isEmpty()) {
                    return null;
                }
            }
            return guide;
        }
        return null;
    }

    class IGuideBridge implements IGuide {
        IGuide guide;

        IGuideBridge(@NonNull IGuide guide) {
            this.guide = guide;
            addOnDismiss(new OnDismiss() {
                @Override
                public void onGuideDismiss(IGuide guide) {
                    Log.i(LogGuide, "onGuideDismiss " + guide);
                    next();
                }
            });
        }

        @Override
        public Context getContext() {
            return guide != null ? guide.getContext() : null;
        }

        @Override
        public void setContext(Context context) {
            if (guide != null) {
                guide.setContext(context);
            }
        }

        @Override
        public void setGroup(String group) {
            if (guide != null) {
                guide.setGroup(group);
            }
        }

        @Override
        public String getGroup() {
            return guide != null ? guide.getGroup() : null;
        }

        @Override
        public void setPriority(int priority) {
            if (guide != null) {
                guide.setPriority(priority);
            }
        }

        @Override
        public int getPriority() {
            return guide != null ? guide.getPriority() : 0;
        }

        @Override
        public void setAnchorViewId(int id) {
            if (guide != null) {
                guide.setAnchorViewId(id);
            }
        }

        @Override
        public int getAnchorViewId() {
            return guide != null ? guide.getAnchorViewId() : 0;
        }

        @Override
        public View getAnchorView() {
            return guide != null ? guide.getAnchorView() : null;
        }

        @Override
        public void setAnchorView(View anchor) {
            if (guide != null) {
                guide.setAnchorView(anchor);
            }
        }

        @Override
        public boolean ableShow() {
            return guide != null && guide.ableShow();
        }

        @Override
        public void show() {
            if (guide != null) {
                if (guide.ableShow()) {
                    guide.show();
                } else {
                    next();
                }
            }
        }

        @Override
        public void dismiss() {
            if (guide != null) {
                guide.dismiss();
            }
        }

        @Override
        public void addOnDismiss(OnDismiss onDismiss) {
            if (guide != null) {
                guide.addOnDismiss(onDismiss);
            }
        }

        @Override
        public void notifyDismiss() {
            if (guide != null) {
                guide.notifyDismiss();
            }
        }

        private void next() {
            IGuideBridge guide = pollGuideBridge();
            if (guide == null) {
                Log.i(LogGuide, getClass().getSimpleName() + " next poll guide is null");
                return;
            }
            long gap = mConfig != null ? mConfig.getGuideGap() : 0;
            if (gap > 0) {
                final IGuideBridge target = guide;
                AndroidMainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        target.show();
                    }
                }, gap);
            } else {
                guide.show();
            }
        }
    }
}
