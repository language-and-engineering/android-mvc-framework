package com.android_mvc.framework.controller.validation;

import android.app.Activity;

import com.android_mvc.framework.controller.routing.Router;
import com.android_mvc.framework.ui.UIUtil;


/**
 * 一つのコントロールフロー内でバリデーション操作を実行するクラス
 */
public abstract class ValidationExecutor
{
    // バリデーション実行結果
    public ValidationResult validation_result;

    // バリデ失敗時に遷移元となる画面
    public Activity from_activity;


    // --------- コアな挙動


    /**
     * ユーザ側で，具体的なバリデーション処理呼び出しを記述する。
     */
    public abstract ValidationResult doValidate();


    /**
     * ユーザ側で，バリデーション失敗時の挙動を記述する。
     */
    public abstract void onValidationFailed();


    /**
     * バリデーションを実行して，実行結果を保持する。
     */
    public void execAndStoreValidationResult() {
        this.validation_result = doValidate();
    }


    // --------- エラーメッセージの通知


    /**
     * バリデ失敗を新画面上で通知する。
     * デフォルトの通知方法はToastだが，処理内容を変更したい場合は，匿名クラス宣言時に上書きすること。
     */
    protected void showErrMessages()
    {
        UIUtil.longToast((Activity)from_activity, getAllErrMsgs() );
    }


    /**
     * 全エラーメッセージを取得する。
     */
    protected String getAllErrMsgs()
    {
    	return this.validation_result.getAllErrMsgs();
    }


    // --------- 画面遷移


    /**
     * バリデーション失敗時にも，同一画面内にとどまる。
     * デフォルトの挙動だが，このメソッドを呼び出すことで，画面遷移の仕様を明示的に記述できる。
     */
    protected void stayInThisPage()
    {
        // nop
    }


    /**
     * バリデーション失敗時に，特定の画面へ遷移する。
     */
    protected void goOnValidationFailed( Class<? extends Activity> to_activity )
    {
        Router.goWhenValidationFailed(
            from_activity,
            to_activity,
            this.validation_result
        );
    }
}

