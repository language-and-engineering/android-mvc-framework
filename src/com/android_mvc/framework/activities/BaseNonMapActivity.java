package com.android_mvc.framework.activities;

import android.app.Activity;
import android.os.Bundle;


/**
 * 非Map系Activityの基底クラス。
 * @author id:language_and_engineering
 *
 */
public abstract class BaseNonMapActivity extends Activity implements IBaseActivity
{
    // Activityの共通便利クラス
    protected CommonActivityUtil<BaseNonMapActivity> cau;

    // UI構築用
    protected Activity context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = this; // NOTE: contextがないと後続処理ができないので注意
        cau = new CommonActivityUtil<BaseNonMapActivity>();
        cau.onActivityCreated( this );
    }


    @Override
    public void procBeforeUI() {
    }


    @Override
    public boolean requireProcBeforeUI() {
        return false;
    }

}
