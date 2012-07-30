package com.android_mvc.framework.db.transaction.todo;

/**
 * トランザクションの完了を監視するリスナ。
 * @author id:language_and_engineering
 *
 */
public abstract class TxnListener
{
    public abstract void onComplete();

    // TODO: 共通エラー処理を隠ぺいし，個別エラー処理の記述は任意でOverrideさせる？
    public abstract void onError( Exception e );
}