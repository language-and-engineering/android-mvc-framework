package com.android_mvc.framework.ui;

import com.android_mvc.sample_project.R;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.ui.view.MLinearLayout;

import android.app.Activity;
import android.view.View;

/**
 * XMLを使わずに，メソッドチェインによって全レイアウトを構築するためのビルダー。
 * @author id:language_and_engineering
 *
 */
public class UIBuilder
{

    protected Activity context = null;
        // NOTE: Activityだけど，変数名はcontextにしておいたほうが
        // Activity側のコーディング時に自動補完しやすく読みやすいので定石破り。
        // あくまでも，FWを利用するAP側での生産性をアップさせるために。
        // FWの内部にこだわるせいでAP側が読みづらいコードになったら本末転倒ではないか。


    // 最上位のレイアウト
    MLinearLayout rootLayout;


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
        //FWUtil.d("addによって" + v.length + "個のViewを追加登録開始。現在の個数：" + includingViews.size() );
        for( int i = 0; i < v.length; i ++ )
        {
            //FWUtil.d("追加対象のViewは" + v.toString() );
            this.rootLayout.add( v[i] );
        }
        //FWUtil.d("addによって" + v.length + "個のViewを追加登録完了。現在の個数：" + includingViews.size() );

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
