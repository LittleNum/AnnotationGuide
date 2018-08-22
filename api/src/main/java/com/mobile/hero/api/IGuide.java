package com.mobile.hero.api;

import android.content.Context;
import android.view.View;

public interface IGuide {
    boolean ableShow();

    void show(Context context);

    void show(Context context, View anchor);

    void dismiss();

    void dismiss(Context context);
}
