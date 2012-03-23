package com.android_mvc.framework.controller.routing;

import java.util.HashMap;

import android.app.Activity;


/**
 * タブホストに対して，「タブのタグ文字列」と「表示アクティビティ」のマッピングを伝達するためのオブジェクト。
 * @author id:language_and_engineering
 *
 */
public class TabContentMapping
{
    // マッピング
    private HashMap<String, Class<? extends Activity>> map = new HashMap<String, Class<? extends Activity>>();


    /**
     * 情報を一つ追加
     */
    public TabContentMapping add(String tabTag, Class<? extends Activity> activity_class)
    {
        map.put(tabTag, activity_class);
        return this;
    }


    /**
     * タグ文字列をもとに，情報を一つ取得。
     * 存在しない場合はnullを返す。
     */
    public Class<? extends Activity> getByTag( String tabTag )
    {
        return map.get(tabTag);
    }

}
