package com.android_mvc.framework.controller.routing;

import java.util.HashMap;

import android.app.Activity;

/**
 * あるアクティビティからの遷移先情報を，集約して保持する。
 * @author id:language_and_engineering
 *
 */
public class RoutingTable
{
    // ハッシュとして保持
    private HashMap<String, Class<? extends Activity>> table = new HashMap<String, Class<? extends Activity>>();


    /**
     * マッピング情報を一つ登録。
     * 遷移先Activityとして定数STAY_THIS_PAGEを渡せば，画面遷移を抑止できる。
     */
    public RoutingTable map(String route_id, Class<? extends Activity> destActivityClass)
    {
        table.put( route_id, destActivityClass );
        return this;
    }


    /**
     * ルーティング識別子をもとに，遷移先のアクティビティを返す。
     * 識別子が未登録の場合はnullを返す。
     */
    public Class<? extends Activity> getActivityByRouteId(String routeId)
    {
        return table.get(routeId);
    }


    //DIARY: このクラスを作らないでいた理由は，HashMapのラッパーの作りすぎで力尽きたからだった。
    //  http://d.hatena.ne.jp/language_and_engineering/20120213/p1
    // しかしこれを作っておけば，@SuppressWarnings("serial")しなくて済む。
    //  http://code.google.com/p/android-mvc-framework/source/browse/tags/20120215_ver0.1/src/com/android_mvc/sample_project/controller/Controller.java


}
