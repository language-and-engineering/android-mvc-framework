package com.android_mvc.framework.ui.view.map;

import java.util.List;

/**
 * アイコンを自由に配置できるオーバレイの設定をするクラス。
 * @author id:language_and_engineering
 *
 */
public class IconsOverlaySettings {

    private int resId;
    private List<BaseOverlayItem> itemsList;


    /**
     * 描画アイコンのリソースIDを指定
     */
    public IconsOverlaySettings setIconImage( int resId )
    {
        this.resId = resId;
        return this;
    }


    /**
     * 描画アイコンのリソースIDを取得
     */
    public int getIcon()
    {
        return resId;
    }


    /**
     * オーバレイアイテムのリストをセット
     */
    public IconsOverlaySettings setItemsList(List<BaseOverlayItem> itemsList) {
        this.itemsList = itemsList;
        return this;
    }


    /**
     * オーバレイアイテムのリストを取得
     */
    public List<BaseOverlayItem> getOverlayItemsList()
    {
        return itemsList;
    }


}
