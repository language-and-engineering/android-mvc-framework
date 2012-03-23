package com.android_mvc.framework.ui.view;

import android.content.Context;
import android.widget.CheckBox;

/**
 * チェックボックスのラッパークラス。
 * @author id:language_and_engineering
 *
 */
public class MCheckBox extends CheckBox implements IFWView
{

    public MCheckBox(Context context) {
        super(context);
    }

    @Override
    public Object getViewParam(String key) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void setViewParam(String key, Object val) {
        // TODO 自動生成されたメソッド・スタブ

    }


    // 以下は属性操作


    // チェックを外す
    public MCheckBox unChecked() {
        this.setChecked(false);
        return this;
    }


}
