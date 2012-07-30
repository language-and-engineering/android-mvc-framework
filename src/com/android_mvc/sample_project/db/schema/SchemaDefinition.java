package com.android_mvc.sample_project.db.schema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android_mvc.framework.db.schema.AbstractSchemaDefinition;
import com.android_mvc.framework.db.schema.RDBColumn;
import com.android_mvc.framework.db.schema.RDBTable;
import com.android_mvc.sample_project.db.entity.Friend;
import com.android_mvc.sample_project.db.entity.LocationLog;

/**
 * AP側のテーブル構造と初期データを定義。
 * @author id:language_and_engineering
 *
 */
public class SchemaDefinition extends AbstractSchemaDefinition
{
    // NOTE:
    // ・各テーブルの主キーは「id」。
    // ・SQLiteのカラムで定義可能な型については，下記を参照
    //     http://www.sqlite.org/datatype3.html
    // ・アプリのインストール（初期化）アクティビティから呼び出される。

    @Override
    public void define_tables()
    {

        // サンプル用の友達テーブル
        new RDBTable(this)
            .nameIs( new Friend().tableName() )
            .columns(new RDBColumn[]{
                new RDBColumn().nameIs("id").primaryKey(),
                new RDBColumn().nameIs("name").comment("名前").typeIs("text").notNull(),
                new RDBColumn().nameIs("age").comment("年齢").typeIs("integer"),
                new RDBColumn().nameIs("favorite_flag").comment("お気に入りフラグ").typeIs("integer")
            })
            .create()
        ;

        // サンプル用のGPS情報格納テーブル
        new RDBTable(this)
            .nameIs( new LocationLog().tableName() )
            .columns(new RDBColumn[]{
                new RDBColumn().nameIs("id").primaryKey(),
                new RDBColumn().nameIs("recorded_at").comment("記録日時").typeIs("text").notNull(),
                new RDBColumn().nameIs("latitude").comment("緯度").typeIs("real").notNull(),
                new RDBColumn().nameIs("longitude").comment("経度").typeIs("real").notNull(),
                new RDBColumn().nameIs("geo_str").comment("地名").typeIs("text").notNull()
            })
            .create()
        ;

    }


    @Override
    public void init_db_data(SQLiteDatabase db, Context context)
    {
        db.execSQL("insert into friends (name, age, favorite_flag) values ('山田太郎', 30, 1);");
    }

}
