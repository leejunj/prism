package com.leejunj.prism;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.leejunj.prism.engine.SkinEngine;
import com.leejunj.prism.engine.SkinEngineImpl;
import com.leejunj.prism.item.OnSkinChangedListener;
import com.leejunj.prism.type.SkinType;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/19<p>
 * <p>description : <p>
 */
public class SkinManager {

    private static SkinEngine engine = new SkinEngineImpl();

    private static SkinManager sInstance;

    private SkinManager() {
    }

    public static synchronized SkinManager getInstance() {
        if (sInstance == null) {
            sInstance = new SkinManager();
        }
        return sInstance;
    }

    //--------------------------对外提供方法----------------------------

    /**
     * 使用前必须调用方法
     * @param application 用于构筑内部{@link android.content.res.Resources}对象，不会保留引用，避免内存泄露
     */
    public void build(Application application) {
        engine.build(application);
    }

    public boolean markActivity(Context context) {
        return engine.markActivity(context);
    }

    /**
     * 必须在Activity.setContentView()之前设置进LayoutInflater.setFactory()
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return engine.onCreateView(name, context, attrs);
    }

    public boolean registerSkin(SkinType skinType) {
        return engine.registerSkin(skinType);
    }

    public boolean unregisterSkin (SkinType skinType) {
        return engine.unregisterSkin(skinType);
    }

    public boolean changeSkin(SkinType type) {
        return engine.changeSkin(type);
    }

    public void restoreSkin() {
        engine.restoreSkin();
    };

    //---------------------动态标记管理----------------------

    public void addView(View target, int attrValueResId, AttrType type) {
        engine.addView(target, attrValueResId, type);
    }

    public void addView(View view, OnSkinChangedListener listener) {
        engine.addView(view, listener);
    }

    public void removeViews(Context context) {
        engine.removeViews(context);
    }

    //----------------------资源查询-------------------------

    public int getColor(int id) {
        return engine.getColor(id);
    }

    public Drawable getDrawable(int id) {
        return engine.getDrawable(id);
    }

    public int getDrawableId(int id) {
        return engine.getDrawableId(id);
    }
}
