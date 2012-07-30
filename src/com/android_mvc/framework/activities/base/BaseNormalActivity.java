package com.android_mvc.framework.activities.base;

import com.android_mvc.framework.activities.CommonActivityUtil;
import com.android_mvc.framework.activities.IBaseActivity;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.menu.OptionMenuBuilder;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


/**
 * 非Map系Activityの基底クラス。
 * @author id:language_and_engineering
 *
 */
public abstract class BaseNormalActivity extends Activity implements IBaseActivity
{



    // ここから下はBase系Activity間で共通



    // ----- 一般メンバ -----


    // Activityの共通便利クラス
    protected CommonActivityUtil<BaseNormalActivity> $;

    // UI構築用
    protected Activity context;


    @Override
    public ActivityParams toParams() {
        return null;
    }


    @Override
    public void afterBLExecuted(ActionResult ares)
    {
    }


    // ----- 画面初期化関連 -----


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = this;
        $ = new CommonActivityUtil<BaseNormalActivity>();
        $.onActivityCreated( this );
    }


    @Override
    public void procAsyncBeforeUI() {
    }


    @Override
    public boolean requireProcBeforeUI() {
        return false;
    }


    @Override
    public void afterViewDefined()
    {
    }


    // ------ メニュー関連 ------


    @Override
    public OptionMenuBuilder defineMenu()
    {
        return null;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu = $.renderOptionMenuAsDescribed( menu );
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        $.onOptionItemSelected(item);
        return super.onOptionsItemSelected(item);
    }


}
