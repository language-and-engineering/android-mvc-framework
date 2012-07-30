package com.android_mvc.framework.ui.view;

import android.content.Context;
import android.widget.ToggleButton;

/**
 * トグルボタンのラッパークラス。
 * @author id:language_and_engineering
 *
 */
public class MToggleButton extends ToggleButton implements IFWView
{

    public MToggleButton(Context context) {
        super(context);
        this.setChecked(false);
    }

    @Override
    public Object getViewParam(String key) {
        return null;
    }

    @Override
    public void setViewParam(String key, Object val) {
    }


    // 以下は属性操作


    public MToggleButton textOn(String s) {
        this.setTextOn(s);
        return this;
    }

    public MToggleButton textOff(String s) {
        this.setTextOff(s);
        return this;
    }

    public MToggleButton onCheck(OnCheckedChangeListener listener )
    {
        setOnCheckedChangeListener( listener );
        return this;
    }

    public MToggleButton checked() {
        this.setChecked(true);
        return this;
    }

    public MToggleButton unchecked() {
        this.setChecked(false);
        return this;
    }


}
