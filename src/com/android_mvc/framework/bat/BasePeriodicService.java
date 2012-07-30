package com.android_mvc.framework.bat;

import com.android_mvc.framework.common.FWUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * 常駐型サービスの基底クラス。
 * @author id:language_and_engineering
 *
 */
public abstract class BasePeriodicService extends Service
{
    // 常駐不可フラグ
    private boolean mustStopResident = false;

    // メインスレッドで生成されたハンドラ
    private static Handler handlerMainThread = null;


    /**
     * サービスの定期実行の間隔をミリ秒で指定。
     * 処理が終了してから次に呼ばれるまでの時間。
     */
    protected abstract long getIntervalMS();


    /**
     * 定期実行したいタスクの中身（１回分）。
     * タスクの実行が完了したら，次回の実行計画を立てること。
     */
    protected abstract void execTask();


    /**
     * 次回の実行計画を立てる。
     */
    protected abstract void makeNextPlan();


    /**
     * ServiceのContextを返す。
     */
    protected Context getContext()
    {
        return this;
    }


    /**
     * メインスレッドで生成されたハンドラを返す。
     */
    protected Handler getHandler()
    {
        return handlerMainThread;
    }


    // ---------- 必須メンバ -----------


    protected final IBinder binder = new Binder()
    {
        @Override
        protected boolean onTransact( int code, Parcel data, Parcel reply, int flags ) throws RemoteException
        {
            return super.onTransact(code, data, reply, flags);
        }
    };


    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }


    // ---------- サービスのライフサイクル -----------


    /**
     * 常駐を開始
     */
    public BasePeriodicService startResident(Context context)
    {
        if( handlerMainThread == null )
        {
            FWUtil.d("ハンドラがないため，生成します。");
            handlerMainThread = new Handler();
        }

        FWUtil.d("サービスの常駐を開始します。");
        Intent intent = new Intent(context, this.getClass());
        intent.putExtra("type", "start");
        context.startService(intent);
            // NOTE: ここから先は，違うインスタンスとしてサービスが生成される。
            // 何かを渡したい場合はintentに含めるか，staticメンバを経由する。

        return this;
    }


    /**
     * 常駐を開始。別スレッドから呼び出す場合。
     */
    public BasePeriodicService startResident(Context context, Handler handler)
    {
        // ハンドラを保持
        handlerMainThread = handler;

        this.startResident(context);

        return this;
    }


    @Override
    public void onStart(Intent intent, int startId)
    {
        // サービス起動時の処理。
        // サービス起動中に呼ぶと複数回コールされ得る。しかし二重起動はしない
        // @see http://d.hatena.ne.jp/rso/20110911

        super.onStart(intent, startId);

        mustStopResident = false;

        FWUtil.d("サービスからタスクを起動");
        execTask();

            // NOTE: 重いタスクかもしれないが，同一スレッド内でコールしている。
            // 理由は，もし重いタスクであれば，メソッド内部で逐次非同期にタスクを走らせればよいから。
            // ここの時点で別スレッドに分けてしまうと，逐次非同期のタスクランナーが実行できなくなってしまう。

            // NOTE: ここで次回の実行計画を逐次的にコールしていない理由は，
            // タスクが非同期の場合があるから。

    }


    /**
     * サービスの次回の起動を予約
     */
    public void scheduleNextTime() {

        // サービス終了の指示が出ていたら，次回の予約はしない。
        if( mustStopResident ) return;

        long now = System.currentTimeMillis();

        // アラームをセット
        PendingIntent alarmSender = PendingIntent.getService(
            this,
            0,
            new Intent(this, this.getClass()),
            0
        );
        AlarmManager am = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        am.set(
            AlarmManager.RTC,
            now + getIntervalMS(),
            alarmSender
        );
        FWUtil.d("次回登録が完了");

    }


    /**
     * サービスの定期実行を解除し，サービスを停止
     */
    public void stopResident(Context context)
    {
        // TODO: 既存の実行タスクも終了させる？今は生かしてある。


        FWUtil.d("サービスの常駐を終了します。");

        // 次回予約をさせない
        mustStopResident = true;

        // サービス名を指定
        Intent intent = new Intent(context, this.getClass());

        // アラームを解除
        PendingIntent pendingIntent = PendingIntent.getService(
            context,
            0, // ここを-1にすると解除に成功しない
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
            // @see http://creadorgranoeste.blogspot.com/2011/06/alarmmanager.html

        // サービス自体を停止
        stopSelf();
    }


}
