package com.android_mvc.framework.db.transaction.todo;


import android.database.sqlite.SQLiteDatabase;

/**
 * SQLiteのバージョンによらず，トランザクションの入れ子をサポートするためのクラス。TODO段階。
 * @author id:language_and_engineering
 *
 */
public abstract class TxnScope
{
    private boolean is_top_level = false;


    private SQLiteDatabase db;


    /**
     *  ユーザに記述させるメインのDB処理。トランザクションは意識しないでよい。
     */
    protected abstract void handleDB();


    /**
     * 初期化
     */
    public TxnScope( SQLiteDatabase db, TxnScope upper_scope )
    {
        this.db = db;
        if( upper_scope == null )
        {
            this.is_top_level = true;
        }
    }


    /**
     * １層分のトランザクションを実行
     */
    public void execute( TxnListener listener )
    {
        try
        {
            manipulate_db();
            listener.onComplete();
        }
        catch( Exception e )
        {
            // TODO: 例外ケースが多い場合は，
            // listener側で受け取る例外をジェネリックなパラメータにするように要検討
            listener.onError( e );
        }
    }


    /**
     * コールバック無しで，真偽値のみで成否を返却する
     */
    public boolean execute()
    {
        try
        {
            manipulate_db();
            return true;
        }
        catch( Exception e )
        {
            // TODO:

            return false;
        }
    }


    /**
     * このトランザクション層内でのDB操作処理を実行
     */
    private void manipulate_db() throws Exception
    {
        // NOTE: sqlite3.6.8より前だと，SQL文中でトランザクションの入れ子ができないので
        // スコープがトップレベルの場合のみ明示的にトランザクションを指定する。
        // もしRDBレベルで入れ子が許可されていれば，Commandパターン等で作り替えたいのだが。

        if( is_top_level )
            db.beginTransaction();

        try {
            handleDB();

            if( is_top_level )
                db.setTransactionSuccessful();
        }
        catch( Exception e )
        {
            throw e;
        }
        finally {
            if( is_top_level )
                db.endTransaction();
        }

    }


    /**
     * 自分自身を返す。
     * 利用時は匿名クラスとして具体的に実装することになるので，
     * 自分自身を表すわかりやすいメソッドがあったほうがよい。
     */
    protected TxnScope getScope()
    {
        return this;
    }

}