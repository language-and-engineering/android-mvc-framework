package com.android_mvc.framework.activities;


import android.app.Activity;
import android.os.Bundle;

import com.google.android.maps.MapActivity;

/**
 * Map系Activityの基底クラス。
 * @author id:language_and_engineering
 *
 */
public abstract class BaseMapActivity extends MapActivity implements IBaseActivity
{
    // Activityの共通便利クラス
    protected CommonActivityUtil<BaseMapActivity> cau;

    // UI構築用
    protected Activity context;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = this;
        cau = new CommonActivityUtil<BaseMapActivity>();
        cau.onActivityCreated( this );
    }


    @Override
    protected boolean isRouteDisplayed()
    {
        // 子クラスでいちいちこのメソッドを記述する必要はない
        return false;
    }


    @Override
    public void procBeforeUI() {
    }


    @Override
    public boolean requireProcBeforeUI() {
        return false;
    }

}
