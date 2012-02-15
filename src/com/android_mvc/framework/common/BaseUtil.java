package com.android_mvc.framework.common;

import com.android_mvc.framework.annotations.SuppressDebugLog;

import android.util.Log;

/**
 * コアな共通処理
 * @author id:language_and_engineering
 *
 */
public class BaseUtil
{

    // デバッグログの出力対象となるコールスタックインデックス
    private static final int DEBUG_STACK_INDEX = 5;


    // デバッグログの抑制対象となるコールスタックインデックス
    private static final int SUPPRESS_STACK_INDEX = 5;


    /**
     * ログ用のタグ。アプリ起動時に，ユーザ定義アプリケーション側で上書きさせる。
     */
    protected static String APP_TAG = "TODO:";


    /**
     * 開発モード判定フラグ
     */
    protected static boolean DEBUGGING_FLAG;


    /**
     * 開発モード時にプリファレンスを初期化するフラグ
     */
    protected static boolean FORGET_PREFS_ON_DEBUG;


    /**
     * 開発モード時にRDBを初期化するフラグ
     */
    protected static boolean FORGET_RDB_ON_DEBUG;



    // ----------------- タグ関連


    /**
     * ログ用にタグを返す
     */
    private static String getAppTag()
    {
        return APP_TAG;
    };
        // http://okwave.jp/qa/q3705267.html


    /**
     * ログ用のタグをセット
     */
    private static void setAppTag( String s )
    {
        APP_TAG = s;
    }


    /**
     * ユーザ定義のタグをFW内でも使用可能にするために保持
     */
    public static void initAppTag( String s )
    {
        setAppTag( s );
    }


    // ----------------- ログ関連


    /**
     *  Log.dのラッパ
     */
    public static void d( String s )
    {
        // 対象クラスでデバッグログの出力が抑制されていたら，出力しない。
        if( mustSuppressDebugLogByAnnotation() )
        {
            // 抑制
            return;
        }

        // TODO: クラス単位ではなく，アプリ単位でデバッグログの出力を設定項目で制御できるように


        // 出力の初期化
        String output = "";

        // 開発中モードであれば，呼び出し元情報を含める
        if( isDebuggingMode() )
        {
            // NOTE: １回を２行に分割。呼び出し元を含めると，LogCat上であまりにも読みづらいから。
            output += getCallerMethodInfoAsString() + " : \n  ";
        }

        output += s;
        Log.d( getAppTag(), output );
    }


    /**
     * デバッグログを抑制すべきかどうか判定
     */
    private static boolean mustSuppressDebugLogByAnnotation() {

        // 対象クラスを取得
        Class<?> target_class = null;
        try {
            target_class = Class.forName( getTraceInfoByIndex( SUPPRESS_STACK_INDEX ).getClassName() );
        } catch (ClassNotFoundException e) {
            // 起こりえず
        }

        // クラスのアノテーション宣言と値を取得
        SuppressDebugLog ann = target_class.getAnnotation(SuppressDebugLog.class);
        if( ( ann != null ) && ann.value() ) // 宣言されていてtrue指定されていれば
        {
            return true;
        }
        else
        {
            return false;
        }
            // NOTE: もしエラーになったら，複数アノテーションをAndroidが裁けないバグを疑え
            // @see http://d.hatena.ne.jp/Kazzz/20100110/p1
    }


    /**
     *  Log.wのラッパ
     */
    public static void w( String s )
    {
        Log.w( getAppTag(), s );
    }


    /**
     *  Log.eのラッパ
     */
    public static void e( String s )
    {
        Log.e( getAppTag(), s );
    }


    /**
     * ロガーを呼び出したメソッドの情報を返す
     */
    private static String getCallerMethodInfoAsString()
    {
        StackTraceElement trace = getTraceInfoByIndex( DEBUG_STACK_INDEX );

        String class_name = trace.getClassName(); // FIXED:横長すぎて読みづらい！行分割して対処
        String method_name = trace.getMethodName();
            // @see http://koteitan.seesaa.net/article/171393826.html
            // http://okwave.jp/qa/q6341313.html

        return class_name + "#" + method_name;
    }


    /**
     * 特定の階層インデックスのトレース情報を返す。
     */
    private static StackTraceElement getTraceInfoByIndex( int index )
    {
        return Thread.currentThread().getStackTrace()[ index ];
    }


    // ----------------- デバッグ関連


    /**
     * デバッグ中（開発モード）かどうかを設定する
     */
    public static void initDebuggingMode( boolean b )
    {
        DEBUGGING_FLAG = b;
    }


    /**
     * デバッグ中（開発モード）かどうかを判定する
     */
    public static boolean isDebuggingMode()
    {
        return DEBUGGING_FLAG;
    }


    /**
     * 開発時プリファレンスクリアフラグをセット
     */
    public static void setForgetPrefOnDebug( boolean b )
    {
        FORGET_PREFS_ON_DEBUG = b;
    }


    /**
     * 開発時プリファレンスクリアフラグを取得
     */
    private static boolean mustForgetPrefIfDebug()
    {
        return FORGET_PREFS_ON_DEBUG;
    }


    /**
     * アプリ初期化時にプリファレンスを削除すべきかどうか
     */
    public static boolean mustClearPrefsForDebug()
    {
        d("isDebuggingMode = " + isDebuggingMode() + ", mustForgetPrefIfDebug = " + mustForgetPrefIfDebug() );
        return isDebuggingMode() && mustForgetPrefIfDebug();
    }


    /**
     * 開発時RDBクリアフラグを取得
     */
    public static boolean mustForgetRdbIfDebug()
    {
        return FORGET_RDB_ON_DEBUG;
    }


    /**
     * 開発時RDBクリアフラグを設定
     */
    public static void setForgetRdbOnDebug( boolean b )
    {
        FORGET_RDB_ON_DEBUG = b;
    }


    /**
     * アプリ初期化時にRDBを削除すべきかどうか
     */
    public static boolean mustClearRdbForDebug()
    {
        return isDebuggingMode() && mustForgetRdbIfDebug();
    }
}
