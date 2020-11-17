package com.leejunj.prism.res;

import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/17<p>
 * <p>description : 管理静态注册主题<p>
 */
public class InternalResProvider {

    //TODO:这个数据结果会不会出现内容泄漏或者太复杂了？
    private Map<String, SkinType> mSkinMap;

    protected void init() {
        mSkinMap = new HashMap<>();
    }

    protected boolean registerSkin (SkinType skinType) {
        if (mSkinMap != null) {
            if (!mSkinMap.containsKey(skinType.getSkinName())) {
                mSkinMap.put(skinType.getSkinName(), skinType);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected boolean unregisterSkin (SkinType skinType) {
        if (mSkinMap != null) {
            return mSkinMap.remove(skinType.getSkinName(), skinType);
        } else {
            return false;
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - -

    private SkinType currentSkinType;

    SkinType getSkinType() {
        return currentSkinType;
    }

    void setSkinType(SkinType type) {
        this.currentSkinType = type;
    }

    //-----------以上是皮肤注册/注销，以下是对应资源查询-----------------

//    int getColor() {
//
//    }
//
//    Drawable getDrawable() {
//
//    }
}
