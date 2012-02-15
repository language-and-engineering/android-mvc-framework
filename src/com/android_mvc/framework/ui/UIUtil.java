package com.android_mvc.framework.ui;


import android.content.Context;
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
    public static void longToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

}
