package com.android_mvc.framework.db.dao;

import java.util.ArrayList;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.entity.BaseLogicalEntity;

/**
 * DAOの基底クラス。
 * @author id:language_and_engineering
 */
public class BaseDAO<T extends BaseLogicalEntity<T>>
{

    /**
     * レコードを全て新しい順に返す。
     */
    public ArrayList<T> findAll(DBHelper helper, Class<T> entity_class)
    {
        // 有効な湯キーを持つ全件を降順に
        return new Finder<T>(helper)
            .where("id > 0")
            .orderBy("id DESC")
            .findAll(entity_class)
        ;
    }


    /**
     * レコードを主キーから１件取得する。
     */
    public T findById(DBHelper helper, Class<T> entity_class, Long record_id)
    {
        // 特定のIDを持つレコード
        ArrayList<T> result =  new Finder<T>(helper)
            .where("id = " + record_id.toString())
            .findAll(entity_class)
        ;

        if( result.size() > 0 )
        {
            return result.get(0);
        }
        else
        {
            FWUtil.w("検索結果がヒットしませんでした。id = " + record_id.toString());
            return null;
        }
    }
}
