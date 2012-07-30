package com.android_mvc.framework.ui.view.map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * オーバレイアイテムの基底クラス。
 * 適宜拡張すること。
 * @author id:language_and_engineering
 *
 */
public class BaseOverlayItem extends OverlayItem
{
    /**
     * 初期化
     */
    public BaseOverlayItem(GeoPoint gp, String name, String description)
    {
        super(gp, name, description);
    }

}
