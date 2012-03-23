package com.android_mvc.framework.db.schema;

import android.database.sqlite.SQLiteDatabase;

import com.android_mvc.framework.common.FWUtil;

/**
 * SQLiteの１つのテーブル定義を表すクラス。
 * @author id:language_and_engineering
 *
 */
public class RDBTable
{
    // NOTE: Railsのマイグレーションをお手本に。
    // Java上でDDLに型安全性を持ち込むと同時に，テーブル構築のDSLを提供。


    // NOTE: setterをDSLとしてユーザに利用させるためにはprivate宣言する必要がある。
    // 補完機能でメンバを出させないために。
    private String table_name;
    private RDBColumn[] columns;
    private SQLiteDatabase db;


    /**
     * コンストラクタ。引数によってdbへのポインタを確保しておく。
     */
    public RDBTable( AbstractSchemaDefinition schemaDefinition )
    {
        this.db = schemaDefinition.getDB();
    }


    /**
     * テーブル名をセット
     */
    public RDBTable nameIs(String table_name)
    {
        this.table_name = table_name;
        return this;
    }


    /**
     * 全カラムをセット
     */
    public RDBTable columns(RDBColumn[] columns)
    {
        this.columns = columns;
        return this;
    }


    /**
     * このテーブルを作成
     */
    public RDBTable create()
    {
        FWUtil.d("テーブル「" + table_name + "」の作成を開始。");

        // DDLを作成
        String ddl = this.toSQLString();

        // 構築実行
        db.execSQL( ddl );

        FWUtil.d("テーブル「" + table_name + "」の作成が完了。DDL文:\n" + ddl);
        return this;
    }


    /**
     * このテーブルのDDLを構築する。
     */
    private String toSQLString()
    {
        String sql = "";
        sql += "CREATE TABLE " + table_name + " (\n";

        // 全カラムについて
        for( int i = 0; i < columns.length; i ++ )
        {
            // DIARY: 拡張for文を書かないと多少の罪悪感に襲われるのだが，
            // 末尾判定が必要なのでしょうがない。一番シンプル。
            // ていうかいつも思うのだが，他言語のようにjoin("\n,")ぐらいできてほしい。
            // Javaよ，Ruby化してくれ。静的でクローズドクラスなかわりに，APIの充実だけでいいから。
            // 生産性や可読性が悪すぎる。


            // １カラム分の定義を付与
            RDBColumn column = columns[ i ];
            sql += "  " + column.toSQLString();

            // 末尾でない？
            if( i != columns.length - 1 )
            {
                sql += ",";
            }

            sql += "\n";
        }

        sql += ");";

        return sql;
    }

}
