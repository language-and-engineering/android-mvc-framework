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
abstract public class SequentialAsyncTask extends AsyncTask<Void, Void, Void> // TODO:型パラメータ
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
     * 個別タスクの事前処理。
     */
    protected void onPreExecuteHook()
    {
        // Override me
    }


    /**
     * 個別タスクのメイン処理。後続タスクへの継続是非を返す。
     */
    protected abstract boolean main();


    /**
     * ランナーにより，このタスクを開始
     */
    public void kickByRunner( AsyncTasksRunner parent )
    {
        // 呼び出し元のランナーをセット
        this.parent = parent;

        // タスクを開始
        execute();
        FWUtil.d("別スレッドでのタスク実行を開始しました。");
    }


    /**
     *  次のタスクへ継続可能かどうかを判定
     */
    public boolean tasksContinuable()
    {
        return task_execution_result;
    }


    // ------------- メイン処理の実行関係 ------------


    // NOTE: AsyncTaskの隠ぺいが主な仕事

    /**
     * タスク開始前
     */
    @Override
    protected void onPreExecute()
    {
        onPreExecuteHook();
    }


    /**
     * メイン処理
     */
    protected Void doInBackground(Void... unused)
    {
        // メイン処理を実行し，逐次タスクの進行是非を取得
        task_execution_result = main();
        FWUtil.d("別スレッドでのタスク実行を終了しました。実行結果は" + task_execution_result);

        return null;
    }

    /**
     * タスク終了時
     */
    protected void onPostExecute(Void unused)
    {
        // 呼び出し元に通知
        parent.onCurrentTaskFinished();
    }


    // ------------- この非同期タスク内部で保持するデータ ------------


    /**
     * 任意のデータを１つ格納
     */
    protected void storeData( String key, Object val )
    {
        hash.put( key, val );
        FWUtil.d("タスク内で，" + key + "というキーの値をハッシュに登録しました。");
    }


    /**
     * 全データを返す
     */
    public HashMap<String, Object> getStoredObjects()
    {
        return hash;
    }


    // ------------- 別の非同期タスクと共有するデータ ------------


    /**
     * 任意のデータをランナー側から１つ取得
     */
    protected Object getDataFromRunner( String key )
    {
        return parent.getDataByKey( key );
    }


}