package com.android_mvc.sample_project.db.dao;

import java.util.ArrayList;

import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.Friend;
import com.android_mvc.framework.db.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 友達を読み書きするクラス。
 * @author id:language_and_engineering
 */
public class FriendDAO {

    // 全カラム名
    private static final String[] COLUMNS = {
        "id", "name", "age", "favorite_flag"
    };

    private DBHelper helper = null;
    private String TABLE_NAME = "friends";
    //private Context context;
    SQLiteDatabase db = null;


    public FriendDAO(Context context) {
        helper = new DBHelper(context);
        //this.context = context;
    }


    // ------------ C --------------


    /**
     * 1人の友達を保存。
     */
    public Friend create(String name, Integer age, Boolean favoriteFlag)
    {
        // 論理エンティティを構築
        Friend f = new Friend();
        f.setName(name);
        f.setAge( age );
        f.setFavorite_flag( favoriteFlag );

        // DB登録
        db = helper.getWritableDatabase();
        try{
            // 物理エンティティに変換
            ContentValues values = f.toContentValues();

            db.insert(TABLE_NAME, null, values);
            Util.d("友達を登録完了。ID：" + f.getId() + ", 名前：" + f.getName() );

        } finally {
            db.close();
        }

        return f;
    }


    // ------------ R --------------


    /**
     * 友達を全て新しい順に返す。
     */
    public ArrayList<Friend> findAll()
    {
        ArrayList<Friend> friends = new ArrayList<Friend>();

        // WHERE句。ここでは，全件の意。
        String selection = "id > 0 ";

        // 読み込み
        db = helper.getReadableDatabase();
        try{
            Cursor c = db.query(
                TABLE_NAME, COLUMNS, selection,
                null, null, null, "id DESC"
            );
            while( c.moveToNext() )
            {
                // カーソル（物理値）から，論理エンティティを構築する
                Friend f = new Friend().logicalFromPhysical(c);

                friends.add( f );
            }
            c.close();
        } finally {
            db.close();
        }
        Util.d("友達の全件検索結果の件数：" + friends.size() );

        return friends;
    }


    // ------------ U --------------


    // ------------ D --------------


}
