package com.android_mvc.framework.db.entity;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.controller.routing.IntentPortable;
import com.android_mvc.framework.db.DBHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 論理エンティティの基底クラス。派生クラスは，Java内での処理のために使用。
 * Intent経由で運搬可能。
 * @author id:language_and_engineering
 *
 */
public abstract class BaseLogicalEntity<T> implements IntentPortable
{

    /**
     * Serializableなので。
     */
    private static final long serialVersionUID = 1L;


    /**
     * 主キー「id」を返す。
     */
    public abstract Long getId();


    /**
     * 自身がDBに未登録の新規レコードであるかどうかを返す。
     */
    private boolean is_new_record()
    {
        return (getId() == null);
    }


    // ----- LP変換（Logical ⇔ Physical） -----


    /**
     * DB内の格納値（物理値）から，論理エンティティを構築する。
     * 物理値から論理値へ変換し，論理エンティティの各フィールドにセットする。
     * @param c カーソル
     */
    public abstract T logicalFromPhysical(Cursor c);


    /**
     * 自身を，DBに新規登録可能なデータ型に変換して返す。
     * 論理値から物理値へ変換し，ContentValuesの各キーにセットする。
     */
    protected abstract ContentValues toPhysicalEntity( ContentValues values );
        // NOTE: 本当はここで「PhysicalEntity」を返却するようにしたかった。
        // しかしContentValuesはfinalなので，継承してPhysicalEntityクラスを作れぬ。
        // ラッパーメソッドも多すぎるので，コンポジションも無理。
        // finalじゃなければいいのに・・・。


    /**
     * FW内で，DB登録用の「物理エンティティ」を取得するためのメソッド。
     */
    public ContentValues toContentValues()
    {
        ContentValues values = new ContentValues();
        return toPhysicalEntity( values );
    }


    // ----- CRUD -----


    /**
     * テーブル名。
     */
    public abstract String tableName();


    /**
     * カラム名たち。
     */
    public abstract String[] columns();


    // C，U:


    /**
     * 自身を保存。
     * 主キーがなければ，新規保存になる。主キーがあれば，更新になる。
     */
    public boolean save(DBHelper helper)
    {
        boolean ret = false;

        FWUtil.d("DB接続をオープンします。");
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            // 物理エンティティに変換
            ContentValues values = this.toContentValues();
            FWUtil.d("物理エンティティへの変換が成功しました。");

            if( this.is_new_record() )
            {
                db.insert(this.tableName(), null, values);
                FWUtil.d("DB新規登録に成功しました。");
            }
            else
            {
                // 主キーをもとに１件だけ更新
                db.update( this.tableName(), values, "id = " + this.getId(), null );
                FWUtil.d("DB更新に成功しました。");
            }

            ret = true;

        } finally {
            db.close();
        }

        return ret;
    }


    // R:
    // Finderを使って検索してもらう


    // D:


    /**
     * 自身を削除。
     */
    public void delete(DBHelper helper)
    {
        FWUtil.d("DB接続をオープンします。");
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            db.delete( this.tableName(), "id = " + getId().toString(), null );
            FWUtil.d("DBからの削除に成功しました。");
        } finally {
            db.close();
        }
    }

}
