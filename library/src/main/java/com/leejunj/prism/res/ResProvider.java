package com.leejunj.prism.res;

import android.graphics.drawable.Drawable;

import com.leejunj.prism.type.SkinType;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/18<p>
 * <p>description : <p>
 */
public interface ResProvider {

    /**
     * 注册定制皮肤包
     * @param skinType
     * @return false=注册失败，已注册同类型皮肤包有同名存在
     */
    boolean registerSkin(SkinType skinType);

    /**
     * 注销定制皮肤包
     * @param skinType
     * @return false=注销失败，没找到要注销皮肤包
     */
    boolean unregisterSkin (SkinType skinType);

    SkinType getCurrentSkinType();

    boolean setCurrentSkinType(SkinType type);
    void restoreSkin();

    int getColor(int resId);
    int getDrawableId(int resId);
    Drawable getDrawable(int resId);
}
