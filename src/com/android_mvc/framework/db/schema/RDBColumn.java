package com.android_mvc.framework.db.schema;

/**
 * SQLiteの１つのカラム定義を表すクラス。
 * @author id:language_and_engineering
 *
 */
public class RDBColumn
{
    private String column_name;
    private String type_name;
    private boolean primary_key_flag = false;
    private boolean not_null_flag = false;


    /**
     * カラム名をセット
     */
    public RDBColumn nameIs(String column_name)
    {
        this.column_name = column_name;
        return this;
    }


    /**
     * カラムの型名をセット
     */
    public RDBColumn typeIs(String type_name)
    {
        // TODO: type名をtypoする場合もあるだろう。型安全にするか，もしくはバリデーションするか。
        this.type_name = type_name;
        return this;
    }


    /**
     * 連番の主キーとする。※not null指定は不要。
     */
    public RDBColumn primaryKey()
    {
        this.primary_key_flag = true;
        this.type_name = "integer";
        return this;
    }


    /**
     * not nullとする。
     */
    public RDBColumn notNull()
    {
        this.not_null_flag = true;
        return this;
    }


    /**
     * １カラム分をSQL化。正確に言うと，DDLの一部分。
     */
    protected String toSQLString()
    {
        // SAMPLE:
        //  "id integer primary key autoincrement"
        //  "hoge text not null"

        String sql = "";
        sql += column_name + " " + type_name;

        // 主キーの場合
        if( primary_key_flag )
        {
            sql += " primary key autoincrement";
                // http://www.dbonline.jp/sqlite/table/index6.html
        }
        else
        {
            if( not_null_flag )
            {
                sql += " not null";
            }
        }

        return sql;
    }

}
