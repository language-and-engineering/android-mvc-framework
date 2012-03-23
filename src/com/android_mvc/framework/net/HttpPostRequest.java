package com.android_mvc.framework.net;

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

/**
 * 同期的なHTTP通信を行うクラス。
 * @author id:language_and_engineering
 *
 */
public class HttpPostRequest
{
    // NOTE: 下記を参考に。
    // http://d.hatena.ne.jp/language_and_engineering/20111121/p1

    // NOTE: 全BLは，自動的に別スレッド上で同期的に実行される。
    // なので，過去バージョンが持っていた「逐次実行可能性」の性質は除去した。
    // ダイアログも不要になり，きわめてシンプルになった。

    // TODO: httpsとかGETに対応


    // 設定事項
    private String request_encoding = "UTF-8";
    private String response_encoding = "UTF-8";


    // 事前
    private String target_url;
    private List<NameValuePair> post_params = new ArrayList<NameValuePair>();

    // 事後
    HttpPostResponse res = new HttpPostResponse();


    /**
     * アクセス対象のURLをセット
     */
    public HttpPostRequest target(String url)
    {
        this.target_url = url;

        FWUtil.d("this.target_urlにセット：" + this.target_url);
        return this;
    }


    /**
     * POSTパラメータを追加
     */
    public HttpPostRequest addParam(String post_name, String post_value)
    {
        post_params.add(new BasicNameValuePair( post_name, post_value ));

        FWUtil.d(
            "POSTパラメータ名「"
            + post_name
            + "」，値「"
            + post_value
            + "」をセット。"
        );
        return this;
    }


    /**
     * リクエストを送信し，通信結果を返す。
     */
    public HttpPostResponse getResponse()
    {
        if(target_url == null)
        {
            FWUtil.e("URLがセットされていません。");
            return null;
        }
        FWUtil.d("post実行します");

        // URL
        URI urlObj;
        try {
            urlObj = new URI( target_url );
            FWUtil.d("URLはOK");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return res.err( "不正なURL" );
        }

        // POSTパラメータ付きでPOSTリクエストを構築
        HttpPost request = new HttpPost( urlObj );
        try {
            // 送信パラメータのエンコードを指定
            request.setEntity(new UrlEncodedFormEntity(post_params, request_encoding));
            FWUtil.d( "文字コードはOK" );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return res.err( "不正な文字コード" );
        }

        // POSTリクエストを実行
        DefaultHttpClient httpClient = new DefaultHttpClient();
        FWUtil.d("POST開始");
        try {
            // 通信を同期的に実行し，handlerにHttpResponseを操作させる
            httpClient.execute( request, response_handler );
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return res.err( "プロトコルのエラー" );
        } catch (IOException e) {
            e.printStackTrace();
            return res.err( "IOエラー" );
        }

        // shutdownすると通信できなくなる
        httpClient.getConnectionManager().shutdown();

        return res;
    }


    /**
     * 通信結果を受理して結果を返すためのハンドラ
     */
    private ResponseHandler<Void> response_handler = new ResponseHandler<Void>() {

        // HTTPレスポンスから，受信文字列をエンコードして文字列として返す
        @Override
        public Void handleResponse(HttpResponse response) throws IOException
        {
            int response_code = response.getStatusLine().getStatusCode();
            FWUtil.d( "レスポンスコード：" + response_code );

            // 成否で場合分け
            switch ( response_code )
            {
                // 正常に受信できた場合は200
                case HttpStatus.SC_OK:
                    FWUtil.d("レスポンス取得に成功");

                    // レスポンスデータをエンコード済みの文字列として取得する。
                    // ※IOExceptionの可能性あり
                    String response_text = EntityUtils.toString(
                        response.getEntity(),
                        HttpPostRequest.this.response_encoding
                    );
                    res.setTextOnSuccess( response_text );
                    break;

                // 404
                case HttpStatus.SC_NOT_FOUND:
                    FWUtil.d("存在しない");
                    res.err( "404 Not Found" );
                    break;

                default:
                    FWUtil.d("通信エラー");
                    res.err( "通信エラーが発生" );
          }

          return null;
        }

    };

}
