package com.android_mvc.framework.activities.installation;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.common.AbstractAppSettings;
import com.android_mvc.framework.db.dao.FWPrefDAO;
import com.android_mvc.framework.db.dao.IFWDAO;
import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.task.AsyncTasksRunner;
import com.android_mvc.framework.task.SequentialAsyncTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

/**
 * アプリ初期化アクティビティの基底クラス
 * @author id:language_and_engineering
 *
 */
public abstract class InstallAppFWBaseActivity extends BaseNormalActivity
{

    /**
     * 端末内に格納したFWのメタ情報にアクセスするオブジェクト
     */
    private IFWDAO fwDAO = new FWPrefDAO();


    /**
     * アプリを動かすためのクラス関係などをFW側で初期化。
     * 簡易DIみたいなもの。アプリ側からFW側に情報を注入する。
     */
    protected abstract void injectAppInfoIntoFW();


    /**
     * プリファレンスを初期構築
     */
    protected abstract void init_db_preferences();


    /**
     * DB構造を初期構築
     */
    protected abstract void init_db_schema();


    /**
     * DB内のデータを初期構築
     * @param db
     */
    protected abstract void init_db_data(SQLiteDatabase db);


    /**
     * その他の初期化処理
     */
    protected abstract void init_etc();


    /**
     * アプリ初期化成功時のコールバック
     */
    protected abstract void onInstallCompleted();


    /**
     * アプリ初期化スキップ時のコールバック
     */
    protected abstract void onInstallSkipped();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // NOTE:基底クラスのonCreate処理が呼ばれるよりも前に，
        // ロガーのタグ等を初期化しておく必要がある。
        injectAppInfoIntoFW();

        // NOTE: FWの初期化判定に入る前に，必要なら
        // デバッグ用にプリファレンスをクリアしておく必要がある。
        // FWのメタ情報はプリファレンスに入ってるから。
        clear_prefs_if_required();


        // 必要ならデバッグ用にDB削除
        clear_rdb_if_required();


        super.onCreate(savedInstanceState);


        // TODO: アップグレード処理


        // 初期化
        if( requires_installation( this ) )
        {
            // デフォルトの初期化プロセスを実行
            executeDefaultInstallationProcessAsync();
        }
        else
        {
            onInstallSkipped();
        }
    }


    /**
     * 必要ならデバッグ用にプリファレンスをクリアする。
     * アプリインストール時のプリファレンス初期化処理を強制的に行わせるため。
     */
    private void clear_prefs_if_required()
    {
        if( FWUtil.mustClearPrefsForDebug() )
        {
            FWUtil.d("プリファレンスのクリアの必要があると判断");
            fwDAO.deleteAll(this);
        }
        else
        {
            FWUtil.d("プリファレンスのクリアの必要なし");
        }
    }


    /**
     * 必要ならデバッグ用にRDBをクリアする。
     * テーブルもデータも削除される。
     */
    private void clear_rdb_if_required()
    {
        if( FWUtil.mustClearPrefsForDebug() )
        {
            FWUtil.d("RDBのクリアの必要があると判断");
            new DBHelper(this).deleteDB();
        }
        else
        {
            FWUtil.d("RDBのクリアの必要なし");
        }
    }


    /**
     * 初期化処理が必要かどうかを判定
     */
    private boolean requires_installation(Context context)
    {
        // DIARY: ここの!の付け忘れで悩んだ。requireとcompletedではフラグの意味が正反対。
        return ! fwDAO.getFWInstallCompletedFlag(context);
    }


    /**
     * デフォルトのアプリ初期化プロセス。
     */
    private void executeDefaultInstallationProcessAsync()
    {
        FWUtil.d("アプリ初期化プロセスを開始");
        final Context context = this;

        new AsyncTasksRunner( new SequentialAsyncTask[]{

            new SequentialAsyncTask(){

                @Override
                protected boolean main() {

                    // プリファレンスを初期化
                    init_db_preferences();

                    // DBを初期化 // TODO:DAOに抽出？
                    init_db_schema();
                    SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
                    init_db_data( db );
                    db.close();

                    // その他の初期化
                    init_etc();

                    // 完了した旨を記録
                    fwDAO.updateFWInstallCompletedFlag( context, true );
                    FWUtil.d("アプリ初期化プロセスを終了");

                    // ランナーの親に制御を戻す
                    onInstallCompleted();
                    return CONTINUE_TASKS;
                }
            }

        }).withSimpleDialog("アプリを初期化中・・・", this).begin();
    }


    /**
     * アプリ起動時に，AP側の設定情報を受け取り，FW側に注入・初期化する。
     */
    protected void receiveAppInfoAsFW( AbstractAppSettings settings )
    {
        // NOTE: FW側に存在する基底クラスIAppSettingsとして受理しているため，
        // 設定クラスでstaticメンバを使用できない点に注意。
        // もしstaticにすると，AP側でなくFW側のダミー値が返ってきてしまう。

        // ログのタグを初期化
        FWUtil.initAppTag( settings.getAppTagForLog() );

        // 開発モードのセット
        FWUtil.initDebuggingMode( settings.isDebuggingFlag() );

        // 開発時プリファレンスクリア設定のセット
        FWUtil.setForgetPrefOnDebug( settings.isForgetPrefsOnDebug() );

        // 開発時RDBクリア設定のセット
        FWUtil.setForgetRdbOnDebug( settings.isForgetRdbOnDebug() );

        // RDBの名称を登録
        DBHelper.setDB_NAME( settings.getDbName() );

        // RDBのファイルパスを登録
        DBHelper.setDB_FULLPATH( settings.getDbFullpath() );

        // TODO:他にAPから渡されるFW側の初期化処理


    }

}