package com.leejunj.prism;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    public CustomTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setText("测试控件");
    }
}
