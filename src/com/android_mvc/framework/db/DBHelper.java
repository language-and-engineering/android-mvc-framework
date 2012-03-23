package com.android_mvc.framework.db;

import java.io.File;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.db.schema.AbstractSchemaDefinition;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBをオープンするクラス。
 * @author id:language_and_engineering
 */
public class DBHelper extends SQLiteOpenHelper
{

    // データベース名
    public static String DB_NAME = ""; // AP側から初期化される

    // データベースファイル名
    private static String DB_FULL_PATH = "";

    // データベースバージョン
    private static final int DB_VERSION = 1; // TODO:


    // DB構造の定義（初期化用）
    AbstractSchemaDefinition schemaDefinition;


    /**
     * コンストラクタ
     */
    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);

        //this.context  = context;
    }


    /**
     * スキーマが存在しなければ作成する
     */
    public void createSchemaIfNotExists( AbstractSchemaDefinition sd )
    {
        schemaDefinition = sd;

        // 書き込み可能なDB接続を一度オープンする。
        // 初回であれば，スキーマが作成され，onCreateが呼ばれる。
        // onCreateの同期的終了を自動的に待つ事になる。
        getWritableDatabase().close();
            // @see http://d.hatena.ne.jp/language_and_engineering/20111202/p1
            // @see http://stackoverflow.com/questions/5024223/sqliteopenhelper-failing-to-call-oncreate
            // NOTE:closeしないとリーク扱いで例外になる
    }


    /**
     * テーブル構築を実行。
     * 親クラスのコンストラクタでデータベースが存在しないときに呼ばれる
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // あらかじめ渡されているスキーマ定義の通りに，テーブル構築を実行する。
        schemaDefinition.createAll( db );

/*
        // このメソッドが同期的に呼ばれていることを確認するために書いたコード
        for( int i = 0 ; i < 10; i ++ ){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/
        FWUtil.d("DBの初期化を実行完了。");

    }


    /**
     * データベースの更新。
     * 親クラスのコンストラクタに渡すversionを変更したときに呼び出される。
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    	// TODO:
        //DBInitializer.exec_update(db, this);
    }


    /**
     * DBを削除
     */
    public void deleteDB()
    {
        FWUtil.d("DBを削除します。");
        new File(DB_FULL_PATH).delete();
        FWUtil.d("DBを削除しました。");
    }


    // G&S


    /**
     * DB名を指定する
     */
    public static void setDB_NAME(String db_name)
    {
        DB_NAME = db_name;
        FWUtil.d("操作対象のDB名は" + DB_NAME);
    }


    /**
     * DBのフルパスを指定する
     */
    public static void setDB_FULLPATH(String dbFullpath)
    {
        DB_FULL_PATH = dbFullpath;
    }

}
