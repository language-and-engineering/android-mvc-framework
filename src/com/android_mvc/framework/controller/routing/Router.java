package com.android_mvc.framework.controller.routing;

import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.validation.ValidationResult;


import android.app.Activity;
import android.content.Intent;

/**
 * 画面遷移と，それに伴うデータ運搬を実行するクラス。
 * @author id:language_and_engineering
 *
 */
public class Router
{
    // Intentに自動的にオブジェクトを詰め込むためのキー
    public static final String EXTRA_KEY_VALIDATION_RESULT = "_ValidationResult";
    public static final String EXTRA_KEY_ACTION_RESULT = "_ActionResult";
        // TODO: その他の各種情報をIntent内に自動的に詰め込んでおく


    /**
     * 単純に，別の画面に遷移する。
     * Intent経由で情報を渡さない。
     */
    public static void go(Activity fromActivity, Class<? extends Activity> toActivity)
    {
        Intent intent = new Intent(
            fromActivity.getApplicationContext(),
            toActivity
        );
        fromActivity.startActivity(intent);
    }


    /**
     * Intentに情報を込めつつ，別の画面に遷移する。
     */
    public static void goWithData(Activity fromActivity, Class<? extends Activity> toActivity, Intent src)
    {
        Intent intent = new Intent(
            fromActivity.getApplicationContext(),
            toActivity
        );

        // Extraを全コピー
        intent.putExtras(src);

        fromActivity.startActivity(intent);
    }


    // 下記はFW内で利用


    /**
     * バリデ失敗時の画面遷移
     */
    public static void goWhenValidationFailed(
        Activity from_activity,
        Class<? extends Activity> to_activity,
        ValidationResult vres
    )
    {
        Intent intent = new Intent(
            from_activity.getApplicationContext(),
            to_activity
        );

        // バリデーション結果を格納
        intent.putExtra(EXTRA_KEY_VALIDATION_RESULT, vres);

        from_activity.startActivity(intent);

    }


    /**
     *  BL実行時の画面遷移。
     * アクション実行結果ごとのルーティング識別子によって分岐。
     */
    public static void switchByActionResult(
        Activity from_activity,
        ActionResult ares,
        RoutingTable routingTable
    )
    {
        // ルーティング識別子を取得
        String route_id = ares.getRouteId();

        // 遷移先のActivityのクラスを取得
        Class<? extends Activity> to_activity = routingTable.getActivityByRouteId(route_id);

        if( to_activity != null )
        {
            // 遷移を実行
            Intent intent = new Intent(
                from_activity.getApplicationContext(),
                to_activity
            );

            // アクション実行結果を格納
            intent.putExtra(EXTRA_KEY_ACTION_RESULT, ares);

            from_activity.startActivity(intent);

            // BL実行完了を新画面上で通知
            ares.onNextActivityStarted(from_activity);
        }
        else
        {
            // 遷移先がnullの場合は，遷移しないでとどまる。
        }

    }


}
