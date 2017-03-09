package com.liuhesan.app.distributionapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Tao on 2016/11/23.
 */

public class RelativeLayoutForButton extends RelativeLayout {
    public RelativeLayoutForButton(Context context) {
        super(context);
    }

    public RelativeLayoutForButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeLayoutForButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
