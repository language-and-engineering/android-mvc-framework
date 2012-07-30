package com.android_mvc.framework.task;

import java.util.HashMap;

import com.android_mvc.framework.annotations.SuppressDebugLog;
import com.android_mvc.framework.common.FWUtil;



/**
 * 逐次化可能なイベントタスクの基底クラス
 * @author id:language_and_engineering
 *
 */
@SuppressDebugLog(false)
abstract public class SequentialEventTask implements ISequentialRunnable
{
    // 呼び出し元のランナー
    protected AsyncTasksRunner parent = null;

    // タスクの実行結果
    private boolean task_execution_result = true;

    // タスク内の保持データ
    private HashMap<String, Object> hash = new HashMap<String, Object>();

    // ランナー側にタスクの継続有無を通知するための定数
    protected boolean CONTINUE_TASKS = true;
    protected boolean BREAK_TASKS = false;



    // ------------- メイン処理 ------------


    @Override
    public void kickByRunner( AsyncTasksRunner parent )
    {
        // 呼び出し元のランナーをセット
        this.parent = parent;

        // タスクを開始
        kickEventAndWait();
        FWUtil.d("別スレッドでのタスク実行を開始しました。");
    }


    /**
     * イベント駆動の非同期タスクなど，待機が必要なタスクを開始する。
     * 例えば，イベントリスナを起動する。
     * イベント完了時に，実装クラス側でランナーに制御を戻すこと。
     */
    protected abstract void kickEventAndWait();


    /**
     * 本イベントタスクを終了する。
     * 後続タスクへの継続是非を渡す。
     */
    public void endEventAndBackToRunner(boolean b)
    {
        task_execution_result = b;

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