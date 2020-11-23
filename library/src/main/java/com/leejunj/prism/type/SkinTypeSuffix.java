package com.leejunj.prism.type;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/23<p>
 * <p>description : 后缀标记方式<p>
 *     如果标记法是同名资源加后缀可用此类，
 *     省略实现接口时的模版代码。
 */
public class SkinTypeSuffix implements SkinType{

    private String skinName;

    public SkinTypeSuffix (String suffix) {
        this.skinName = suffix;
    }

    @Override
    public String getSkinName() {
        return skinName;
    }

    @Override
    public String constitute(String resName) {
        return resName + getSkinName();
    }
}
