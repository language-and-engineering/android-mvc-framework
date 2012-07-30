package com.android_mvc.framework.gps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.android_mvc.framework.common.FWUtil;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

/**
 * GPSの座標情報と住所情報を相互変換するクラス。
 * @author id:language_and_engineering
 *
 */
public class GeocodeUtil
{
    // @see http://d.hatena.ne.jp/language_and_engineering/20110828/p1


    /**
     * 座標から住所文字列へ変換。だめなら？を返す
     */
    public static String point2geostr(double latitude, double longitude, Context context)
    {
        String address_string = new String();

        // 変換実行
        Geocoder coder = new Geocoder(context, Locale.JAPAN);
        try
        {
            List<Address> list_address = coder.getFromLocation(latitude, longitude, 1);
                // may throw IOE

            if ( ! list_address.isEmpty())
            {
                // 変換成功時は，最初の変換候補を取得
                Address address = list_address.get(0);
                address_string = LocationUtil.address2geostr( address );
            }

            FWUtil.d("地名：" + address_string);
            return address_string;
        }
        catch(IOException e)
        {
            return "？";
        }
    }


    /**
     * 住所文字列から座標情報へ変換。だめならnullを返す。
     */
    public static Address geostr2address(String str_address, Context context)
    {
        Address address = null;

        // 変換実行
        Geocoder coder = new Geocoder(context, Locale.JAPAN);
        try
        {
            // TODO: NW接続するので，別スレッド化が推奨されている
            List<Address> list_address = coder.getFromLocationName(str_address, 1);
                // may throw IOE

            if (!list_address.isEmpty()){

                // 変換成功時は，最初の変換候補を取得
                address = list_address.get(0);

            }

            return address;
        }
        catch(IOException e)
        {
            return null;
        }
    }
}