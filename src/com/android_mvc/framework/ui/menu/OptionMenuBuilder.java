package com.android_mvc.framework.ui.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * オプションメニューを構築するためのビルダー。
 * @author id:language_and_engineering
 *
 */
public class OptionMenuBuilder
{
    // 表示項目たち
    private ArrayList<OptionMenuDescription> descriptions = new ArrayList<OptionMenuDescription>();


    public OptionMenuBuilder(Activity context)
    {
    }


    /**
     * 表示項目を一つ登録
     */
    public OptionMenuBuilder add(OptionMenuDescription desc)
    {
        descriptions.add(desc);
        return this;
    }


    /**
     * 実際のMenuオブジェクトに，表示項目を描画用に登録
     */
    public Menu registerItemsInMenu(Menu menu)
    {
        for( int i = 0; i < descriptions.size(); i ++)
        {
            menu.add(
                Menu.NONE,
                i,
                Menu.NONE,
                descriptions.get(i).displayText()
            );
        }

        return menu;
    }


    /**
     * 特定の項目が選択された時のイベントを処理
     */
    public void onItemSelected(MenuItem item)
    {
        // itemIdを横並びの順序番号とみなす
        int index = item.getItemId();
        OptionMenuDescription targetDescription = descriptions.get(index);

        // イベントハンドラを実行
        targetDescription.onSelected();

        // TODO: 縦並びも許可する
    }

}
