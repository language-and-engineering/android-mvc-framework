package com.android_mvc.framework.db.schema;

import com.android_mvc.framework.common.FWUtil;

import android.database.sqlite.SQLiteDatabase;

/**
 * AP側のスキーマ定義を表現するための抽象クラス。
 * @author id:language_and_engineering
 *
 */
public abstract class AbstractSchemaDefinition
{

    private SQLiteDatabase db;


    /**
     * AP側でテーブル構造を定義する。
     */
    protected abstract void define_tables();


    /**
     * FW側で全テーブルの作成処理を実行する。
     */
    public void createAll(SQLiteDatabase db)
    {
        this.db = db;

        // TODO:本当はトランザクション内で処理したいのだが，
        // SQLiteではテーブルレベルのDDLをトランザクションで完全に処理することはできない。
        // http://old.nabble.com/DDL-statements-in-transactions-td17785928.html

        //TxnScope upper_scope = null;
        //new TxnScope( db, upper_scope ){
        //    @Override
        //    protected void handleDB()
        //    {

        FWUtil.d("テーブル構造の初期化を開始");
        define_tables();
        FWUtil.d("テーブル構造の初期化を完了");

        //    }
        //}.execute();
    }


    /**
     * DBを取得。
     */
    public SQLiteDatabase getDB()
    {
        return db;
    }

}
