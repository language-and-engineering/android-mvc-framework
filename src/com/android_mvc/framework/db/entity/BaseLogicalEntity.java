package com.android_mvc.framework.db.entity;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 論理エンティティの基底クラス。派生クラスは，Java内での処理のために使用。
 * @author id:language_and_engineering
 *
 */
public abstract class BaseLogicalEntity<T>
{
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

}
