package com.android_mvc.sample_project.controller;


import android.content.Intent;

import com.android_mvc.sample_project.activities.func_db.DBEditActivity;
import com.android_mvc.sample_project.activities.func_db.DBListActivity;
import com.android_mvc.sample_project.activities.func_db.SampleTabHostActivity;
import com.android_mvc.sample_project.activities.func_html.SampleHtmlActivity;
import com.android_mvc.sample_project.activities.func_html.SampleJQueryMobileActivity;
import com.android_mvc.sample_project.activities.func_map.SampleMapActivity;
import com.android_mvc.sample_project.activities.func_visual.SampleAnimationActivity;
import com.android_mvc.sample_project.activities.installation.InstallAppActivity;
import com.android_mvc.sample_project.activities.installation.InstallCompletedActivity;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.framework.controller.BaseController;
import com.android_mvc.framework.controller.routing.Router;
import com.android_mvc.framework.controller.routing.RoutingTable;


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

        // 送られてきたボタンタイプに応じて，遷移先を振り分ける。

        // extra付きの遷移を実行
        if( "EDIT_DB".equals(button_type) )
        {
            Router.goWithData(activity, DBEditActivity.class, "DB編集画面へ",
                new Intent().putExtra("hoge", "Intentで値を渡すテスト").putExtra("fuga", 1)
            );
        }
        else
        {
            // extraのない遷移を実行
            Router.goByRoutingTable(activity, button_type,
                new RoutingTable()
                    .map("VIEW_DB",       DBListActivity.class,             "DB一覧画面へ")
                    .map("TAB_SAMPLE",    SampleTabHostActivity.class,      "タブ画面へ")
                    .map("MAP_SAMPLE",    SampleMapActivity.class,          "マップ画面へ")
                    .map("HTML_SAMPLE",   SampleHtmlActivity.class,         "HTMLのサンプル画面へ")
                    .map("JQUERY_SAMPLE", SampleJQueryMobileActivity.class, "jQuery Mobileのサンプル画面へ")
                    .map("ANIM_SAMPLE",   SampleAnimationActivity.class,    "アニメーションのサンプル画面へ")
            );
        }

/*
    NOTE: 下記のように書くのと同じ。

        if( "VIEW_DB".equals(button_type) )
        {
            // 一覧画面へ
            Router.go(activity, DBListActivity.class);
        }
        if( "TAB_SAMPLE".equals(button_type) )
        {
            // タブ画面へ
            Router.go(activity, SampleTabHostActivity.class);
        }
        if( "MAP_SAMPLE".equals(button_type) )
        {
            // マップ画面へ
            Router.go(activity, SampleMapActivity.class);
        }
        if( "HTML_SAMPLE".equals(button_type) )
        {
            // HTMLのサンプル画面へ
            Router.go(activity, SampleHtmlActivity.class);
        }
        if( "JQUERY_SAMPLE".equals(button_type) )
        {
            // jQuery Mobileのサンプル画面へ
            Router.go(activity, SampleJQueryMobileActivity.class);
        }
        if( "ANIM_SAMPLE".equals(button_type) )
        {
            // アニメーションのサンプル画面へ
            Router.go(activity, SampleAnimationActivity.class);
        }
*/

    }


}
