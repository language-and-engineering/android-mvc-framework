package com.android_mvc.framework.controller;

import java.util.ArrayList;

import android.app.Activity;
import android.widget.Toast;

/**
 * Activityごとのバリデーション操作の結果を詰め込むクラス。
 * @author id:language_and_engineering
 *
 */
public class GateValidationResult {

    private boolean validation_success_flag = true;

    ArrayList<String> err_msgs;


    public GateValidationResult()
    {
        this.err_msgs = new ArrayList<String>();
    }


    /**
     * エラーメッセージを追加
     */
    public GateValidationResult err( String err_msg )
    {
        validation_success_flag = false;
        err_msgs.add(err_msg);
        return this;
    }


    /**
     * バリデが成功したとみなす
     */
    public GateValidationResult success()
    {
        validation_success_flag = true;
        return this;
    }


    /**
     * BLの実行を許可するかどうか
     */
    public boolean permitsExecitionOfBL()
    {
        return validation_success_flag;
    }


    /**
     * BLを実行しなかったかどうか
     */
    public boolean didNotExecuteBL()
    {
        return ! validation_success_flag;
    }


    /**
     * 全てのエラーメッセージを表示用に取得
     */
    private String getAllErrMsgs()
    {
        String ret = "";
        for( int i = 0; i < err_msgs.size(); i ++ )
        {
            ret += err_msgs.get(i);
            if( i != err_msgs.size() - 1 )
            {
                ret += "\n";
            }
        }

        return ret;
    }


    /**
     * バリデ失敗後の画面にて
     */
    public void onFailedActivityStarted(final Activity fromActivity)
    {
        // 失敗した旨を伝える
        fromActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText( fromActivity, getAllErrMsgs(), Toast.LENGTH_LONG).show();
            }

        });
        // 別スレッドでUI操作をじかに呼んではいけないので
        // @see http://stackoverflow.com/questions/2837676/how-to-raise-a-toast-in-asynctask-i-am-prompted-to-used-the-looper


        // NOTE: Toastを出したくない場合は，匿名クラス宣言時にoverrideすること。

    }

}
