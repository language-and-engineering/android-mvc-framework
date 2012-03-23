package com.android_mvc.framework.ui.view;

import java.util.HashMap;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * TextViewのラッパークラス。
 * @author id:language_and_engineering
 *
 */
public class MTextView extends TextView implements IFWView
{

    public MTextView(Context context) {
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


    public MTextView text(String s) {
        setText(s);
        return this;
    }

    public MTextView paddingPx( int px ) {
        setPadding(px, px, px, px);
        return this;
    }

    public MTextView widthWrapContent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.WRAP_CONTENT );
        return this;
    }

    public MTextView widthFillParent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.FILL_PARENT );
        return this;
    }

    public MTextView heightWrapContent() {
        setViewParam("layout_height", ViewGroup.LayoutParams.WRAP_CONTENT );
        return this;
    }

    public MTextView heightPx(int px) {
        setHeight(px);
        return this;
    }

    public MTextView textColor(int color){
        setTextColor(color);
            // 色の指定には，XMLではなく「android.graphics.Color」クラスを使う。
            // @see http://www.javadrive.jp/android/textview/index3.html
        return this;
    }

    public MTextView visible() {
        setVisibility(VISIBLE);
        return this;
    }

    public MTextView invisible() {
        setVisibility(GONE);
        return this;
    }


}
