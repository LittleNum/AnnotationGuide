package com.mobile.hero.annotationguide.guides.detail;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.mobile.hero.annotation.Guide;
import com.mobile.hero.annotationguide.R;
import com.mobile.hero.api.BaseGuide;

@Guide(group = "detail", priority = 1, anchor = R.id.detail)
public class ReadGuide extends BaseGuide {
    PopupWindow mWindow;

    @Override
    public boolean ableShow() {
        return true;
    }

    @Override
    public void show() {
        Context ctx = getContext();
        View anchor = getAnchorView();
        if (ctx == null || anchor == null) {
            dismiss();
            return;
        }
        mWindow = new PopupWindow(ctx);
        View view = LayoutInflater.from(ctx).inflate(R.layout.detail_read_popupview, null, false);
        mWindow.setContentView(view);
        mWindow.showAtLocation(anchor, Gravity.TOP, 0, -anchor.getMeasuredHeight() - 200);
        mWindow.setOutsideTouchable(false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                dismiss();
            }
        });
        mWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mWindow != null && mWindow.isShowing()) {
            mWindow.dismiss();
        }
    }
}
