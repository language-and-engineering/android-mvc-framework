package com.android_mvc.framework.controller.validation;

import java.util.HashMap;

/**
 * Activityの現在の状態をパラメータとして保持するためのクラス。
 * @author id:language_and_engineering
 *
 */
public class ActivityParams
{

    private HashMap<String, Object> arr_values;
    private HashMap<String, Object> arr_names;


    public ActivityParams()
    {
        arr_values = new HashMap<String, Object>();
        arr_names = new HashMap<String, Object>();
    }


    /**
     * 情報を追加
     */
    public ActivityParams add(String logical_name, String physical_key, Object val)
    {
        arr_values.put(physical_key, val);
        arr_names.put(physical_key, logical_name);
        return this;
    }


    /**
     * 値を取得
     */
    public Object getValue(String key)
    {
        return arr_values.get(key);
    }


    /**
     * 論理名を取得
     */
    public Object getName(String key)
    {
        return arr_names.get(key);
    }

}
