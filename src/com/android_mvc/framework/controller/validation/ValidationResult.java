package com.android_mvc.framework.controller.validation;

import java.util.ArrayList;

import com.android_mvc.framework.controller.routing.IntentPortable;

/**
 * Activityごとのバリデーション操作の結果を詰め込むクラス。
 * Intent経由で運搬される。
 * @author id:language_and_engineering
 *
 */
public class ValidationResult implements IntentPortable
{
    private static final long serialVersionUID = 1L;

    private boolean validation_success_flag = true;

    ArrayList<String> err_msgs;


    public ValidationResult()
    {
        this.err_msgs = new ArrayList<String>();
    }


    /**
     * エラーメッセージを追加
     */
    public ValidationResult err( String err_msg )
    {
        validation_success_flag = false;
        err_msgs.add(err_msg);
        return this;
    }


    /**
     * バリデが全成功したか
     */
    public boolean isSuccess()
    {
        return validation_success_flag;
    }


    /**
     * バリデが全成功した事にする
     */
    public ValidationResult setSuccess() {
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
    public String getAllErrMsgs()
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

}
