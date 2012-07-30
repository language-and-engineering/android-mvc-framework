package com.android_mvc.framework.task;

import java.util.HashMap;

/**
 * 逐次実行可能なタスク類が実装すべきインタフェース。
 * @author id:language_and_engineering
 *
 */
public interface ISequentialRunnable
{

    // NOTE: 本当は宣言だけでなくコード共有したいのだが，
    // Javaでは多重継承できないのでインタフェースを作って具象クラス側でソースコードをコピペせざるを得ないばかりか，
    // それらのメソッドがpublicになってしまうという悲しさ（あちこちで味わっている）


    /**
     * ランナーにより，このタスクを開始
     */
    public void kickByRunner( AsyncTasksRunner parent );


    // ------------- ランナーとの連携 ------------


    /**
     *  次のタスクへ継続可能かどうかを判定
     */
    public boolean tasksContinuable();


    // ------------- この非同期タスク内部で保持するデータ ------------


    /**
     * 任意のデータを１つ格納
     */
    public void storeData( String key, Object val );


    /**
     * 全データを返す
     */
    public HashMap<String, Object> getStoredObjects();


    // ------------- 別の非同期タスクと共有するデータ ------------


    /**
     * 任意のデータをランナー側から１つ取得
     */
    public Object getDataFromRunner( String key );


}
