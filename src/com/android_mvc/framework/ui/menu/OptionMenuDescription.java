package com.android_mvc.framework.ui.menu;


/**
 * オプションメニュー内の１項目を表すためのクラス。
 * @author id:language_and_engineering
 *
 */
public abstract class OptionMenuDescription
{
    /**
     * メニュー内の項目に表示される文言。
     */
    protected abstract String displayText();


    /**
     * メニュー内の項目選択時の挙動。
     * @param activity
     */
    protected abstract void onSelected();
}
