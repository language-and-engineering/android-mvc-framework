package com.android_mvc.framework.controller;

import java.util.HashMap;

import android.app.Activity;

/**
 * BLの実行結果を詰め込むクラス。
 * @author id:language_and_engineering
 *
 */
public class ActionResult {

    private HashMap<String, Object> arr;
    private String route_id;


    public ActionResult()
    {
        arr = new HashMap<String, Object>();
    }


    /**
     * 値を追加
     */
    public ActionResult add(String key, Object val)
    {
        arr.put(key, val);
        return this;
    }


    /**
     * 値を取得
     */
    public Object get(String key)
    {
        return arr.get(key);
    }


    /**
     * BL実行完了後の画面にて
     * 何かさせたければoverrideしてね
     */
    public void onNextActivityStarted(Activity activity)
    {
    }


    /**
     * 遷移先を識別するための識別子をセット
     */
    public ActionResult setRouteId( String s )
    {
        this.route_id = s;
        return this;
    }


    /**
     * 遷移先を識別するための識別子を取得
     */
    public String getRouteId()
    {
        return this.route_id;
    }

}
