package com.leejunj.prism.app;

import android.os.Bundle;

import com.leejunj.prism.SkinManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/19<p>
 * <p>description : <p>
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SkinManager.getInstance().markActivity(this);

        super.onCreate(savedInstanceState);
    }
}
