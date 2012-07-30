package com.android_mvc.framework.db.dao;

import java.util.List;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.entity.BaseLogicalEntity;

/**
 * DAOの基底クラス。
 * @author id:language_and_engineering
 */
public class BaseDAO<T extends BaseLogicalEntity<T>>
{

    protected DBHelper helper = null;


    /**
     * レコードを全て新しい順に返す。
     */
    public List<T> findAll(DBHelper helper, Class<T> entity_class)
    {
        // 有効な主キーを持つ全件を降順に
        return new Finder<T>(helper)
            .where("id > 0")
            .orderBy("id DESC")
            .findAll(entity_class)
        ;
    }


    /**
     * idが最新の１件を取得する。
     */
    public T findNewestOne(DBHelper helper, Class<T> entity_class)
    {
        List<T> records =  new Finder<T>(helper)
            .where("id > 0")
            .orderBy("id DESC")
            .offset(1)
            .limit(1)
            .findAll(entity_class)
        ;
        if( records.size() > 0 )
        {
            return records.get(0);
        }
        else
        {
            return null;
        }
    }


    /**
     * レコードを主キーから１件取得する。
     */
    public T findById(DBHelper helper, Class<T> entity_class, Long record_id)
    {
        // 特定のIDを持つレコード
        List<T> result =  new Finder<T>(helper)
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


    // 以下は内部的な事情でもがいた残骸


    /*

TODO: find系のメソッドでgetConcreteGenericType()しても，返却値は下記のようになってしまう。子クラスが取れず。orz

    07-26 15:32:32.515: DEBUG/android-mvc-sample(3191):   エンティティクラスの型：com.android_mvc.framework.db.entity.BaseLogicalEntity
    07-26 15:32:32.525: DEBUG/android-mvc-sample(3191): com.android_mvc.framework.db.dao.Finder#findAll :
    07-26 15:32:32.525: DEBUG/android-mvc-sample(3191):   DB接続をオープンします。
    07-26 15:32:32.525: ERROR/android-mvc-sample(3191): java.lang.InstantiationException: com.android_mvc.framework.db.entity.BaseLogicalEntity

*/

    /**
     * このクラスに渡された型パラメータTの具体的なClass情報を返却する。
     */
/*
    protected Class<T> getConcreteGenericType()
    {
        // 下記は引数なしで呼んでいるのがポイント
        @SuppressWarnings("unchecked")
        Class<T> entity_class = getConcreteGenericTypeByTrickyWay();

        return entity_class;
    }
*/

    /**
     * このクラスに渡された型パラメータTの具体的なClass情報を，裏技的に返却するための仕掛け。
     * 可変長引数で定義してあるのがミソ。
     */
 /*
    private Class<T> getConcreteGenericTypeByTrickyWay(T... dummyParam)
    {
        // NOTE: このメソッドは，引数なしで呼び出す。
        //  呼び出し側では「総称配列は可変引数パラメーターに対して作成されます」の警告が出るので
        //  @SuppressWarnings("unchecked")を付与する。
        //  この対処はFW内のみで済むため，ユーザにアノテーションを書かせなくて済む。

        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) dummyParam.getClass().getComponentType();
        FWUtil.d("エンティティクラスの型：" + type.getName());

        return type;

        // Credit:
        // This method was realized by the courtesy of 「Java変態文法最速マスター」
        //   http://d.hatena.ne.jp/Nagise/20100202/1265131791
        //   http://d.hatena.ne.jp/language_and_engineering/20120502/p1
    }

*/

}
