package com.android_mvc.framework.gps;

import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.location.Address;
import android.location.Location;


/**
 * 位置情報に関する変換などの共通処理
 * @author id:language_and_engineering
 *
 */
public class LocationUtil
{

    // ----------- 地名関連 -----------


    /**
     * Locationをフル地名に変換
     */
    public static String location2geostr( Location location, Context context)
    {
        return GeocodeUtil.point2geostr(
            location.getLatitude(),
            location.getLongitude(),
            context
        );
    }


    /**
     * 地名KWをAddressに変換
     */
    public static Address geostr2address( String geo_str, Context context)
    {
        return GeocodeUtil.geostr2address(
            geo_str,
            context
        );
    }


    /**
     * Addressをフル地名に変換
     */
    public static String address2geostr( Address address )
    {
        StringBuffer sb = new StringBuffer();

        // adressの大区分から小区分までを改行で全結合
        String s;
        for (int i = 0; (s = address.getAddressLine(i)) != null; i++){
            sb.append( s + "\n" );
        }

        return sb.toString();
    }


    // ----------- 座標関連のオブジェクトの相互変換 -----------


    /**
     * GeoPointをLocationに変換
     */
    public static Location geopoint2location( GeoPoint geoPoint )
    {
        float latitude = geoPoint.getLatitudeE6() / 1000000F;
        float longitude = geoPoint.getLongitudeE6() / 1000000F;
        Location location = locationFromCoordinate(latitude, longitude);
            // @see http://stackoverflow.com/questions/531324/android-location-distanceto-not-working-correctly
            // http://stackoverflow.com/questions/3472603/convert-geopoint-to-location

        return location;
    }


    /**
     * AddressクラスからLocationに変換
     */
    public static Location address2location( Address address )
    {
        return LocationUtil.locationFromCoordinate(
            (float)address.getLatitude(),
            (float)address.getLongitude()
        );
    }


    /**
     * 緯度と経度からLocationオブジェクトを返す
     */
    public static Location locationFromCoordinate(float lat, float lon) {
        Location location = new Location("reverseGeocoded");
        location.setLatitude(lat);
        location.setLongitude(lon);

        return location;
    }


    /**
     * 緯度と経度からGeoPointオブジェクトを返す
     */
    public static GeoPoint geopointFromCoordinate(double lat, double lon) {
        int micro_lat = (int)(lat * 1E6);
        int micro_lon = (int)(lon * 1E6);

        GeoPoint g = new GeoPoint(micro_lat, micro_lon);

        return g;
    }

}
