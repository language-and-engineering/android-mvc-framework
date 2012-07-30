package com.android_mvc.sample_project.db.entity;

import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;

import com.android_mvc.framework.common.DateTimeUtil;
import com.android_mvc.framework.gps.LocationUtil;
import com.android_mvc.framework.ui.view.map.BaseOverlayItem;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
import com.android_mvc.sample_project.db.entity.lib.LogicalEntity;

/**
 * 自分の特定時刻における位置情報の記録を表す論理エンティティ。
 * @author id:language_and_engineering
 *
 */
public class LocationLog extends LogicalEntity<LocationLog>
{
    // Intent経由でエンティティを運搬可能にするために
    private static final long serialVersionUID = 1L;

    @Override
    public String tableName(){return "location_logs";}

    @Override
    public final String[] columns(){
        return new String[]{ "id", "recorded_at", "latitude", "longitude", "geo_str" };
    }


    // メンバ
    private Double latitude = null;
    private Double longitude = null;
    private Calendar recorded_at = null;
    private String geo_str = null;


    // IDEが自動生成したG&S


    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Calendar getRecorded_at() {
        return recorded_at;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setRecorded_at(Calendar recordedAt) {
        recorded_at = recordedAt;
    }

    public String getGeo_str() {
        return geo_str;
    }

    public void setGeo_str(String geoStr) {
        geo_str = geoStr;
    }


    // カスタムG&S


    /**
     * 記録日時に現在日時をセット
     */
    public void setRecordedAtCurrentDatetime() {
        //Util.d("現在日時：" + DateTimeUtil.calendar2string(Calendar.getInstance(), "yyyy年MM月dd日 HH時mm分ss秒"));
        setRecorded_at( Calendar.getInstance() );
    }


    /**
     * Locationから緯度をセット
     */
    public void setLatitudeByLocation(Location location) {
        setLatitude( location.getLatitude() );
    }


    /**
     * Locationから経度をセット
     */
    public void setLongitudeByLocation(Location location) {
        setLongitude( location.getLongitude() );
    }


    /**
     * この位置情報を説明する文章
     */
    public String getDescription()
    {
        return "・"
            + DateTimeUtil.calendar2string(getRecorded_at(), "yyyy年MM月dd日 HH時mm分ss秒")
            + "：\n　"
            + getGeo_str()
        ;
    }


    // ----- LP変換(Logical <-> Physical) -----


    /**
     * DBの格納値から論理エンティティを構成
     */
    @Override
    public LocationLog logicalFromPhysical(Cursor c)
    {
        setId(c.getLong(0));
        setRecorded_at( LPUtil.decodeTextToCalendar(c.getString(1)) );
        setLatitude( c.getDouble(2) );
        setLongitude( c.getDouble(3) );
        setGeo_str( c.getString(4) );

        return this;
    }


    /**
     * 自身をDBに新規登録可能なデータ型に変換して返す
     */
    @Override
    protected ContentValues toPhysicalEntity(ContentValues values)
    {
        // entityをContentValueに変換

        if( getId() != null)
        {
            values.put("id", getId());
        }
        values.put("latitude", getLatitude());
        values.put("longitude", getLongitude());
        values.put("recorded_at", LPUtil.encodeCalendarToText( getRecorded_at() ));
        values.put("geo_str", getGeo_str());

        return values;
    }


    // その他


    /**
     * MapView上に描画可能なオブジェクトに変換
     */
    public BaseOverlayItem toMOverlayItem()
    {
        // 座標，ID，地名の３情報を渡すことにする
        return new BaseOverlayItem(
            LocationUtil.geopointFromCoordinate( getLatitude(), getLongitude() ),
            getId().toString(),
            getGeo_str()
        );
    }

}
