package com.android_mvc.framework.ui.view;

import java.util.HashMap;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * ImageViewのラッパークラス。
 * @author id:language_and_engineering
 *
 */
public class MImageView extends ImageView implements IFWView
{

    public MImageView(Context context, int resId) {
        super(context);
        this.setImageResource(resId);
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


    public MImageView paddingPx( int px ) {
        setPadding(px, px, px, px);
        return this;
    }

    public MImageView widthWrapContent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.WRAP_CONTENT );
        return this;
    }

    public MImageView widthFillParent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.FILL_PARENT );
        return this;
    }

    public MImageView heightWrapContent() {
        setViewParam("layout_height", ViewGroup.LayoutParams.WRAP_CONTENT );
        return this;
    }

    public MImageView visible() {
        setVisibility(VISIBLE);
        return this;
    }

    public MImageView invisible() {
        setVisibility(GONE);
        return this;
    }

    public MImageView click(OnClickListener l) {
        setOnClickListener(l);
        return this;
    }

    public MImageView unclickable() {
        setOnClickListener(null);
        return this;
    }

}
