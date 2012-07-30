package com.android_mvc.framework.controller.routing;

import java.util.HashMap;

import com.android_mvc.framework.common.FWUtil;

import android.app.Activity;

/**
 * あるアクティビティからの遷移先情報を，集約して保持する。
 * 文字列識別子と遷移先アクティビティクラスを対応付けるために使用する。
 * @author id:language_and_engineering
 *
 */
public class RoutingTable
{
    // ハッシュとして保持
    private HashMap<String, Class<? extends Activity>> storedTable = new HashMap<String, Class<? extends Activity>>();


    /**
     * マッピング情報を一つ登録。
     * 遷移先Activityとして定数STAY_THIS_PAGEを渡せば，画面遷移を抑止できる。
     */
    public RoutingTable map(String route_id, Class<? extends Activity> destActivityClass)
    {
        storedTable.put( route_id, destActivityClass );
        return this;
    }


    /**
     * マッピング情報を一つ登録。
     * 遷移先Activityとして定数STAY_THIS_PAGEを渡せば，画面遷移を抑止できる。
     * 末尾の引数は単なるコメント。
     */
    public RoutingTable map(String route_id, Class<? extends Activity> destActivityClass, String comment)
    {
        this.map(route_id, destActivityClass);
        return this;
    }


    /**
     * ルーティング識別子をもとに，遷移先のアクティビティを返す。
     * 識別子が未登録の場合はnullを返す。
     */
    public Class<? extends Activity> getActivityByRouteId(String routeId)
    {
        Class<? extends Activity> destActivityClass = storedTable.get(routeId);
        FWUtil.d("routeId = " + routeId + "に対応する遷移先：" + destActivityClass);
        return destActivityClass;
    }


    //DIARY: このクラスを作らないでいた理由は，HashMapのラッパーの作りすぎで力尽きたからだった。
    //  http://d.hatena.ne.jp/language_and_engineering/20120213/p1
    // しかしこれを作っておけば，@SuppressWarnings("serial")しなくて済む。
    //  http://code.google.com/p/android-mvc-framework/source/browse/tags/20120215_ver0.1/src/com/android_mvc/sample_project/controller/Controller.java


    //DIARY: もともと，Javaの全てのハッシュ系APIは，こういう記法が可能であるべきだ。と思うんだよね。
    // RubyやJavaScriptなどの言語に比べ，Javaではハッシュの初期化があまりにも面倒orトリッキーすぎるため。
    // こういう各種ラッパーがあれば一発なので，ジェネリクスを使ってChainableHashみたいな基底クラスにしたいのだが，
    // 作ってもコード量はむしろ増えるだけなのでやめておく。
    // その基底クラスの実際の利用シーンを考えると，子側のハッシュクラスにフィットしたプロキシメソッド名が必要になり，
    // クラスの型パラメータを渡すだけではDSLとして不足があるから。

}
