package com.leejunj.prism.res;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;

import com.leejunj.prism.type.SkinType;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/17<p>
 * <p>description : 管理静态注册主题<p>
 */
public class InternalResProvider implements ResProvider{

    //TODO:这个数据结果会不会出现内容泄漏或者太复杂了？
    private Map<String, SkinType> mSkinMap;

    private String defPackage;
    private Resources mResources;

    //TODO:context和packagePath能不能用注入方式加入，省的改构造函数
    protected boolean init(Context context, String packagePath) {
        if (context == null || TextUtils.isEmpty(packagePath)) {
            return false;
        }
        mSkinMap = new HashMap<>();
        mResources = context.getResources();
        defPackage = packagePath;
        return true;
    }
    @Override
    public boolean registerSkin(SkinType skinType) {
        if (mSkinMap == null) {
            mSkinMap = new HashMap<>();
        }
        if (!mSkinMap.containsKey(skinType.getSkinName())) {
            mSkinMap.put(skinType.getSkinName(), skinType);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean unregisterSkin(SkinType skinType) {
        if (mSkinMap != null) {
            return mSkinMap.remove(skinType.getSkinName()) != null;
        } else {
            return false;
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - -

    /**
     * 标记当前选中的皮肤（如用户未选中定制皮肤或选中了动态加载皮肤时为null）
     */
    private SkinType currentSkinType;

    @Override
    public SkinType getCurrentSkinType() {
        return this.currentSkinType;
    }

    @Override
    public boolean setCurrentSkinType(SkinType type) {
        if (mSkinMap != null) {
            if (mSkinMap.containsKey(type.getSkinName())) {
                currentSkinType = type;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void restoreSkin() {
        currentSkinType = null;
    }

    //-----------以上是皮肤注册/注销----------------
    //-----------以下是对应资源查询-----------------

    @Override
    public int getColor(int resId) {
        int originColor = mResources.getColor(resId);
        if (currentSkinType == null) {
            return originColor;
        }
        String resName = mResources.getResourceEntryName(resId);
        int newResId = mResources.getIdentifier(getNewResName(resName), "color", defPackage);
        int newColor;
        try {
            newColor = mResources.getColor(newResId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return originColor;
        }
        return newColor;
    }

    @Override
    public int getDrawableId(int resId) {
        if (currentSkinType == null) {
            return resId;
        }
        String resName = mResources.getResourceEntryName(resId);
        int newResId = mResources.getIdentifier(getNewResName(resName), "drawable", defPackage);
        if (newResId != 0) {
            return newResId;
        } else {
            return resId;
        }
    }

    @Override
    public Drawable getDrawable(int resId) {
        Drawable originDrawable = mResources.getDrawable(resId);
        if (currentSkinType == null) {
            return originDrawable;
        }
        int newResId = getDrawableId(resId);
        Drawable newDrawable;
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                newDrawable = mResources.getDrawable(newResId);
            } else {
                newDrawable = mResources.getDrawable(newResId, null);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return originDrawable;
        }
        return newDrawable;
    }

    //-------------------------------------------------

    private String getNewResName(String name) {
        return currentSkinType.constitute(name);
    }
}
