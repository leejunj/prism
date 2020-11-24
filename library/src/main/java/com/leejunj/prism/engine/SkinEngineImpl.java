package com.leejunj.prism.engine;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leejunj.prism.AttrType;
import com.leejunj.prism.SkinConfig;
import com.leejunj.prism.SkinManager;
import com.leejunj.prism.item.OnSkinChangedListener;
import com.leejunj.prism.item.SkinAttr;
import com.leejunj.prism.item.SkinItem;
import com.leejunj.prism.res.ResProviderWrapper;
import com.leejunj.prism.type.SkinType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/17<p>
 * <p>description : <p>
 */
public class SkinEngineImpl implements SkinEngine {

    private Map<Context, Map<View, SkinItem>> skinItems = new WeakHashMap<>();
    private Application mApplication;

    @Override
    public void build(Application application) {
        this.mApplication = application;
        ResProviderWrapper.getInstance().init(application, application.getPackageName());
    }

    @Override
    public boolean markActivity(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (layoutInflater.getFactory() == null) {
            layoutInflater.setFactory(new LayoutInflater.Factory() {
                @Nullable
                @Override
                public View onCreateView(@NonNull String s, @NonNull Context context, @NonNull AttributeSet attributeSet) {
                    return SkinManager.getInstance().onCreateView(s, context, attributeSet);
                }
            });
            return true;
        } else {
            return false;
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        int skinChangeableType = attrs.getAttributeIntValue(SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_CHANGEABLE, -1);
        if (skinChangeableType == -1) {  //未标记View
            return null;
        }
        View view = createView(name, context, attrs);
        if (view == null) {  //createView()的时候抛异常了
            return null;
        }
        List<AttrType> types = new ArrayList<>();
        //skin_changeable支持"background|textColor|src"这种多值写法
        if ((skinChangeableType & 1) == 1) {
            types.add(AttrType.background);
        } else if ((skinChangeableType & 2) == 2) {
            types.add(AttrType.textColor);
        } else if ((skinChangeableType & 4) == 4) {
            types.add(AttrType.src);
        }
        collectViewAttr(view, attrs, types);
        return view;
    }

    @Override
    public void addView(View target, int attrValueResId, AttrType type) {

        int id = attrValueResId;
        Resources res = mApplication.getResources();
        String resName = res.getResourceEntryName(id);  //属性值名字，如white_mcc
        String typeName = res.getResourceTypeName(id);

        List<SkinAttr> skinAttrs = new ArrayList<>();
        skinAttrs.add(new SkinAttr(type.toString(), typeName, attrValueResId, resName));
        SkinItem skinItem = new SkinItem(target, skinAttrs);
        if (ResProviderWrapper.getInstance().getCurrentSkinType() != null) {
            skinItem.change();
        }
        addSkinItem(target.getContext(), skinItem);
    }

    @Override
    public void addView(View view, OnSkinChangedListener listener) {

        SkinItem skinItem = new SkinItem(view, listener);
        if (ResProviderWrapper.getInstance().getCurrentSkinType() != null) {
            skinItem.change();
        }
        addSkinItem(view.getContext(), skinItem);
    }

    @Override
    public boolean registerSkin(SkinType skinType) {
        return ResProviderWrapper.getInstance().registerSkin(skinType);
    }

    @Override
    public boolean unregisterSkin(SkinType skinType) {
        return ResProviderWrapper.getInstance().unregisterSkin(skinType);
    }

    @Override
    public boolean changeSkin(SkinType type) {
        if (type == null
                || (ResProviderWrapper.getInstance().getCurrentSkinType() != null
                && ResProviderWrapper.getInstance().getCurrentSkinType().getSkinName().equals(type.getSkinName()))
        ) {
            return false;
        }
        if (ResProviderWrapper.getInstance().setCurrentSkinType(type)) {
            updateAllView();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void restoreSkin() {
        ResProviderWrapper.getInstance().restoreSkin();
        updateAllView();
    }

    @Override
    public void removeViews(Context context) {
        if (skinItems != null) {
            skinItems.remove(context);
        }
    }

    @Override
    public int getColor(int resId) {
        return ResProviderWrapper.getInstance().getColor(resId);
    }

    @Override
    public int getDrawableId(int resId) {
        return ResProviderWrapper.getInstance().getDrawableId(resId);
    }

    @Override
    public Drawable getDrawable(int resId) {
        return ResProviderWrapper.getInstance().getDrawable(resId);
    }
    //---------------------------------------

    private static View createView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = null;
        try {
            if (-1 == name.indexOf('.')) {  //TODO:研究下为什么不拆开时第一Activity的Button和TextView会抛NoClass
                //下面的三个前缀来自系统内部类 PhoneLayoutInflater.sClassPrefixList
                if ("View".equals(name)) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs);
                }
            } else {
                view = LayoutInflater.from(context).createView(name, null, attrs);
            }
        } catch (Exception e) {
            view = null;
        }
        return view;
    }

    /**
     * 保存被标记支持换肤的View
     * @param view 被标记的View
     * @param attrs 被标记view的所有属性
     * @param types 支持换肤的属性
     */
    private void collectViewAttr(View view, AttributeSet attrs, List<AttrType> types) {
        if (view == null || attrs == null || types == null || types.size() == 0  //异常数据
                || !(view.getContext() instanceof Activity)) {  //现有设计中view一定依附Activity，所以skinItems存的键值对中键是Activity
            return;
        }
        Context context = view.getContext();
        List<SkinAttr> skinAttrs = new ArrayList<>();
        int attCount = attrs.getAttributeCount();
        for (int i = 0; i < attCount; i++) {
            String attributeName = attrs.getAttributeName(i);  //可能是 background或textColor等
            String attributeValue = attrs.getAttributeValue(i);  //可能是 @721945706
            if (isSupportedAttr(view, attributeName, types) && attributeValue.startsWith("@")) {
                int resId = Integer.parseInt(attributeValue.substring(1));  //去掉最前面的"@"
                String resName = context.getResources().getResourceEntryName(resId);
                String attrType = context.getResources().getResourceTypeName(resId);
                skinAttrs.add(new SkinAttr(attributeName, attrType, resId, resName));  //只加入参与变色的属性
            }
        }
        SkinItem skinItem = new SkinItem(view, skinAttrs);
        if (ResProviderWrapper.getInstance().getCurrentSkinType() != null) {  //TODO:这个判断或者说这块逻辑要抉择一下，现在只是粘贴而已
            skinItem.change();
        }
        if (context instanceof Activity) {
            addSkinItem(context, skinItem);
        }
    }

    /**
     * 支持换肤的属性种类
     * @param view 将被标记的view
     * @param attributeName 将被标记的view的属性
     * @param types 需要支持换肤的属性类别
     * @return
     */
    private boolean isSupportedAttr(View view, String attributeName, List<AttrType> types) {
        if (TextUtils.isEmpty(attributeName)) {
            return false;
        }
        for (AttrType type : types) {
            if ((type.equals(AttrType.textColor) && !(view instanceof TextView))   //换textColor属性的View必须为TextView或子类
                    || (type.equals(AttrType.src) && !(view instanceof ImageView))) {  //换src属性的View必须为ImageView或子类
                continue;
            }
            if (TextUtils.equals(type.toString(), attributeName)) {
                return true;
            }
        }
        return false;
    }

    private void addSkinItem(Context activity, SkinItem item) {
        if(skinItems == null) {
            skinItems = new WeakHashMap<>();
        }
        if (skinItems.get(activity) == null) {
            skinItems.put(activity, new WeakHashMap<View, SkinItem>());
        }
        skinItems.get(activity).put(item.getView(), item);
    }

    private void updateAllView () {
        if (skinItems == null || skinItems.size() == 0) {
            return;
        }
        for (Map<View, SkinItem> skins: skinItems.values()) {
            for (SkinItem item : skins.values()) {
                item.change();
            }
        }
    }
}
