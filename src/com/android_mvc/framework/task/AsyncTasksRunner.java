package com.android_mvc.framework.task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.ProgressDialog;
import android.content.Context;

import com.android_mvc.framework.annotations.SuppressDebugLog;
import com.android_mvc.framework.common.FWUtil;


/**
 * 複数の非同期タスクを逐次実行するためのコンテナ。
 *
 * 利用できるのはUIスレッド上のみなので注意。本クラスを入れ子で利用することはできない。
 * もしUIスレッド上で別のAsyncTasksRunnerを呼び出したい場合は，
 * whenAllTasksCompleted() メソッド内のコールバック処理として記述すること。
 * @author id:language_and_engineering
 */
@SuppressDebugLog
public class AsyncTasksRunner
{
    // 実行したい非同期タスク達
    private SequentialAsyncTask[] tasks;

    // 現在取り扱い中のタスクのインデックス
    private int executing_task_cursor = 0;

    // 全タスクから返されるデータのストア
    private HashMap<String, Object> data_from_all_tasks = new HashMap<String, Object>();


    // ダイアログ表示フラグ
    private boolean requireDialogFlag = false;

    // ダイアログ上に表示する文言
    private String dialogMessage = null;

    // ダイアログ
    private ProgressDialog dialog = null;

    // ダイアログ用のコンテキスト
    private Context context;


    // 全タスク完了時のコールバック
    private RunnerFollower follower;


    /**
     * 初期化
     * @param tasks 逐次実行したい非同期タスクの配列
     */
    public AsyncTasksRunner( SequentialAsyncTask[] tasks )
    {
        this.tasks = tasks;

        // TODO: 初期化の時点でContextを控えておいたほうがいいかもしれない。
    }


    // ---------------- タスクの実行 -----------------


    /**
     * 全タスクを実行開始
     */
    public void begin()
    {
        FWUtil.d("begin()が呼ばれました。");

        if( tasks.length > 0 )
        {
            FWUtil.d("最初のタスクの実行を開始します。");

            if( requireDialogFlag )
            {
                // ダイアログ表示
                dialog = new ProgressDialog( context );
                dialog.setMessage( dialogMessage );
                dialog.show();
            }
            executeCurrentTask();
        }

        FWUtil.d("begin()を終了します。");
    }


    /**
     * 現在のタスクを実行
     */
    private void executeCurrentTask()
    {
        FWUtil.d("現在のタスクを実行します。インデックスは" + executing_task_cursor);
        getCurrentTask().kickByRunner( this );
    }


    /**
     * 次のタスクを実行
     */
    private void executeNextTask()
    {
        executing_task_cursor ++;
        executeCurrentTask();
    }


    // ---------------- タスクの完了 -----------------


    /**
     * 現在のタスクの実行完了時に，タスク側から呼ばれる
     */
    public void onCurrentTaskFinished()
    {
        // 現行タスクの終了処理
        mergeDataFromTask( getCurrentTask() );

        if( mustMoveToNextTask() )
        {
            FWUtil.d("次のタスクへ移動する事を決定");

            // 次のタスクへ移動
            executeNextTask();
        }
        else
        {
            FWUtil.d("次のタスクへ移動しない事を決定");
            onAllTasksFinished();
        }
    }


    /**
     * 全タスクの実行が完了した際の内部処理。
     */
    private void onAllTasksFinished()
    {
        // ダイアログの後始末
        if( requireDialogFlag && ( dialog != null ))
        {
            // ダイアログを消す
            dialog.dismiss();
            FWUtil.d("ダイアログを消しました。");
        }

        // ユーザ定義コールバック
        if( this.follower != null)
        {
            // NOTE: これがメイン（UI）スレッドで実行されるところがミソ。
            // 再度AsyncTaskの生成に取り掛かれるから。
            FWUtil.d("ユーザ定義のコールバックを実行します。");
            follower.exec();
            FWUtil.d("ユーザ定義のコールバックを実行完了。");
        }

        FWUtil.d("全非同期タスクの逐次実行が完了。");
    }


    /**
     *  全タスク終了時のユーザ側コールバックを定義
     */
    public AsyncTasksRunner whenAllTasksCompleted(RunnerFollower follower)
    {
        this.follower = follower;
        return this;
    }


    // ---------------- 個別のタスクの管理 -----------------


    /**
     * 現在取り組み中のタスクを返す
     */
    private SequentialAsyncTask getCurrentTask()
    {
        return tasks[ executing_task_cursor ];
    }


    /**
     * 現在処理中のタスクが最後のタスクかどうか返す
     */
    private boolean isProccessingLastTask()
    {
        return ( tasks.length == ( executing_task_cursor + 1 ) );
    }


    /**
     * タスクを継続可能かどうか返す
     */
    private boolean mustMoveToNextTask()
    {
        // 最後のタスクでなく，現行タスクの結果がOKであれば。
        return (
            ( ! isProccessingLastTask() ) &&
            ( getCurrentTask().tasksContinuable() )
        );
    }


    /**
     * １タスクの保持データを回収
     * @param task 実行し終わった非同期タスク
     */
    private void mergeDataFromTask( SequentialAsyncTask task )
    {
        // HashMapを取り出して，そのループの準備をする
        HashMap<String, Object> data_from_current_task = task.getStoredObjects();
        Set<String> keySet = data_from_current_task.keySet();
        Iterator<String> keyIterator = keySet.iterator();

        // 全キーについて，個別タスクからランナー側に値を取り込む
        while( keyIterator.hasNext() )
        {
            // １ペアを読み込み
            String key = (String)keyIterator.next();
            Object value = data_from_current_task.get( key );

            // １ペアを上書き
            data_from_all_tasks.put( key, value );
            FWUtil.d("ランナー側で，" + key + "の値を上書きしました。");
        }
    }


    /**
     * これまでに実行した全タスクから回収した値をキーごとに返す
     */
    public Object getDataByKey( String key )
    {
        return data_from_all_tasks.get( key );
    }


    // -------------- ダイアログ --------------


    /**
     * 全ての非同期処理が終わるまで，プログレスダイアログを表示する
     * @param string ダイアログ上の表示文字列
     */
    public AsyncTasksRunner withSimpleDialog(String s, Context context )
    {
        this.context = context;
        setRequireDialogFlag(true);
        setDialogMessage(s);

        return this;
    }


    private void setDialogMessage(String s) {
        dialogMessage = s;
    }


    private void setRequireDialogFlag(boolean b) {
        requireDialogFlag = b;
    }

}
