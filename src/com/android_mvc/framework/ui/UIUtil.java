package com.android_mvc.framework.ui;


import android.app.Activity;
import android.widget.Toast;

/**
 * FW側でUIに関する便利メソッドを詰め込んだクラス。
 * @author id:language_and_engineering
 *
 */
public class UIUtil {

    /**
     * 長時間のトーストを表示
     */
    public static void longToast(final Activity activity, final String s) {
        // 非同期処理中の別スレッドからも呼べるように
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            }
        });
            // NOTE: 別スレッドでUI操作をじかに呼んではいけないので
            // @see http://stackoverflow.com/questions/2837676/how-to-raise-a-toast-in-asynctask-i-am-prompted-to-used-the-looper
    }

}
