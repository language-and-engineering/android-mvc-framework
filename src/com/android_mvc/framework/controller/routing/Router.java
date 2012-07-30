package com.android_mvc.framework.controller.routing;

import com.android_mvc.framework.common.FWUtil;
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
        // NOTE: その他の各種情報をIntent内に自動的に詰め込んでおく


    /**
     * 単純に，別の画面に遷移する。
     * Intent経由で情報を渡さない。
     */
    public static void go(Activity fromActivity, Class<? extends Activity> toActivity)
    {
        // ありがちなコーディングミスを予防しておく
        if( toActivity == null )
        {
            FWUtil.e("遷移先がnullです。");
            return;
        }

        // 遷移
        Intent intent = new Intent(
            fromActivity.getApplicationContext(),
            toActivity
        );
        fromActivity.startActivity(intent);
    }


    /**
     * ルーティングテーブルに従い，別の画面に遷移する。
     * Intent経由で情報を渡さない。
     */
    public static void goByRoutingTable(Activity fromActivity, String routeId, RoutingTable table)
    {
        Class<? extends Activity> destActivityClass = table.getActivityByRouteId(routeId);
        Router.go(fromActivity, destActivityClass);

        // TODO: Intent付きも可能にする？
    }


    /**
     * Intentに情報を込めつつ，別の画面に遷移する。
     */
    public static void goWithData(Activity fromActivity, Class<? extends Activity> toActivity, Intent src)
    {
        if( toActivity == null )
        {
            FWUtil.e("遷移先がnullです。");
            return;
        }

        Intent intent = new Intent(
            fromActivity.getApplicationContext(),
            toActivity
        );

        // Extraを全コピー
        intent.putExtras(src);
            // NOTE: ここでsrcのIntentは，メソッド間でのextrasの運搬媒体としてのみ利用している。
            // 遷移先に関する情報はC層で持ちまわって制御したいから。

        // 遷移
        fromActivity.startActivity(intent);
    }


    /**
     * Intentに情報を込めつつ，別の画面に遷移する。
     * 遷移先についてコメントを記述できる。
     */
    public static void goWithData(Activity fromActivity, Class<? extends Activity> toActivity, String comment, Intent src)
    {
        goWithData(fromActivity, toActivity, src);
    }


    // 下記はFW内で利用


    /**
     * バリデ失敗時の画面遷移
     */
    public static void goWhenValidationFailed(
        Activity from_activity,
        Class<? extends Activity> toActivity,
        ValidationResult vres
    )
    {
        if( toActivity == null )
        {
            FWUtil.e("遷移先がnullです。");
            return;
        }

        Intent intent = new Intent(
            from_activity.getApplicationContext(),
            toActivity
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
            // NOTE: 遷移先がnullの場合は，遷移しないでとどまる。
            // 特定のケースで移動を阻止するため，意図的にnull指定する場合もあるかもしれないから。
            FWUtil.d("遷移先がnullです。");
        }

    }


}
