package com.android_mvc.sample_project.db.schema;

import com.android_mvc.framework.db.schema.AbstractSchemaDefinition;
import com.android_mvc.framework.db.schema.RDBColumn;
import com.android_mvc.framework.db.schema.RDBTable;

/**
 * AP側のテーブル構造を定義
 * @author id:language_and_engineering
 *
 */
public class SchemaDefinition extends AbstractSchemaDefinition
{
    @Override
    public void define_tables()
    {
        // NOTE: 各テーブルの主キーは「id」。


        // サンプル用の友達テーブル

        new RDBTable(this)
            .nameIs("friends")
            .columns(new RDBColumn[]{
                new RDBColumn().nameIs("id").primaryKey(),
                new RDBColumn().nameIs("name").typeIs("text").notNull(),
                new RDBColumn().nameIs("age").typeIs("integer"),
                new RDBColumn().nameIs("favorite_flag").typeIs("integer")
            })
            .create()
        ;

 /*
        new RDBTable(this)
            .nameIs("table1")
            .columns(new RDBColumn[]{
                new RDBColumn().nameIs("id").primaryKey(),
                new RDBColumn().nameIs("col1").typeIs("text").notNull(),
                new RDBColumn().nameIs("col2").typeIs("integer")
            })
            .create()
        ;

        new RDBTable(this)
            .nameIs("table2")
            .columns(new RDBColumn[]{
                new RDBColumn().nameIs("id").primaryKey(),
                new RDBColumn().nameIs("col1").typeIs("blob")
            })
            .create()
        ;
*/
    }
}
