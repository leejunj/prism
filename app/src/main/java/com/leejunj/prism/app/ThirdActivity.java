package com.leejunj.prism.app;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/19<p>
 * <p>description : <p>
 */
public class ThirdActivity extends BaseActivity {

    private Button mBtnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        initView();
    }

    private void initView() {
        mBtnBack = findViewById(R.id.btn_back);

        mBtnBack.setOnClickListener((v) -> {
            onBackPressed();
        });
    }
}
