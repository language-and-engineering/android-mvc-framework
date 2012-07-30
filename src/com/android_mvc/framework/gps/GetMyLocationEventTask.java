package com.android_mvc.framework.gps;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.task.SequentialEventTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

/**
 * 逐次非同期で自分の現在地を取得するためのクラス。
 * 地名変換処理は別の非同期タスクで実行すること。
 * @author id:language_and_engineering
 *
 */
public abstract class GetMyLocationEventTask extends SequentialEventTask implements LocationListener
{

    private LocationManager mLocationManager;
    private Context context;
    private boolean continue_tasks_flag;


    public GetMyLocationEventTask(Context context) {
        this.context = context;
    }


    /**
     * 位置情報を取得したときの処理を記述する。
     */
    protected abstract boolean onLocationReceived( Location location );


    // -------- 非同期タスクとしての性質 --------


    @Override
    public void kickEventAndWait()
    {
        // GPS機能を呼び出し
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager != null) {

            // 別スレッドでGPSを呼ぶので，明示的にLooperが必要
            Looper looper = Looper.getMainLooper();
                // @see http://d.hatena.ne.jp/terurou/20110825
                // http://www.stevenmarkford.com/cant-create-handler-inside-thread-that-has-not-called-looper-prepare-in-android/

            // 現在地取得をリクエストし，応答を待つ
            mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                this,
                looper
            );
            FWUtil.d("LocationManagerの処理を開始。位置変更の検出を待っています・・・");
            // onLocationChangedが呼ばれるまで待機。
        }

    }


    // -------- GPS処理クラスとしての性質 --------


    // 位置情報取得時に呼ばれる。呼ばれるまで時間がかかるかも。
    @Override
    public void onLocationChanged(Location location)
    {
        FWUtil.d("位置変更を検出");
        FWUtil.d("GPS:Latitude" + String.valueOf(location.getLatitude()));
        FWUtil.d("GPS:Longitude" + String.valueOf(location.getLongitude()));

        // GPS処理をクローズ
        mLocationManager.removeUpdates(this);
            // http://d.hatena.ne.jp/glass-_-onion/20101113/1289615195

        // 取得したlocationを実装側に引き渡して処理させる。タスク継続の可否を返却させる
        continue_tasks_flag = onLocationReceived( location );

        // このタスクを終了し，ランナー側に制御を戻す
        endEventAndBackToRunner(continue_tasks_flag);
    }


    @Override
    public void onProviderDisabled(String provider) {
        // TODO:異常系
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


}
