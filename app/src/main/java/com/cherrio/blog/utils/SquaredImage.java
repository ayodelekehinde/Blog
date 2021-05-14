package com.cherrio.blog.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by User on 6/4/2017.
 */

public class SquaredImage extends AppCompatImageView {

    public SquaredImage(Context context) {
        super(context);
    }

    public SquaredImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquaredImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
