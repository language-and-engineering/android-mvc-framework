package com.android_mvc.sample_project.common;

import com.android_mvc.framework.common.AbstractAppSettings;


/**
 * アプリのユーザ定義設定項目を詰め込んだクラス。
 * このクラスの外部に設定内容が記述されている場合は，参照だけ張っておく。
 * @author id:language_and_engineering
 *
 */
public class AppSettings extends AbstractAppSettings
{
    // NOTE: FW用の設定項目については，各種getterは基底側に宣言されている。
    // ここには値の初期化処理だけを書けばよい。
    // 各フィールドをstaticにしない理由は，ポリモーフィズムとstatic変数を組み合わせると悲惨な目に遭うから。
    // また，このクラス上でインスタンス変数の宣言部分にデフォルト値を埋め込んでも無駄。
    // @see http://www.ne.jp/asahi/hishidama/home/tech/java/class_use.html#h3_field
    // なお，FW側に注入する必要のある設定項目については，基底クラスにgetterを設けること。

    // TODO: 込み入ってきたらenumで作り直し？


    /**
     * FW側の設定項目を初期化する。
     */
    protected void initForFW()
    {
        // --------------- このアプリの基本的な情報 ------------------


        // このアプリのログ出力時用のタグ
        APP_TAG_FOR_LOG = "android-mvc-sample";


        // RDBの物理名
        DB_NAME = "android_mvc_sample";


        // RDBのフルパス
        DB_FULLPATH = "/data/data/com.android_mvc.sample_project/databases/" + DB_NAME;


        // --------------- 開発用の設定項目 ------------------


        // 開発中モードかどうか
        DEBUGGING_FLAG = true;


        // 開発中モードであれば，アプリ起動時に毎回プリファレンスを削除するかどうか。
        // USB経由でインストールした際に，FW初期化フラグなんかを消去してやり直すために必要。
        FORGET_PREFS_ON_DEBUG = true;


        // 開発中モードであれば，アプリ起動時に毎回RDBを削除するかどうか。
        // USB経由で繰り返しデバッグする際に必要。
        FORGET_RDB_ON_DEBUG = true;

    }

}
