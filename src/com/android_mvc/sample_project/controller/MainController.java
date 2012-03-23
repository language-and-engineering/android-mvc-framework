package com.android_mvc.sample_project.controller;


import android.content.Intent;

import com.android_mvc.sample_project.activities.func_db.DBEditActivity;
import com.android_mvc.sample_project.activities.func_db.DBListActivity;
import com.android_mvc.sample_project.activities.func_db.SampleTabHostActivity;
import com.android_mvc.sample_project.activities.installation.InstallAppActivity;
import com.android_mvc.sample_project.activities.installation.InstallCompletedActivity;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.framework.controller.BaseController;
import com.android_mvc.framework.controller.routing.Router;


/**
 * メイン系画面のコントローラ。
 * @author id:language_and_engineering
 *
 */
public class MainController extends BaseController
{

    // 遷移元となるActivityごとに，submit()をオーバーロードする。


    /**
     * インストール画面からの遷移時
     */
    public static void submit(InstallAppActivity installAppActivity, boolean installExecutedFlag)
    {
        // インストールをスキップしたかどうか
        if( installExecutedFlag )
        {
            // インストール完了画面へ
            Router.go(installAppActivity, InstallCompletedActivity.class);
        }
        else
        {
            // トップ画面へ
            Router.go(installAppActivity, TopActivity.class);
        }
    }


    /**
     * インストール完了画面からの遷移時
     */
    public static void submit(InstallCompletedActivity activity) {
        // トップ画面へ
        Router.go(activity, TopActivity.class);
    }


    /**
     * TOP画面からの遷移時
     */
    public static void submit(TopActivity activity, String button_type) {
        if( "EDIT_DB".equals(button_type) )
        {
            // 編集画面へ
            Router.goWithData(activity, DBEditActivity.class,
                new Intent().putExtra("hoge", "Intentで値を渡すテスト").putExtra("fuga", 1)
            );
        }
        else
        if( "VIEW_DB".equals(button_type) )
        {
            // 一覧画面へ
            Router.go(activity, DBListActivity.class);
        }
        else
        if( "TAB_SAMPLE".equals(button_type) )
        {
            // タブ画面へ
            Router.go(activity, SampleTabHostActivity.class);
        }
    }


}
