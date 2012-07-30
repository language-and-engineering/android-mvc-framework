package com.android_mvc.framework.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.entity.BaseLogicalEntity;

/**
 * SELECT時の検索条件を保持し，検索を実行するクラス。
 * @author id:language_and_engineering
 *
 */
public class Finder<T extends BaseLogicalEntity<T>>
{
    // NOTE: 本当は，Rails3のArelを模倣できたら最高なんだが。。。。


    private DBHelper helper;

    // SELECT時の検索条件
    private String selection = "";
    private String orderDescription;
    private int offsetNum = -1;
    private int limitNum = -1;


    public Finder(DBHelper helper) {
        this.helper = helper;
    }


    /**
     * WHERE句をセット
     */
    public Finder<T> where( String selection )
    {
        this.selection = selection;
        return this;
    }


    /**
     * ORDER BY句をセット
     */
    public Finder<T> orderBy(String orderDescription)
    {
        this.orderDescription = orderDescription;
        return this;
    }

    /**
     * OFFSET句をセット
     */
    public Finder<T> offset(int offsetNum)
    {
        this.offsetNum = offsetNum;
        return this;
    }


    /**
     * LIMIT句をセット
     */
    public Finder<T> limit(int limitNum)
    {
        this.limitNum = limitNum;
        return this;
    }


    /**
     * セット済みの検索条件で，全件検索を実行する。
     */
    public List<T> findAll( Class<T> entity_class )
    {
        // NOTE: なぜ，Tを受け取っておきながら，こんな引数が必要になるのか？
        // それは，Javaではジェネリクスの型パラメータでnewできないから。
        // なんとも冗長。美しくない。でもユーザ側の記述量の削減にはなるので，甘受すべし…。
        // http://www.ne.jp/asahi/hishidama/home/tech/java/generics.html#h3_new
        // http://blog.isocchi.com/2009/02/new.html


        // LIMIT句を構築
        String limitClause = null;
        if( offsetNum > 0 )
        {
            limitClause = "" + offsetNum;
        }
        if( limitNum > 0 )
        {
            if( limitClause != null )
            {
                limitClause += ",";
            }
            else
            {
                limitClause = "";
            }
            limitClause += "" + limitNum;
        }


        FWUtil.d("DB接続をオープンします。");
        SQLiteDatabase db = helper.getReadableDatabase();
        List<T> result_list = new ArrayList<T>();

        try{
            // NOTE: インスタンスメソッドを呼びたいだけのためのオブジェクト。
            // Class<T>は不可視だし，Tはnewできんし，裏技的にインスタンス生成。
            // table名取得プロセスが，staticだとうまく共有できないのがつらいところ。
            T dummy_entity = entity_class.newInstance();

            FWUtil.d("クエリを発行します。");
            Cursor c = db.query(
                dummy_entity.tableName(),
                dummy_entity.columns(),
                selection,
                null, null, null,
                orderDescription,
                limitClause
            );
                // http://d.hatena.ne.jp/shimooka/20110428/1303972105

            FWUtil.d("全件の結果を読み取ります。");
            while( c.moveToNext() )
            {
                // カーソル（物理値）から，論理エンティティを構築する
                T entity = entity_class.newInstance().logicalFromPhysical(c);

                // 結果に格納
                result_list.add(entity);
            }

            FWUtil.d("クエリへの参照を破棄します。");
            c.close();

        } catch (InstantiationException e) {
            FWUtil.e( e.toString() );
        } catch (IllegalAccessException e) {
            FWUtil.e( e.toString() );
        } finally {
            db.close();
        }
        FWUtil.d("全件検索結果の件数：" + result_list.size() );

        return result_list;
    }


}
