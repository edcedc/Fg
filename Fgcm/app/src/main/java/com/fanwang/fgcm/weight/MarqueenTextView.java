package com.fanwang.fgcm.weight;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by edison on 2018/4/25.
 */

public class MarqueenTextView extends android.support.v7.widget.AppCompatTextView {

    public MarqueenTextView(Context context) {
        super(context);
    }

    public MarqueenTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        // DO NOTHING
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }
}

