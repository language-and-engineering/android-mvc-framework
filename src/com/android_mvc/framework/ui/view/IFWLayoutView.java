package com.android_mvc.framework.ui.view;

import android.view.View;

/**
 * フレームワーク内で独自に拡張したLayoutであることを示すためにimplementするインタフェース。
 * @author id:language_and_engineering
 *
 */
public interface IFWLayoutView
{
    // NOTE: 実装側でこれらがpublicになるのは極めて不本意だが，カスタムビューを透過的に扱うために仕方がない。


    /**
     * 特定のViewのタテヨコ値を取得
     */
    int[] getWidthHeightOfView(View v);


    /**
     * このレイアウトの描画を実行する。
     * 中身にレイアウトを含む場合，再帰的に呼び出される。
     * すでに描画が完了しているViewはスキップする。
     */
    void inflateInside();


    /**
     * 1つのビューをレイアウト内に描画する。
     */
    void registerAndRenderOneView(View v);


    /**
     * 描画済みの内部Viewの個数を返す。
     */
    int getNumInflatedViews();


    /**
     * 特定のインデックスの内部ビューを返す。
     */
    View getIncludingViewAt(int i);


    /**
     * 描画済みビューの個数情報を更新する。
     */
    void updateNumInflatedViews();


    /**
     * 内部ビューの全個数を返す。
     */
    int getIncludingViewsSize();


    /**
     * レイアウト内に描画したいビューを１つ以上登録する。
     * @param v 可変個のView
     */
    IFWLayoutView add(View...v);


    /**
     * レイアウト内に描画したいビューを１つ登録する。
     */
    void addOneIncludingView( View v );


    /**
     * 内部に登録済みの全Viewを登録解除する。
     */
    void removeAllIncludingViews();

}
