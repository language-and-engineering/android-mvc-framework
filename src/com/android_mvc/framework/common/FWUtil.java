package com.android_mvc.framework.common;

import com.android_mvc.framework.db.DBHelper;


/**
 * FW内でのコアな共通処理
 * @author id:language_and_engineering
 *
 */
public class FWUtil extends BaseUtil
{

    /**
     * アプリ起動時に，AP側の設定情報を受け取り，FW側に注入・初期化する。
     * 端末ブート時に自動起動するサービスからも利用される。
     */
    public static void receiveAppInfoAsFW( AbstractAppSettings settings )
    {
        // NOTE: FW側に存在する基底クラスIAppSettingsとして受理しているため，
        // 設定クラスでstaticメンバを使用できない点に注意。
        // もしstaticにすると，AP側でなくFW側のダミー値が返ってきてしまう。


        // ログのタグを初期化
        FWUtil.initAppTag( settings.getAppTagForLog() );


        // 開発モードのセット
        FWUtil.initDebuggingMode( settings.isDebuggingFlag() );

        // 開発時プリファレンスクリア設定のセット
        FWUtil.setForgetPrefOnDebug( settings.isForgetPrefsOnDebug() );

        // 開発時RDBクリア設定のセット
        FWUtil.setForgetRdbOnDebug( settings.isForgetRdbOnDebug() );


        // RDBの名称を登録
        DBHelper.setDB_NAME( settings.getDbName() );

        // RDBのファイルパスを登録
        DBHelper.setDB_FULLPATH( settings.getDbFullpath() );


        // GoogleMapsのAPI keyをセット
        FWUtil.setGoogleMapsAPIKey( settings.getGoogleMapsAPIKey() );

        // 他にAPから渡されるFW側の初期化処理

    }


}
