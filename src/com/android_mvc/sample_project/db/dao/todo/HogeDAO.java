package com.android_mvc.sample_project.db.dao.todo;

import com.android_mvc.framework.db.dao.BaseDBDAO;
import com.android_mvc.framework.db.transaction.todo.TxnListener;
import com.android_mvc.framework.db.transaction.todo.TxnScope;

import android.database.sqlite.SQLiteDatabase;

/**
 * トランザクション管理のサンプルとして作りかけになっているDAO。
 * @author id:language_and_engineering
 *
 */
public class HogeDAO extends BaseDBDAO
{

    private SQLiteDatabase db;


    /**
     * DB更新のサンプル関数1
     */
    private void insert1( TxnScope upper_scope )
    {
        new TxnScope( db, upper_scope ){

            @Override
            protected void handleDB()
            {
                // ～

                insert2( getScope() );
            }

        }.execute( new TxnListener(){
            @Override
            public void onComplete()
            {
                //
            }

            @Override
            public void onError( Exception e )
            {
                //
            }
        });
    }


    /**
     * DB更新のサンプル関数2
     */
    private void insert2( TxnScope upper_scope )
    {
        new TxnScope( db, upper_scope ){

            @Override
            protected void handleDB()
            {
                // ～

                //insertX( getScope() );
            }

        }.execute();
    }


    /**
     * メイン関数
     */
    public void main(){
        new TxnScope( db, null ){

            @Override
            protected void handleDB() {
                insert1( getScope() );

            }
        }.execute();
    }

}