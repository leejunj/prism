package com.leejunj.prism.engine;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.leejunj.prism.AttrType;
import com.leejunj.prism.item.OnSkinChangedListener;
import com.leejunj.prism.type.SkinType;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/17<p>
 * <p>description : <p>
 */
public interface SkinEngine {

    void build(Application application);

    boolean markActivity(Context context);

    View onCreateView(String name, Context context, AttributeSet attrs);

    void addView(Context activity, View target, int attrValueResId, AttrType type);

    void addView(Context activity, View view, OnSkinChangedListener listener);

    boolean registerSkin(SkinType skinType);

    boolean unregisterSkin (SkinType skinType);

    //void changeSkin();

    boolean changeSkin(SkinType type);

    void restoreSkin();

    void removeViews(Context context);

    int getColor(@ColorRes int resId);

    int getDrawableId(@DrawableRes int resId);

    Drawable getDrawable(@DrawableRes int resId);
}
