package com.leejunj.prism.res;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.leejunj.prism.type.SkinType;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/17<p>
 * <p>description : 资源供应工具<p>
 */
public class ResProviderWrapper implements ResProvider{

    private static ResProviderWrapper sInstance;

    public static synchronized ResProviderWrapper getInstance() {
        if (sInstance == null) {
            sInstance = new ResProviderWrapper();
        }
        return sInstance;
    }

    //-----------------------------------------------

    //TODO:暂时先做成只对接静态注册皮肤包的状态，后续想办法兼容两个来源

    private InternalResProvider internalResProvider;
    private ExternalResProvider externalResProvider;

    public boolean init(Context context, String packagePath) {

        if (context == null || TextUtils.isEmpty(packagePath)) {
            return false;
        }

        internalResProvider = new InternalResProvider();
        internalResProvider.init(context, packagePath);
        externalResProvider = new ExternalResProvider();
        externalResProvider.init(context);

        return true;
    }

    @Override
    public boolean registerSkin(SkinType skinType) {
        return internalResProvider.registerSkin(skinType);
    }

    @Override
    public boolean unregisterSkin(SkinType skinType) {
        return internalResProvider.unregisterSkin(skinType);
    }

    @Override
    public SkinType getCurrentSkinType() {
        return internalResProvider.getCurrentSkinType();
    }

    @Override
    public boolean setCurrentSkinType(SkinType type) {
        return internalResProvider.setCurrentSkinType(type);
    }

    @Override
    public void restoreSkin() {
        internalResProvider.restoreSkin();
    }


    @Override
    public int getColor(int resId) {
        return internalResProvider.getColor(resId);
    }

    @Override
    public int getDrawableId(int resId) {
        return internalResProvider.getDrawableId(resId);
    }

    @Override
    public Drawable getDrawable(int resId) {
        return internalResProvider.getDrawable(resId);
    }
}
