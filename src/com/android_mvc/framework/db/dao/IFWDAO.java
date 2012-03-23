package com.android_mvc.framework.db.dao;

import android.content.Context;

/**
 * FWのメタ情報を処理するためのインタフェース。
 * 必要時に，プリファレンスからRDBへの移植が容易になるように。
 * @author id:language_and_engineering
 */
public interface IFWDAO
{

    /**
     * 初期化済みフラグを更新
     */
    public void updateFWInstallCompletedFlag( Context context, boolean b );


    /**
     * 初期化済みフラグを取得
     */
    public boolean getFWInstallCompletedFlag(Context context);


    /**
     * 全削除（デバッグ用）
     */
    public void deleteAll( Context context );

}
