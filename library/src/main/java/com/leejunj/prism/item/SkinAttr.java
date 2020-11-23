package com.leejunj.prism.item;

/**
 * <P>author : Leejunj<P>
 * <p>date : 2020/11/19<p>
 * <p>description : <p>
 */
public class SkinAttr {

    private String attrName;  //属性名，如background/textColor

    private String attrType;  //属性值，如boolean/color/drawable

    private int resId;  //属性id，如R.color.white_mcc

    private String resName;  //属性值，如white_mcc

    public SkinAttr(String attrName, String attrType, int resId, String resName) {
        this.attrName = attrName;
        this.attrType = attrType;
        this.resId = resId;
        this.resName = resName;
    }

    String getAttrName() {
        return attrName;
    }

    void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    String getAttrType() {
        return attrType;
    }

    void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    int getResId() {
        return resId;
    }

    void setResId(int resId) {
        this.resId = resId;
    }

    String getResName() {
        return resName;
    }

    void setResName(String resName) {
        this.resName = resName;
    }
}
