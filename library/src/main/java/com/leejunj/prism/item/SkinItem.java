package com.leejunj.prism.item;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leejunj.prism.AttrType;
import com.leejunj.prism.R;
import com.leejunj.prism.res.ResProviderWrapper;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/19<p>
 * <p>description : <p>
 */
public class SkinItem {

    private WeakReference<View> mViewWeakReference;
    private List<SkinAttr> attrs;  //参与变色的属性，非全部属性

    public SkinItem(View view, List<SkinAttr> attrs) {
        this.mViewWeakReference = new WeakReference<>(view);
        this.attrs = attrs;
    }

    public SkinItem(View view, OnSkinChangedListener listener) {
        this.mViewWeakReference = new WeakReference<>(view);
        this.attrs = null;
        view.setTag(R.id.tag_skin_listener, listener);
    }

    public View getView () {
        return mViewWeakReference.get();
    }

    public void change () {
        View view = mViewWeakReference != null ? mViewWeakReference.get() : null;
        if (view == null) {
            return;
        }
        //动态标记响应事件
        Object tag = view.getTag(R.id.tag_skin_listener);
        OnSkinChangedListener listener = null;
        if (tag instanceof OnSkinChangedListener) {
            listener = (OnSkinChangedListener) tag;
        }
        if (listener != null) {
            listener.onChanged(view);
        }
        if (attrs != null && attrs.size() > 0) {
            for (SkinAttr attr : attrs) {
                String attrName = attr.getAttrName();
                String attrType = attr.getAttrType();
                int resId = attr.getResId();

                if (AttrType.background.toString().equals(attrName)) {
                    if ("color".equals(attrType)) {
                        view.setBackgroundColor(ResProviderWrapper.getInstance().getColor(resId));
                    } else if ("drawable".equals(attrType)) {
                        view.setBackground(ResProviderWrapper.getInstance().getDrawable(resId));
                    }
                } else if (AttrType.textColor.toString().equals(attrName)
                        && view instanceof TextView) {
                    if ("color".equals(attrType)) {
                        ((TextView)view).setTextColor(ResProviderWrapper.getInstance().getColor(resId));
                    } else if ("drawable".equals(attrType)) {  //TextView可以设置drawable类型的textColor，比如selector颜色表示按下状态
                        ((TextView)view).setTextColor(
                                view.getContext().getResources().getColorStateList(
                                        ResProviderWrapper.getInstance().getDrawableId(resId)
                                )
                        );
                    }
                } else if (AttrType.src.toString().equals(attrName)
                        && view instanceof ImageView) {
                    if ("color".equals(attrType)) {
                        ((ImageView)view).setImageResource(ResProviderWrapper.getInstance().getColor(resId));
                    } else if ("drawable".equals(attrType)) {
                        ((ImageView)view).setImageDrawable(ResProviderWrapper.getInstance().getDrawable(resId));
                    }
                }
            }
        }
    }
}
