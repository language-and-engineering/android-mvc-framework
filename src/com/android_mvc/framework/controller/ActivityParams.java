package com.android_mvc.framework.controller;

import java.util.HashMap;

/**
 * Activityの現在の状態をパラメータとして保持するためのクラス。
 * @author id:language_and_engineering
 *
 */
public class ActivityParams
{

    private HashMap<String, Object> arr;


    public ActivityParams()
    {
        arr = new HashMap<String, Object>();
    }


    /**
     * 値を追加
     */
    public ActivityParams add(String key, Object val)
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

}
