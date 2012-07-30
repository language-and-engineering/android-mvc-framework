package com.android_mvc.framework.ui.view.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.android_mvc.framework.ui.UIUtil;
import com.google.android.maps.ItemizedOverlay;

/**
 * マップ上に独自アイコンを表示するためのオーバレイ。
 * @author id:language_and_engineering
 *
 */
public class IconsOverlayImpl extends ItemizedOverlay<BaseOverlayItem>
{

    // 描画中のアイコンの配列。複数スレッドからアクセスされ得るので注意。
    private List<BaseOverlayItem> overlay_items = new ArrayList<BaseOverlayItem>();

    private MMapView map;
    private Context context;


    /* --------- 基本 -------- */


    public IconsOverlayImpl(Drawable defaultMarker, MMapView mMapView)
    {
          super(boundCenterBottom(defaultMarker));
          this.map = mMapView;

          populate();
              // コンストラクタ内でpopulate() しないと，NullPointerExceptionになるので。
              // http://developmentality.wordpress.com/2009/10/19/android-itemizedoverlay-arrayindexoutofboundsexception-nullpointerexception-workarounds/
              // populate()がない場合，ユーザ画面からホーム画面に戻って最初の数秒の
              // まだGPS情報が取得されるよりも前の時点でタップすると落ちた。
    }


    @Override
    protected BaseOverlayItem createItem(int i) {
        return overlay_items.get(i);
    }


    @Override
    public int size() {
        return overlay_items.size();
    }


    /**
     * アイコン表示対象のリストを追加
     */
    public void setIconsList(List<BaseOverlayItem> overlayItemsList) {
        this.overlay_items = overlayItemsList;
        populate();
    }


    /**
     * Contextを追加
     */
    public void addContext(Context context)
    {
        this.context = context;
    }


    /* --------- アイコンの描画 -------- */


    /**
     * マップ上にアイコンを再描画
     */
    protected void redrawMapIcons()
    {
        // マップUIを強制的に再描画
        map.postInvalidate();
    }


    /* --------- イベント -------- */


    // アイコンをタップ時
    @Override
    protected boolean onTap(int index) {

        BaseOverlayItem oi = overlay_items.get(index);

        UIUtil.longToast(context, oi.getSnippet());

        return true;
    }

}