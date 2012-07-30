package com.android_mvc.sample_project.db.dao;


import java.util.List;

import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.LocationLog;
import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.dao.BaseDAO;
import com.android_mvc.framework.db.dao.Finder;

import android.content.Context;
import android.location.Location;

/**
 * 自分の位置情報を読み書きするクラス。
 * @author id:language_and_engineering
 */
public class LocationLogDAO extends BaseDAO<LocationLog>
{

    public LocationLogDAO(Context context) {
        helper = new DBHelper(context);
    }


    /**
     * 新規位置情報を受理したときの処理。
     * 最新の１件を登録し，古いレコードを除去する。
     */
    public void onNewLocationReceived(Location location, String geoStr)
    {
        // 1件登録
        createOne(location, geoStr);

        // 古いのを消去
        deleteOldRecords();
    }


    // ------------ C --------------


    /**
     * 1件保存。
     */
    private LocationLog createOne(Location location, String geoStr)
    {
        // 論理エンティティを構築
        LocationLog loc = new LocationLog();
        loc.setRecordedAtCurrentDatetime();
        loc.setLatitudeByLocation(location);
        loc.setLongitudeByLocation(location);
        loc.setGeo_str(geoStr);

        // DB登録
        loc.save(helper);

        return loc;
    }


    // ------------ R --------------


    /**
     * 位置情報を全て新しい順に返す。
     */
    public List<LocationLog> findAll()
    {
        return findAll(helper, LocationLog.class);
    }


    /**
     * 古いレコードを全件取得。
     */
    private List<LocationLog> findOldRecords()
    {
        LocationLog newest = findNewestOne(helper, LocationLog.class);

        if( newest == null ) return null;

        return new Finder<LocationLog>(helper)
            .where("id < " + newest.getId() + " - 100")
            .findAll(LocationLog.class)
        ;
    }


    // ------------ D --------------


    /**
     * 古いレコードを全件削除
     */
    private void deleteOldRecords()
    {
        // 古いレコードを取得
        List<LocationLog> records = findOldRecords();

        if( records == null ) return;

        // 削除
        Util.d("削除対象の件数：" + records.size());
        for( LocationLog loc : records ) // NOTE:recordsがnullだとNPE
        {
            loc.delete(helper);
        }
    }



}
