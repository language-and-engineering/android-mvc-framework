package com.android_mvc.framework.ui.view;

/**
 * フレームワーク内で独自に拡張したビューであることを示すためにimplementするインタフェース。
 * @author id:language_and_engineering
 *
 */
public interface IFWView
{
    /**
     * ビュー本体に直接セットできないパラメータを返す。
     * widthにsetterでfill_parentなどを直接セットできる場合は，利用する必要がないのでnullを返せばよい。
     */
    public Object getViewParam(String key);


    /**
     * ビュー本体に直接セットできないパラメータを保持する
     */
    public void setViewParam(String key, Object val);


    // NOTE: これらの処理は共通なので，実装をコピペせざるを得ない。
    // Javaでは多重継承できないため，特定のViewを継承してしまうと，もう他の処理は共通化できぬ。

}
