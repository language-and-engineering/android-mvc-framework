package com.android_mvc.framework.activities;

import com.android_mvc.framework.controller.ActivityParams;


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
     * Baseでスタブを実装し，ユーザの記述量を減らすこと。
     */
    public boolean requireProcBeforeUI();


    /**
     * UI描画前に非同期で行うカスタム処理。
     * Baseでスタブを実装し，ユーザの記述量を減らすこと。
     */
    public void procBeforeUI();


    /**
     * UI部品を定義する。
     */
    void defineContentView();


    /**
     * バリデーションに引き渡すために，画面上の値をまとめて回収する。
     */
    public ActivityParams toParams();
}
