package com.android_mvc.framework.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * FW側でUIに関する便利メソッドを詰め込んだクラス。
 * @author id:language_and_engineering
 *
 */
public class UIUtil {

    /**
     * 長時間のトーストを表示。
     * 引数としてActivityが利用な場合であれば，いかなるスレッドからも表示可能。
     */
    public static void longToast(final Activity activity, final String s) {
        // 非同期処理中の別スレッドからも呼べるように
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                longToast( activity.getApplicationContext(), s );
            }
        });
            // NOTE: 別スレッドでUI操作をじかに呼んではいけないので
            // @see http://stackoverflow.com/questions/2837676/how-to-raise-a-toast-in-asynctask-i-am-prompted-to-used-the-looper
    }


    /**
     * 長時間のトーストを表示
     */
    public static void longToast(Context context, String s)
    {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }


    /**
     * 長時間のトーストを表示。
     * 画面がない場合に別スレッドで呼びたい場合などに使う。
     */
    public static void longToastByHandler(Handler handlerMainThread, final Context context, final String s)
    {
        handlerMainThread.post(new Runnable(){
            @Override
            public void run() {
                longToast( context, s );
            }
        });
            // http://stackoverflow.com/questions/6134013/android-how-can-i-show-a-toast-from-a-thread-running-in-a-remote-service
    }


}
