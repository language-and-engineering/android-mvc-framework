package com.android_mvc.framework.net.old;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.task.SequentialAsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * HTTP通信でPOSTリクエストを投げる処理を非同期で行うタスク。
 * @author id:language_and_engineering
 *
 */
public class HttpPostTask extends SequentialAsyncTask
{
    // NOTE: 下記のクラスを逐次化可能にしたもの。
    // @see http://d.hatena.ne.jp/language_and_engineering/20111121/p1


    // 設定事項
    private String request_encoding = "UTF-8";
    private String response_encoding = "UTF-8";

    // 初期化事項
    protected Activity parent_activity = null;
    protected String post_url = null;
    protected Handler result_handler = null;
    protected List<NameValuePair> post_params = null;

    // 処理中に使うメンバ
    private ResponseHandler<Void> response_handler = null;
    private String http_err_msg = null;
    private String http_ret_msg = null;
    private ProgressDialog dialog = null;
    private boolean isDialogRequiredFlag = false;
    private boolean dump_response_when_success = true; // 通信成功時に，ログに通信内容を吐くかどうか


    /**
     * 生成時
     */
    public HttpPostTask( Activity parent_activity, String post_url, Handler result_handler )
    {
        // 初期化
        if( parent_activity != null)
        {
            this.parent_activity = parent_activity;
            setDialogRequired( true );
        }
        this.post_url = post_url;
        this.result_handler = result_handler;

        // 送信パラメータは初期化せず，new後にsetさせる
        post_params = new ArrayList<NameValuePair>();
    }


    /**
     * 親クラスとして使うので，何もしないスーパーコンストラクタを作っておく
     */
    public HttpPostTask()
    {
        // これでOK
    }


    /**
     * タスク中にダイアログを表示する必要はあるか
     */
    protected boolean isDialogRequired()
    {
        return isDialogRequiredFlag;
    }


    /**
     * ダイアログ要求の有無を設定
     */
    public void setDialogRequired( boolean b )
    {
        FWUtil.d("POST通信中のダイアログ有無をセット：" + b);
        this.isDialogRequiredFlag = b;
    }


    /* --------------------- POSTパラメータ --------------------- */


    /**
     * 追加
     */
    public void addPostParam( String post_name, String post_value )
    {
        post_params.add(new BasicNameValuePair( post_name, post_value ));
        FWUtil.d(
            "POSTパラメータ名「"
            + post_name
            + "」，値「"
            + post_value
            + "」をセット。"
        );
    }


    /* --------------------- 処理本体 --------------------- */


    /**
     * タスク開始時
     */
    @Override
    protected void onPreExecuteHook()
    {
        // ダイアログを表示
        if( isDialogRequired() )
        {
            FWUtil.d("通信中のダイアログを表示。");
            dialog = new ProgressDialog( parent_activity );
            dialog.setMessage("通信中・・・");
            dialog.show();
        }
        else
        {
            FWUtil.d("通信中のダイアログを表示しなくてよいと判断。");
        }

        // レスポンスハンドラを生成
        response_handler = new ResponseHandler<Void>() {

            // HTTPレスポンスから，受信文字列をエンコードして文字列として返す
            @Override
            public Void handleResponse(HttpResponse response) throws IOException
            {
              FWUtil.d(
                "レスポンスコード：" + response.getStatusLine().getStatusCode()
              );

              // 正常に受信できた場合は200
              switch (response.getStatusLine().getStatusCode()) {
              case HttpStatus.SC_OK:
                FWUtil.d("レスポンス取得に成功");

                // レスポンスデータをエンコード済みの文字列として取得する。
                // ※IOExceptionの可能性あり
                HttpPostTask.this.http_ret_msg = EntityUtils.toString(
                  response.getEntity(),
                  HttpPostTask.this.response_encoding
                );
                break;

              case HttpStatus.SC_NOT_FOUND:
                // 404
                FWUtil.d("存在しない");
                HttpPostTask.this.http_err_msg = "404 Not Found";
                break;

              default:
                  FWUtil.d("通信エラー");
                HttpPostTask.this.http_err_msg = "通信エラーが発生";
              }

              return null;
            }

        };
    }


    /**
     * メイン処理
     */
    @Override
    protected boolean main()
    {
        FWUtil.d("postします");

        // URL
        URI url = null;
        try {
          url = new URI( post_url );
          FWUtil.d("URLはOK");
        } catch (URISyntaxException e) {
          e.printStackTrace();
          http_err_msg = "不正なURL";
        }

        // POSTパラメータ付きでPOSTリクエストを構築
        HttpPost request = new HttpPost( url );
        try {
          // 送信パラメータのエンコードを指定
          request.setEntity(new UrlEncodedFormEntity(post_params, request_encoding));
        } catch (UnsupportedEncodingException e1) {
          e1.printStackTrace();
          http_err_msg = "不正な文字コード";
        }

        // POSTリクエストを実行
        DefaultHttpClient httpClient = new DefaultHttpClient();
        FWUtil.d("POST開始");
        try {
            httpClient.execute(request, response_handler);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            http_err_msg = "プロトコルのエラー";
        } catch (IOException e) {
            e.printStackTrace();
            http_err_msg = "IOエラー";
        }

        // shutdownすると通信できなくなる
        httpClient.getConnectionManager().shutdown();


        // ------- 終了処理。TODO:RF --------


        // ダイアログを消す
        if( isDialogRequired() )
        {
            dialog.dismiss();
        }

        // 受信結果をUIに渡すためにまとめる
        Message message = new Message();
        Bundle bundle = new Bundle();
        if (http_err_msg != null) {
            // エラー発生時
            bundle.putBoolean("http_post_success", false);
            bundle.putString("http_response", http_err_msg);

            FWUtil.d("通信結果は失敗。内容：" + http_err_msg);

        } else {
            // 通信成功時
            bundle.putBoolean("http_post_success", true);
            bundle.putString("http_response", http_ret_msg);

            FWUtil.d("通信結果は成功。" );
            if( dump_response_when_success )
            {
                FWUtil.d( "取得内容：" + http_ret_msg );
            }
        }
        message.setData(bundle);

        // 受信結果に基づいてUI操作させる
        result_handler.sendMessage(message);

        // 逐次タスクの継続可否
        if (http_err_msg != null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    // G&S


    public boolean isDump_response_when_success() {
        return dump_response_when_success;
    }


    public void setDump_response_when_success(boolean dumpResponseWhenSuccess) {
        dump_response_when_success = dumpResponseWhenSuccess;
    }

}
