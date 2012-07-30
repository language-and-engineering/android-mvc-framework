package com.android_mvc.framework.ui;

import com.android_mvc.sample_project.R;
import com.android_mvc.framework.annotations.SuppressDebugLog;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.etc.BaseJSAPI;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;

/**
 * XMLを使わずに，メソッドチェインによって全レイアウトを構築するためのビルダー。
 * @author id:language_and_engineering
 *
 */
@SuppressDebugLog
public class UIBuilder
{

    protected Activity context = null;
        // NOTE: Activityだけど，変数名はcontextにしておいたほうが
        // Activity側のコーディング時に自動補完しやすく読みやすいので定石破り。
        // あくまでも，FWを利用するAP側での生産性をアップさせるために。
        // FWの内部にこだわるせいでAP側が読みづらいコードになったら本末転倒ではないか。


    // 最上位のレイアウト
    MLinearLayout rootLayout;


    // HTMLで画面描画する場合
    WebView wv_for_html = null;


    /**
     * 初期化。
     * このクラスで全レイアウトを描画する場合，レイアウトXMLは作らないでおくこと。rootLayoutが見つからずヌルポになるから。
     */
    public UIBuilder(Activity context)
    {
        this.context = context;

        // ひな型XMLを使い，その中に各Viewを次々に放り込んでゆく。
        context.setContentView(R.layout.fw_template_scroll); // テンプレートとして空っぽのXMLを使う。
        this.rootLayout = (MLinearLayout)context.findViewById(R.id._FWRootLayout);

        FWUtil.d("ビルダーの初期化が完了。");
    }


    /**
     * １つ以上の描画したい要素を追加。
     *
     * @param v 可変個のView
     */
    public UIBuilder add(View...v)
    {
        for( int i = 0; i < v.length; i ++ )
        {
            //FWUtil.d("追加対象のViewは" + v.toString() );
            this.rootLayout.add( v[i] );
        }

        return this;
    }


    /**
     * 画面の描画に使用したいHTMLを指定する。
     * ファイルパスはassets/内から書き始める。
     */
    public UIBuilder renderLocalHTML(String file_path) {

        // http://d.hatena.ne.jp/language_and_engineering/20120710/CreateAndroidAppByHtml5JavaScript

        wv_for_html = new WebView(this.context);
        wv_for_html.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            // http://stackoverflow.com/questions/11281094/programmatically-displaying-a-webview-with-a-textview-below-it

        // WebView内でJavaScriptを有効化
        wv_for_html.getSettings().setJavaScriptEnabled(true);

        // WebView内でズーム機能を有効にする
        wv_for_html.getSettings().setBuiltInZoomControls(true);

        // WebView内に，アプリが保持するHTMLを表示
        wv_for_html.loadUrl("file:///android_asset/www/" + file_path);

        this.add(wv_for_html);

        return this;
    }


    /**
     * HTMLで画面描画する場合，JavaScriptからJavaのオブジェクトを参照可能にする。
     */
    public UIBuilder setJSAPI(String js_obj_name, BaseJSAPI jsapi) {

        // WebView内のJavaScriptから，Javaのオブジェクトを参照可能にする
        wv_for_html.addJavascriptInterface(jsapi, js_obj_name);

        // TODO: デフォルトのバインド名を「droid」にしてよいのでは？

        return this;
    }


    /**
     * 定義し終えたレイアウトを，実際に描画する。
     */
    public UIBuilder display()
    {
        FWUtil.d("描画処理を開始。");

        // 最上位のレイアウトから再帰的に動的登録
        rootLayout.inflateInside();

        FWUtil.d("描画処理を終了。");
        return this;
    }


    // TODO: toXML() がほしいな…。UIがある程度複雑になってきたら，XMLに移行するはずだから。

}
