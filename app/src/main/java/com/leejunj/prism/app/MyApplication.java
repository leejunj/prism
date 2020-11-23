package com.leejunj.prism.app;

import android.app.Application;
import android.util.Log;

import com.leejunj.prism.SkinManager;
import com.leejunj.prism.type.SkinTypeSuffix;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/23<p>
 * <p>description : <p>
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().build(this);
        SkinManager.getInstance().registerSkin(new SkinTypeSuffix("_skin1"));
    }
}
