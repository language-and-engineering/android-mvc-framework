package com.android_mvc.framework.ui.tab;

import java.util.ArrayList;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.controller.routing.TabContentMapping;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * XMLを使わずに，メソッドチェインによってタブ構造を構築するためのビルダー。
 * @author id:language_and_engineering
 *
 */
public class TabHostBuilder
{
    // NOTE: 現状では，Activityのホストのみ。

    private TabActivity activity;
    private TabHost tabhost;

    private TabContentMapping map;
    private ArrayList<TabDescription> descriptions = new ArrayList<TabDescription>();


    /**
     * 初期化。
     */
    public TabHostBuilder(Activity context)
    {
        this.activity = (TabActivity)context;
        this.tabhost = activity.getTabHost();
    }


    /**
     * コントローラから，タブのタグ文字列とアクティビティのマッピング情報を受け取って登録する。
     */
    public TabHostBuilder setChildActivities(TabContentMapping map)
    {
        this.map = map;
        return this;
    }


    /**
     * 各タブの詳細情報を登録する。
     * 任意個をいっぺんに登録可能。
     */
    public TabHostBuilder add(TabDescription... descs)
    {
        for( int i = 0; i < descs.length; i ++ )
        {
            this.descriptions.add(descs[i]);
        }
        return this;
    }


    /**
     * 登録済みのタブ詳細情報を使って，タブ構造全体の描画処理を行なう。
     */
    public void display()
    {
        // 全タブをHostに登録
        for( int i = 0; i < descriptions.size(); i ++ )
        {
            registerOneTabSpec( descriptions.get(i) );
        }
    }


    /**
     * １つのタブつまみの詳細情報を使って，タブのオブジェクトを生成する。
     */
    private void registerOneTabSpec(TabDescription desc)
    {
        // 詳細情報を取得
        String tabTag = desc.tabTag;
        String displayText = desc.displayText;
        int icon_resource_id = desc.icon_resource_id;

        // 1タブでホストする画面
        Class<? extends Activity> target_activity_class = map.getByTag(tabTag);
        if( target_activity_class == null )
        {
            FWUtil.e(
                "タブ画面の構築中にエラーが発生しました。タグ「"
                    + tabTag
                    + "」に対応するActivityがマッピング情報内に登録されていません。"
            );
            return;
        }

        // specとして再構築
        TabSpec tabspec = tabhost
            .newTabSpec( tabTag )
            .setContent(
                new Intent(activity, target_activity_class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        ;

        // アイコン画像の有無で場合分け
        if( icon_resource_id != 0 )
        {
            tabspec.setIndicator(displayText, activity.getResources().getDrawable( icon_resource_id ));
        }
        else
        {
            tabspec.setIndicator(displayText);
        }

        // タブにホストさせる
        tabhost.addTab( tabspec );
    }

}
