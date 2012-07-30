package com.android_mvc.framework.ui.view.map;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android_mvc.framework.annotations.SuppressDebugLog;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.gps.LocationUtil;
import com.android_mvc.framework.gps.MapLocationListener;
import com.android_mvc.framework.ui.view.IFWView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.view.ViewGroup;

/**
 * MapViewのラッパークラス。
 * @author id:language_and_engineering
 *
 */
@SuppressDebugLog
public class MMapView extends MapView implements IFWView
{
    private Activity activity;

    // 自分の現在地
    private MyLocationOverlay overlay_myloc;
    private TimerTask task_waitfor_myloc;
    private int gps_lookup_period = 10 * 1000;
    private GeoPoint currentGeoPointCache;
    private MapLocationListener mapLocationListener = null;

    // アイコン
    private IconsOverlayImpl overlayImpl;
    private static final int iconSizeX = 50;
    private static final int iconSizeY = 50;


    /**
     * 初期化
     */
    public MMapView(Activity context)
    {
        // API Keyを使って初期化
        super(context, FWUtil.getGoogleMapsAPIKey());
            // NOTE: Rはアプリ側のパッケージ名に依存するので，情報を迂回して取得している。

        this.activity = context;
    }


    /**
     * 破棄処理
     */
    public void onActivityDestroy()
    {
        if( overlay_myloc != null )
        {
            stopFollowMyLocation();
        }
    }


    // --------- 自分の現在地の追跡 ----------


    /**
     * 地図の中心地として，GPSの現在地情報を追跡し続ける
     */
    public MMapView followMyLocation()
    {
        // マップ上にオーバレイを定義
        overlay_myloc = new MyLocationOverlay( activity, this );
        overlay_myloc.onProviderEnabled(LocationManager.GPS_PROVIDER);
        overlay_myloc.enableMyLocation();

        // 現在地の追跡を開始
        waitForMyLocation();

        // このオーバレイをマップ上に追加
        getOverlays().add(overlay_myloc);
        invalidate();

        return this;
    }


    /**
     * 現在地情報の追跡を終了する
     */
    public void stopFollowMyLocation()
    {
        FWUtil.d("現在地追跡処理を終了");

        if( task_waitfor_myloc != null )
        {
            task_waitfor_myloc.cancel();
        }
        if( overlay_myloc != null )
        {
            overlay_myloc.disableMyLocation();
            getOverlays().remove(overlay_myloc);
        }
    }


    /**
     * 位置検出タスクを（再）定義
     */
    private void defineWaitForMyLocationTask()
    {
        task_waitfor_myloc = new TimerTask(){
            @Override
            public void run() {
                FWUtil.d("現在地の検出処理を開始します。待機中");

                // GPSの位置情報を監視
                // NOTE: runOnFirstFixは，位置情報が変更してなくても呼ばれる
                overlay_myloc.runOnFirstFix(new Runnable() {
                    @Override
                    public void run() {
                        onMyLocationFixed();
                    }
                });
            }
        };
            // cancelしたTimerTaskは再利用不可なので
            // http://www.02.246.ne.jp/~torutk/javahow2/timer.html#doc1_id122
    }


    /**
     * 現在地の検出処理を開始
     */
    private void waitForMyLocation()
    {
        // 位置検出タスク
        defineWaitForMyLocationTask();

        // 遅延実行させる
        // NOTE: そうしないとすぐスタックオーバーフローに
        new Timer().schedule(task_waitfor_myloc, gps_lookup_period);
    }


    /**
     * 自分の現在地が検出された時の処理。
     * 検出された座標に変化がない場合もあるという点に注意。
     */
    private synchronized void onMyLocationFixed()
    {
        final GeoPoint newGeoPoint = overlay_myloc.getMyLocation();

        if( newGeoPoint.equals(currentGeoPointCache) )
        {
            FWUtil.d("検出された現在地には変化なし。：" + newGeoPoint.getLatitudeE6() + ", " + newGeoPoint.getLongitudeE6() );
        }
        else
        {
            FWUtil.d("新たな現在地を検出：" + newGeoPoint.getLatitudeE6() + ", " + newGeoPoint.getLongitudeE6() );
            currentGeoPointCache = newGeoPoint;

            // 現在のズームレベルを保持
            int currentZoomLevel = this.getZoomLevel();
                // http://www.anddev.org/map-problems-f26/get-the-current-zoom-level-t15353.html

            // マップ上で新たな現在位置へ移動
            this.getController().animateTo(newGeoPoint);
            this.getController().setCenter(newGeoPoint);

            // ズームレベルが勝手に変わってしまうので，もとのレベルに戻す
            this.zoomLevel(currentZoomLevel);

            // 取得した現在地をもとにコールバック
            if( mapLocationListener != null )
            {
                // UIスレッド上で実行する
                activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        mapLocationListener.onLocationChanged( LocationUtil.geopoint2location(newGeoPoint) );
                    }
                });
            }
        }

        // 監視を継続
        waitForMyLocation();
    }


    /**
     * 現在地検出時のフック処理をセット
     */
    public MMapView onMyLocationChanged( MapLocationListener listener )
    {
        this.mapLocationListener = listener;
        return this;
    }


    /**
     * 現在地の検出処理の実行間隔をセット
     */
    public MMapView gpsLookupPeriod(int ms) {
        gps_lookup_period = ms;
        return this;
    }


    // --------- アイコン表示 ----------


    /**
     * アイコン表示用のオーバーレイをセット
     */
    public MMapView setIconsOverlay(IconsOverlaySettings settings)
    {
        // デフォルトマーカアイコンの生成
        Resources res = this.getResources();
        Bitmap bmp_orig = BitmapFactory.decodeResource(res, settings.getIcon());
        Bitmap bmp_resized = Bitmap.createScaledBitmap(bmp_orig, iconSizeX, iconSizeY, false);
        Drawable drawable_icon = new BitmapDrawable(bmp_resized);

        // オーバーレイを生成
        overlayImpl = new IconsOverlayImpl(drawable_icon, this);
        updateIconsOnOverlay( settings.getOverlayItemsList() );
        overlayImpl.addContext(this.activity);

        // オーバーレイをマップにセット
        List<Overlay> overlays = this.getOverlays();
        overlays.add(overlayImpl);

        return this;
    }


    /**
     * マップ上にアイコン表示すべきリスト情報を更新
     */
    public void updateIconsOnOverlay(List<BaseOverlayItem> list )
    {
        overlayImpl.setIconsList( list );
    }


    // --------- Viewの属性操作 ----------


    // パラメータ保持
    HashMap<String, Object> view_params = new HashMap<String, Object>();

    @Override
    public Object getViewParam(String key) {
        return view_params.get(key);
    }

    @Override
    public void setViewParam(String key, Object val) {
        view_params.put(key, val);
    }


    // 以下は属性操作


    public MMapView widthFillParent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.FILL_PARENT );
        return this;
    }


    public MMapView heightFillParent() {
        setViewParam("layout_height", ViewGroup.LayoutParams.FILL_PARENT );
        return this;
    }

    // TODO: http://stackoverflow.com/questions/9107957/how-to-set-the-mapviews-width-and-height-in-java-code


    public MMapView touchable() {
        setEnabled(true);
        setClickable(true);
            // http://d.hatena.ne.jp/language_and_engineering/20110828/p1
        return this;
    }


    public MMapView showZoomControl() {
        setBuiltInZoomControls(true);
        return this;
    }


    public MMapView zoomLevel( int level ) {
        getController().setZoom( level );
            // NOTE: ズームレベルは1（広域）～21（細部）まで
            // https://developers.google.com/maps/documentation/android/reference/com/google/android/maps/MapController#setZoom%28int%29

        return this;
    }


    /**
     * ズームレベルを最大にする。
     */
    public MMapView zoomToMaxDetail() {
        this.zoomLevel(21);
        return this;
    }


}
