package com.android_mvc.sample_project.activities.func_map;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.android_mvc.framework.activities.base.BaseMapActivity;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.gps.MapLocationListener;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.framework.ui.view.MToggleButton;
import com.android_mvc.framework.ui.view.map.IconsOverlaySettings;
import com.android_mvc.framework.ui.view.map.MMapView;
import com.android_mvc.framework.ui.view.map.BaseOverlayItem;
import com.android_mvc.sample_project.controller.FuncDBController;
import com.android_mvc.sample_project.db.dao.LocationLogDAO;
import com.android_mvc.sample_project.db.entity.LocationLog;

/**
 * サンプルのMapアプリのアクティビティ。
 * @author id:language_and_engineering
 *
 */
public class SampleMapActivity extends BaseMapActivity {

    MLinearLayout layout1;
    MLinearLayout layout2;
    MTextView tv1;
    MMapView map1;

    MToggleButton toggle1;

    // 位置情報のリスト
    List<LocationLog> locs;


    @Override
    public boolean requireProcBeforeUI(){
        // UI構築前に処理を要求する
        return true;
    }


    // UI構築前に別スレッドで実行される処理
    @Override
    public void procAsyncBeforeUI(){
        // 全位置情報をDBからロード
        locs = new LocationLogDAO(this).findAll();
    }


    @Override
    public void defineContentView() {

        final SampleMapActivity activity = this;

        new UIBuilder(context)
        .add(

            layout1 = new MLinearLayout(context)
              .widthFillParent()
              .heightPx(600)
              .add(

                    // GoogleMap。
                    // 自分の現在地を追跡し，なおかつ足跡の履歴をアイコン表示する。
                    map1 = new MMapView(context)
                        .widthFillParent()
                        .heightFillParent()
                        .touchable()
                        .showZoomControl()
                        .zoomToMaxDetail()

                        // GPS関連
                        .followMyLocation() // 自分の現在地を追跡し続ける
                        .gpsLookupPeriod( 10 * 1000 )
                        .onMyLocationChanged(new MapLocationListener(){
                            @Override
                            public void onLocationChanged( Location location )
                            {
                                UIUtil.longToast(context, "マップが現在位置の変更を検出。\n"
                                    + String.valueOf(location.getLatitude()) + ","
                                    + String.valueOf(location.getLongitude())
                                );

                                FuncDBController.submit(activity);
                            }
                        })

                        // マップ上に描画するアイコン関連
                        .setIconsOverlay(
                            new IconsOverlaySettings()
                                .setIconImage(android.R.drawable.sym_def_app_icon)
                                .setItemsList( getLocationLogItemsList() )
                        )
            )
            ,

            toggle1 = new MToggleButton(context)
                .textOn("現在地を追跡中")
                .textOff("現在地の追跡を開始する")
                .checked()
                .onCheck( new CheckBox.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton src, boolean isChecked) {
                        if( isChecked )
                        {
                            map1.followMyLocation();
                        }
                        else
                        {
                            map1.stopFollowMyLocation();
                        }
                    }
                } )
            ,
/*
            tv1 = new MTextView(context)
                .text("GPS常駐サービスで記録された現在位置の履歴が，\n下記に表示されます。↓\n" )
                .widthFillParent()
                .heightWrapContent()
            ,
*/
            layout2 = new MLinearLayout(context)
                .widthFillParent()
                .heightWrapContent()
                .orientationVertical()
        )
        .display();


        // 位置情報をDBから表示
        redrawRecentLocationRecords();
    }


    /**
     * マップ上に描画すべきアイコンのリストを返す
     */
    private List<BaseOverlayItem> getLocationLogItemsList()
    {
        List<BaseOverlayItem> items = new ArrayList<BaseOverlayItem>();
        for( LocationLog loc : locs )
        {
            items.add( loc.toMOverlayItem() );
        }
        return items;
    }



    @SuppressWarnings("unchecked")
    @Override
    public void afterBLExecuted(ActionResult ares)
    {
        // DBからの検索結果に基づいて，DBリスト部分を再描画
        locs = (List<LocationLog>) ares.get("recent_locations"); // TODO:
        redrawRecentLocationRecords();

        // マップ上のアイコンも再描画
        map1.updateIconsOnOverlay( getLocationLogItemsList() );

    }


    /**
     * 画面上で，最新の位置レコード情報を再描画する。
     */
    private void redrawRecentLocationRecords()
    {
/*
        TODO:
        // 中身をクリア
        layout2.removeAllIncludingViews();

        // レイアウト内に位置情報を全件表示
        for( LocationLog loc : locs )
        {
            layout2.add(
                new MTextView(context)
                    .text( loc.getDescription() ) // この位置情報の説明を取得
                    .widthFillParent()
                    .heightWrapContent()
            );
        }

        // 描画
        layout2.inflateInside();
*/
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        map1.onActivityDestroy();
    }

}