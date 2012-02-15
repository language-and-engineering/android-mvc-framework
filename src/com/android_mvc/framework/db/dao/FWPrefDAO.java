package com.android_mvc.framework.db.dao;

import com.android_mvc.framework.common.FWUtil;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * FW内でプリファレンスを扱うクラス。
 * @author id:language_and_engineering
 *
 */
public class FWPrefDAO extends BasePrefDAO implements IFWDAO
{

    // ----------------- FW初期化済みフラグ -----------------


    /**
     * 初期化済みフラグを更新
     */
    public void updateFWInstallCompletedFlag( Context context, boolean b )
    {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean("FWInstallCompletedFlag", b);
        editor.commit();

        FWUtil.d("FWインストール済みフラグを変更：" + b);
    }


    /**
     * 初期化済みフラグを取得
     */
    public boolean getFWInstallCompletedFlag(Context context)
    {
        SharedPreferences settings = getSettings(context);
        boolean ret = settings.getBoolean("FWInstallCompletedFlag", false);
        FWUtil.d("アプリ初期化済みフラグは" +  ret);

        return ret;
    }


    // ----------------- その他 -----------------


    /**
     * 全削除（デバッグ用）
     */
    public void deleteAll( Context context )
    {
        FWUtil.d("プリファレンスの全削除を開始");

        SharedPreferences.Editor editor = getEditor(context);
        editor.clear();
        editor.commit();

        FWUtil.d("プリファレンスの全削除が完了");
    }

}
