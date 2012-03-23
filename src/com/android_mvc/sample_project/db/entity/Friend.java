package com.android_mvc.sample_project.db.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.android_mvc.sample_project.db.entity.lib.LogicalEntity;

/**
 * 友達１人を表す論理エンティティ。
 * @author id:language_and_engineering
 *
 */
public class Friend extends LogicalEntity<Friend>
{
    // Intent経由でエンティティを運搬可能にするために
    private static final long serialVersionUID = 1L;


    // テーブル名
    public String tableName(){return "friends";}

    // カラム
    public final String[] columns(){
        return new String[]{
            "id", "name", "age", "favorite_flag"
        };
    }


    // メンバ
    private Long id = null;
    private String name = null;
    private Integer age = null;
    private Boolean favorite_flag = null;


    // IDEが自動生成したG&S

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getFavorite_flag() {
        return favorite_flag;
    }

    public void setFavorite_flag(Boolean favoriteFlag) {
        favorite_flag = favoriteFlag;
    }


    // その他


    /**
     * 友達に自分の説明をさせる
     */
    public String getDescription()
    {
        String friend_description = "id:" // ※forループ中で呼ばれるので，本当はStringBuilderを（略
            + getId()
            +","
            + getName()
            + "さん("
            + getAge()
            +")"
        ;

        // お気に入りなら星マークをつける
        if( getFavorite_flag() )
        {
            friend_description += "★";
        }

        return friend_description;
    }


    // ----- LP変換(Logical <-> Physical) -----


    /**
     * DBの格納値からエンティティを構成
     */
    @Override
    public Friend logicalFromPhysical(Cursor c)
    {
        setId(c.getLong(0));
        setName(c.getString(1));
        setAge( c.getInt(2) );
        setFavorite_flag( c.getInt(3) == 1 );

        return this;
    }


    /**
     * 自身をDBに新規登録可能なデータ型に変換して返す
     */
    @Override
    protected ContentValues toPhysicalEntity(ContentValues values)
    {
        // entityをContentValueに変換

        if( getId() != null)
        {
            values.put("id", getId());
        }
        values.put("name", getName());
        values.put("age", getAge());
        values.put("favorite_flag", getFavorite_flag() ? 1 : 0);

        return values;
    }


}
