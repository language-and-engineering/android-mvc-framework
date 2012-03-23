package com.android_mvc.framework.controller;

import android.app.Activity;

import com.android_mvc.framework.controller.routing.RoutingTable;


/**
 * コントローラの基底クラス。
 * @author id:language_and_engineering
 *
 */
public class BaseController
{

    // NOTE: MVCのコントローラ層に当たるクラス。
    // ビューから渡された値の検証や，BLの呼び出し，画面遷移の制御などを行う。
    // もし肥大化したら，別クラスに細分化する。
    // 着想は下記の記事を参照。
    // @see http://d.hatena.ne.jp/language_and_engineering/20120213/p1

    // NOTE: 各メソッドは，コントローラ自体に状態を生まない。
    // コントロールフロー詳細クラスに状態を委任しているため。
    // なので，各メソッドはstaticでOK。クラス分けも単なる名前空間の分割。

    // IDEA: コントローラ類のソースを静的解析すれば，画面遷移図が自動生成できるぞ…。


    // BL実行完了時に遷移しないことを表すための定数
    protected static final RoutingTable STAY_THIS_PAGE_ALWAYS = null;
    protected static final Class<? extends Activity> STAY_THIS_PAGE = null;

}
