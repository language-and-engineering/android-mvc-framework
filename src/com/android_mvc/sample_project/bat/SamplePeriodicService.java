package com.android_mvc.sample_project.bat;


import com.android_mvc.framework.bat.BasePeriodicService;

import com.android_mvc.framework.gps.GetMyLocationEventTask;
import com.android_mvc.framework.gps.LocationUtil;
import com.android_mvc.framework.task.AsyncTasksRunner;
import com.android_mvc.framework.task.ISequentialRunnable;
import com.android_mvc.framework.task.RunnerFollower;
import com.android_mvc.framework.task.SequentialAsyncTask;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.dao.LocationLogDAO;
import com.android_mvc.framework.ui.UIUtil;

import android.content.Context;
import android.location.Location;

/**
 * 常駐型サービスのサンプル。
 * 現在地を定期的にDBに記録する。
 * @author id:language_and_engineering
 *
 */
public class SamplePeriodicService extends BasePeriodicService
{
    // 画面から常駐を解除したい場合のために，常駐インスタンスを保持
    public static BasePeriodicService activeService;


    @Override
    protected long getIntervalMS()
    {
        return 1000 * 10;
    }


    @Override
    protected void execTask()
    {
        // インスタンスを保持
        activeService = this;

        // GPS情報のDB記録処理を非同期で開始
        final Context context = getContext();
        new AsyncTasksRunner( new ISequentialRunnable[]{

            // 現在地を検出する非同期タスク。
            new  GetMyLocationEventTask(context){
                @Override
                protected boolean onLocationReceived( Location location )
                {
                    // 次のタスクに渡して処理させる
                    storeData( "location_result", location );

                    return CONTINUE_TASKS;
                }
            }
            ,

            // 位置情報のDB登録を行なうタスク。
            new SequentialAsyncTask(){
                public boolean main(){
                    // 前のGPS処理の結果を取りだす
                    Location location = (Location)getDataFromRunner("location_result");

                    // 地名に変換する。NW接続が必要な重い処理なので，このタイミングで呼び出す。
                    String geo_str = LocationUtil.location2geostr(location, context);

                    // DB登録
                    new LocationLogDAO(context).onNewLocationReceived( location, geo_str );

                    UIUtil.longToastByHandler(
                        getHandler(), context,
                        "サービスが現在地を検出しました。\n" + geo_str
                        + "\n※アプリの常駐停止ボタンから停止できます。"
                    );

                    return CONTINUE_TASKS;
                }
            }

        })
        .withoutDialog()
        .whenAllTasksCompleted(new RunnerFollower(){
            @Override
            protected void exec() {
                // ログ出力
                Util.d("GPS処理が完了しました。");

                // 次回の実行について計画を立てる
                makeNextPlan();
            }
        })
        .begin();

    }


    @Override
    public void makeNextPlan()
    {
        this.scheduleNextTime();
    }


    /**
     * もし起動していたら，サービスの常駐を解除する。
     * 既に起動済みのタスクがある場合，タスクは中断されない。
     */
    public static void stopResidentIfActive(Context context) {
        if( activeService != null )
        {
            activeService.stopResident(context);
        }
    }

}

