package com.leejunj.prism.res;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;

import com.leejunj.prism.type.SkinType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/17<p>
 * <p>description : 管理动态注册主题<p>
 */
public class ExternalResProvider implements ResProvider{

    private Map<String, SkinType> mSkinMap;
    private SkinType currentSkinType;

    private Context mContext;
    private String defPackage;  //TODO:动态加载的皮肤包，PACKAGE可能不一样吧
    private Resources mOriginResources;  //应用自己的资源对象
    private Resources mResources;  //外部资源包对象 //TODO：注意是异步加载，立刻使用可能会出现空指针

    protected boolean init (Context context) {
        if (context == null) {
            return false;
        }
        mContext = context.getApplicationContext();
        mSkinMap = new HashMap<>();
        mOriginResources = context.getResources();
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
            return mSkinMap.remove(skinType.getSkinName(), skinType);
        } else {
            return false;
        }
    }

    @Override
    public SkinType getCurrentSkinType() {
        return this.currentSkinType;
    }

    @Override
    public boolean setCurrentSkinType(SkinType type) {
        if (mSkinMap != null) {
            if (mSkinMap.containsKey(type.getSkinName())) {
                currentSkinType = type;
                //异步加载
                new LoadSkinTask().execute("外部包地址");  //TODO:想办法拼出来完整地址
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

    @Override
    public int getColor(int resId) {
        int originColor = mOriginResources.getColor(resId);
        if (currentSkinType == null || mResources == null) {
            return originColor;
        }
        String resName = mOriginResources.getResourceEntryName(resId);
        int newResId = mResources.getIdentifier(resName, "color", defPackage);
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
        String resName = mOriginResources.getResourceEntryName(resId);
        return mResources.getIdentifier(resName, "drawable", defPackage);
    }

    @Override
    public Drawable getDrawable(int resId) {
        Drawable originDrawable = mOriginResources.getDrawable(resId);
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

    //----------------------------------------

    public class LoadSkinTask extends AsyncTask<String, Void, Resources> {

        @Override
        protected Resources doInBackground(String... paths) {
            try {
                if (paths.length == 1) {
                    String skinPkgPath = paths[0];
                    File file = new File(skinPkgPath);
                    if (!file.exists()) {
                        return null;
                    }
                    PackageManager mPm = mContext.getPackageManager();
                    PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPkgPath, PackageManager.GET_ACTIVITIES);
                    defPackage = mInfo.packageName;
                    AssetManager assetManager = AssetManager.class.newInstance();
                    AssetManager.class.getMethod("addAssetPath", String.class)
                            .invoke(assetManager, defPackage);
                    Resources skinResource = new Resources(
                            assetManager,
                            mOriginResources.getDisplayMetrics(),
                            mOriginResources.getConfiguration()
                    );
                    return skinResource;
                }
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Resources resources) {
            super.onPostExecute(resources);
            mResources = resources;
            //TODO:实例化成功后要开始变色行为了
        }
    }
}
