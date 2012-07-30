package com.android_mvc.framework.bat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * 端末起動時にサービスを自動開始させるためのクラス。
 * @author id:language_and_engineering
 *
 */
public abstract class BaseOnBootReceiver extends BroadcastReceiver
{
    // バッチ側の別スレッドからToastを表示したい時などのために
    protected Handler handlerMainThread = null;


    // ブロードキャストインテント検知時
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        // 端末起動時？
        if( Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            // メインスレッドでHandlerを生成・保持しておく
            handlerMainThread  = new Handler();

            // 別スレッドで処理を実行
            new Thread(new Runnable(){
                @Override
                public void run()
                {
                    onDeviceBoot(context);
                }
            }).start();

        }

        // NOTE:
        // ・このメソッド終了時点でこのオブジェクトは消滅
        // ・このメソッドはメインスレッドで呼ばれ，10秒以上の長い処理は禁物
    }


    /**
     * 端末起動時に呼ばれるメソッド。
     * メインスレッド上ではないので，実行時間の心配はない。
     */
    protected abstract void onDeviceBoot(Context context);


    /**
     * メインスレッドで生成されたハンドラを返す。
     */
    protected Handler getHandler()
    {
        return handlerMainThread;
    }

}
