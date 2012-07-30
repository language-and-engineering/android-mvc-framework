package com.android_mvc.framework.activities.base;


import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android_mvc.framework.activities.CommonActivityUtil;
import com.android_mvc.framework.activities.IBaseActivity;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.menu.OptionMenuBuilder;

/**
 * タブの親Activityの基底クラス。
 * @author id:language_and_engineering
 *
 */
public abstract class BaseTabHostActivity extends TabActivity implements IBaseActivity
{

    // ----- TabActivity専用の記述 -----


    // NOTE: TabHost構造は3.0以降のタブレットのご時世ではDeprecated。
    // しかし，2.3以前でFragmentsを利用可能にするためにあれやこれやも
    // めんどい＆不安定なので，TabHostでゆく。自分の端末が2.3だし…。
    // http://www.atmarkit.co.jp/fsmart/articles/android25/01.html



    // ここから下はBase系Activity間で共通



    // ----- 一般メンバ -----


    // Activityの共通便利クラス
    protected CommonActivityUtil<BaseTabHostActivity> $;

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
        $ = new CommonActivityUtil<BaseTabHostActivity>();
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
