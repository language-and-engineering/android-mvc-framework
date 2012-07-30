package com.android_mvc.framework.net;

import com.android_mvc.framework.controller.routing.IntentPortable;

/**
 * HTTP通信の結果を表すオブジェクト。
 * @author id:language_and_engineering
 *
 */
public class HttpPostResponse implements IntentPortable
{
    // Intent運搬用
    private static final long serialVersionUID = 1L;


    private String err_msg;
    private boolean request_success_flag = false;
    private String responseText;


    /**
     * エラーメッセージを追加
     */
    public HttpPostResponse err(String err_msg)
    {
        this.err_msg = err_msg;
        request_success_flag = false;

        return this;
    }


    /**
     * 通信成功時の結果を保持
     * @param responseText
     */
    public void setTextOnSuccess(String responseText)
    {
        this.responseText = responseText;
        request_success_flag = true;
    }


    /**
     * 通信が成功したかどうか
     */
    public boolean isSuccess()
    {
        return request_success_flag;
    }


    /**
     * 通信成功時の取得テキストを返す
     */
    public String getText()
    {
        return responseText;
    }


    /**
     * 通信後に保持しているエラーメッセージを返す
     */
    public String getErrMsg()
    {
        return err_msg;
    }


}
