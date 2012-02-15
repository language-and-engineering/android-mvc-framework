package com.android_mvc.framework.ui.view;

import java.util.HashMap;



import android.content.Context;
import android.widget.EditText;

/**
 * EditTextのラッパークラス。
 * @author id:language_and_engineering
 *
 */
public class MEditText extends EditText implements IFWView
{

    public MEditText(Context context) {
        super(context);
    }


    // パラメータ保持
    HashMap<String, Object> view_params = new HashMap<String, Object>();

    @Override
    public Object getViewParam(String key) {
        return view_params.get(key);
    }

    @Override
    public void setViewParam(String key, Object val) {
        view_params.put(key, val);
    }


    // 以下は属性操作


    public MEditText widthPx(int px) {
        setWidth(px);
            // http://www.javadrive.jp/android/textview/index8.html
        return this;
    }

    public MEditText text(String s) {
        setText(s);
        return this;
    }

}
