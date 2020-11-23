package com.leejunj.prism.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.leejunj.prism.SkinManager;

import androidx.annotation.NonNull;
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
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        if (layoutInflater.getFactory() == null) {
            layoutInflater.setFactory(new LayoutInflater.Factory() {
                @Nullable
                @Override
                public View onCreateView(@NonNull String s, @NonNull Context context, @NonNull AttributeSet attributeSet) {
                    return SkinManager.getInstance().onCreateView(s, context, attributeSet);
                }
            });
        }

        super.onCreate(savedInstanceState);
    }
}
