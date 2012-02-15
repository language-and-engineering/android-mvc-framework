package com.android_mvc.framework.db.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * アプリのプリファレンスを扱う基底クラス。DAOと呼んでいいものやら。
 * @author id:language_and_engineering
 *
 */
public class BasePrefDAO
{

    protected static SharedPreferences.Editor mEditor = null;
    protected static SharedPreferences mSettings = null;


    /**
     * editorを取得
     */
    protected static SharedPreferences.Editor getEditor(Context context)
    {
        if( mEditor != null)
        {
            return mEditor;
        }

        mEditor = getSettings(context).edit();
        return mEditor;
    }


    /**
     * Preferencesを取得
     */
    protected static SharedPreferences getSettings(Context context)
    {
        if( mSettings != null)
        {
            return mSettings;
        }

        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        return mSettings;
    }


}
