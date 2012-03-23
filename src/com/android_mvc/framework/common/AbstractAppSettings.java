package com.android_mvc.framework.common;


/**
 * アプリのユーザ定義設定項目を詰め込んだクラスのインタフェース。
 * @author id:language_and_engineering
 *
 */
public abstract class AbstractAppSettings
{

    /* ------ 下記のメンバは，子クラス初期化時に本物の値をセットする。 ------ */

    protected String APP_TAG_FOR_LOG = "TODO:";

    protected String DB_NAME = "TODO:";

    protected String DB_FULLPATH = "TODO:";

    protected boolean DEBUGGING_FLAG = false;

    protected boolean FORGET_PREFS_ON_DEBUG = false;

    protected boolean FORGET_RDB_ON_DEBUG = false;

    /* ---------------------------------------------------------------------- */


    /**
     * FW用の設定項目を初期化する。
     */
    protected abstract void initForFW();


    /**
     * コンストラクタ内の処理を隠ぺい
     */
    public AbstractAppSettings()
    {
        // NOTE: この時点では，まだロガーのタグが初期化されていない。それを初期化するのがこの処理なので…。
            //FWUtil.d("デフォルトコンストラクタがコールされました。");

        initForFW();
            //FWUtil.d("FW用の設定項目を，AP側で定義完了。");
    }
        // NOTE: 引数のないコンストラクタである点がミソ。子クラス内で呼ばなくても自動的に処理させるために。
        // @see http://msugai.fc2web.com/java/overrideConstructor.html


    // 以下getter

    /**
     * このアプリのログ出力時用のタグ
     */
    public String getAppTagForLog() {
        return APP_TAG_FOR_LOG;
    }


    /**
     * RDBの物理名
     */
    public String getDbName() {
        return DB_NAME;
    }


    /**
     * RDBのフルパス
     */
    public String getDbFullpath() {
        return DB_FULLPATH;
    }


    /**
     * 開発中モードかどうか
     */
    public boolean isDebuggingFlag() {
        return DEBUGGING_FLAG;
    }


    /**
     * 開発中モードであれば，アプリ起動時に毎回プリファレンスを削除するかどうか。
     * USB経由でインストールした際に，FW初期化フラグなんかを消去してやり直すために必要。
     */
    public boolean isForgetPrefsOnDebug() {
        return FORGET_PREFS_ON_DEBUG;
    }


    /**
     * 開発中モードであれば，アプリ起動時に毎回DBを削除するかどうか。
     */
    public boolean isForgetRdbOnDebug() {
        return FORGET_RDB_ON_DEBUG;
    }

}
