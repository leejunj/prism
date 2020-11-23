package com.leejunj.prism.item;

import android.view.View;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/19<p>
 * <p>description : <p>
 */
public interface OnSkinChangedListener<T extends View> {
    void onChanged(T view);
}
