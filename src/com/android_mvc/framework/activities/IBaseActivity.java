package com.android_mvc.framework.activities;

import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.menu.OptionMenuBuilder;


/**
 * カスタムなアクティビティに個別に持ってほしい独自メソッドの宣言。
 * 各ActivityはMapActivityかActivityのいずれかを継承する必要があるので，
 * このオブジェクトはインターフェースにしかなれず，抽象クラスにする事はできない。
 * @author id:language_and_engineering
 *
 */
public interface IBaseActivity
{
    /**
     * UI描画前に何かカスタム処理が必要かどうかを返す。
     */
    public boolean requireProcBeforeUI();


    /**
     * UI描画前に非同期で行うカスタム処理。
     * requireProcBeforeUI() がtrueを返す場合のみ実行される。
     * メソッドの性質上，UIに関連した処理を記述すべきではない。
     * また，非同期実行されるメソッドなので，UIスレッドとは別のスレッド上で実行される点に注意のこと。
     */
    public void procAsyncBeforeUI();


    /**
     * UI部品を定義する。
     * タブ画面の場合は，タブ定義を記述する。
     */
    public void defineContentView();


    /**
     * オプションメニューを定義する。
     */
    public OptionMenuBuilder defineMenu();


    /**
     * UI構築処理が済んだ後に実行する処理。
     */
    public void afterViewDefined();


    /**
     * バリデーションに引き渡すために，画面上の値をまとめて回収する。
     */
    public ActivityParams toParams();


    /**
     * BL実行完了後に呼ばれる処理。
     * BL実行結果を参照できる。
     */
    public void afterBLExecuted( ActionResult ares );
}
