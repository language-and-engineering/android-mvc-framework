package com.android_mvc.framework.gps;

import android.location.Location;

/**
 * マップ上で現在地の変化を検出したときのフック処理を記述するクラス。
 * @author id:language_and_engineering
 *
 */
public abstract class MapLocationListener
{

    /**
     * マップが現在地の変更を検出したときのフック処理。
     * UIスレッド上で実行される。
     */
    public abstract void onLocationChanged( Location location );

}