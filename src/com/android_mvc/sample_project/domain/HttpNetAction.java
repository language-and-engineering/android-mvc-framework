package com.android_mvc.sample_project.domain;

import com.android_mvc.sample_project.activities.func_net.HttpNetActivity;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.net.HttpPostRequest;
import com.android_mvc.framework.net.HttpPostResponse;

/**
 * 通信に関するBL。
 * @author id:language_and_engineering
 *
 */
public class HttpNetAction extends BaseAction
{

    private HttpNetActivity activity;


    public HttpNetAction(HttpNetActivity activity) {
        this.activity = activity;
    }


    // BL本体
    @Override
    public ActionResult exec()
    {
        ActivityParams params = activity.toParams();

        // 入力されたURLを取得
        String url = (String)params.getValue("http_url");

        // HTTP通信を実行（同期的）
        HttpPostResponse response = new HttpPostRequest()
            .target(url)
            .addParam("key1", "value1") // POSTパラメータを追加できる
            .getResponse()
        ;

        // 通信の成否を判定
        String routeId;
        if( response.isSuccess() )
        {
            routeId = "success";
        }
        else
        {
            routeId = "failed";
        }

        // 実行結果を返す
        return new HttpNetActionResult()
            .setRouteId( routeId )
            .add("http_response", response)
        ;
    }


    // 実行結果オブジェクト
    static class HttpNetActionResult extends ActionResult
    {
        private static final long serialVersionUID = 1L;
    }

}
