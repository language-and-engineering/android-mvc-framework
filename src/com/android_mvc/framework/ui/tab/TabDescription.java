package com.android_mvc.framework.ui.tab;

/**
 * つまみ一つ分のタブを表すクラス。
 * @author id:language_and_engineering
 *
 */
public class TabDescription
{
    public String tabTag;
    public String displayText;
    public int icon_resource_id = 0;
    //getterを作るまでもなし


    /**
     * 初期化。
     * 他のタブと区別するために，タブのタグを登録する。
     */
    public TabDescription(String tabTag)
    {
        this.tabTag = tabTag;
    }


    /**
     * タブつまみに表示する文字列を登録。
     */
    public TabDescription text(String displayText)
    {
        this.displayText = displayText;
        return this;
    }


    /**
     * タブつまみに表示する画像のリソースIDを登録。
     */
    public TabDescription icon(int icon_resource_id) {
        this.icon_resource_id = icon_resource_id;
        return this;
    }


    /**
     * タブつまみに画像を表示しない旨を登録。
     */
    public TabDescription noIcon() {
        this.icon_resource_id = 0;
        return this;
    }

}
