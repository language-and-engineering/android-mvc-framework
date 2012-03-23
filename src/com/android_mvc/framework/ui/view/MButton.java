package com.android_mvc.framework.ui.view;

import java.util.HashMap;



import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Buttonのラッパークラス。
 * @author id:language_and_engineering
 *
 */
public class MButton extends Button implements IFWView
{

    public MButton(Context context) {
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


    public MButton text( String s )
    {
        setText(s);
        return this;
    }

    public MButton click(OnClickListener l) {
        setOnClickListener(l);
        return this;
    }

	public MButton widthFillParent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.FILL_PARENT );
        return this;
	}


}
