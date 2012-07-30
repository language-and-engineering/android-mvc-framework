package com.android_mvc.framework.task;

import java.util.HashMap;

import com.android_mvc.framework.annotations.SuppressDebugLog;
import com.android_mvc.framework.common.FWUtil;


import android.os.AsyncTask;


/**
 * 逐次化可能な非同期タスクの基底クラス
 * @author id:language_and_engineering
 *
 */
@SuppressDebugLog(false)
abstract public class SequentialAsyncTask extends AsyncTask<Void, Void, Void> implements ISequentialRunnable
{

    // TODO: APIレベル13以上（プラットフォームが3.2以上）だと，
    // AsyncTask#executeの挙動が変わり，パラレルではなくシリアル実行になる。
    // その差もいずれ吸収する必要がある。
    // @see http://www.swingingblue.net/mt/archives/003629.html
    // @see http://developer.android.com/intl/ja/guide/appendix/api-levels.html


    // NOTE: AsyncTask自体はjava.util.concurrentのラッパーであり，
    // UIにアクセスするために利用が必須。
    // https://github.com/android/platform_frameworks_base/blob/master/core/java/android/os/AsyncTask.java


    // 呼び出し元のランナー
    protected AsyncTasksRunner parent = null;

    // タスクの実行結果
    private boolean task_execution_result = true;

    // タスク内の保持データ
    private HashMap<String, Object> hash = new HashMap<String, Object>();

    // ランナー側にタスクの継続有無を通知するための定数
    protected boolean CONTINUE_TASKS = true;
    protected boolean BREAK_TASKS = false;


    /**
     * ランナーにより，このタスクを開始
     */
    @Override
    public void kickByRunner( AsyncTasksRunner parent )
    {
        // 呼び出し元のランナーをセット
        this.parent = parent;

        // タスクを開始
        execute();
            // onPreExecute, doInBackground, onPostExecuteの順に呼ばれる
        FWUtil.d("別スレッドでのタスク実行を開始しました。");
    }


    // ------------- メイン処理 ------------


    /**
     * 個別タスクのメイン処理。
     * 内部は単に逐次的に実行されるので，イベントなどの関係しない，いわゆる重い処理を詰め込むのに適している。
     * 後続タスクへの継続是非を返す。
     */
    protected abstract boolean main();


    // NOTE: AsyncTaskの隠ぺいが主な仕事


    /**
     * 個別タスクの事前処理。
     */
    protected void beforeExecution()
    {
        // Override me
    }


    /**
     * タスク開始前
     */
    @Override
    protected void onPreExecute()
    {
        beforeExecution();
    }


    /**
     * メイン処理を実行し，逐次タスクの進行是非を取得
     */
    @Override
    protected Void doInBackground(Void... unused)
    {
        // 重い処理を逐次的に実行
        task_execution_result = main();
        FWUtil.d("別スレッドでのタスク実行を終了しました。実行結果は" + task_execution_result);

        return null;
    }


    @Override
    protected void onPostExecute(Void unused)
    {
        // 呼び出し元に通知
        parent.onCurrentTaskFinished();
    }


    // ------------- 下記はインタフェース宣言で共通のコード ------------


    @Override
    public boolean tasksContinuable()
    {
        return task_execution_result;
    }

    @Override
    public void storeData( String key, Object val )
    {
        hash.put( key, val );
    }

    @Override
    public HashMap<String, Object> getStoredObjects()
    {
        return hash;
    }

    @Override
    public Object getDataFromRunner( String key )
    {
        return parent.getDataByKey( key );
    }


}