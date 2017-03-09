package com.liuhesan.app.distributionapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Tao on 2016/11/23.
 */

public class LinearLayoutForButton extends LinearLayout {
    public LinearLayoutForButton(Context context) {
        super(context);
    }

    public LinearLayoutForButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutForButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
