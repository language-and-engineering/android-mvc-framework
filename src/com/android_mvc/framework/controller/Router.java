package com.android_mvc.framework.controller;

import java.util.HashMap;

import com.android_mvc.framework.common.FWUtil;


import android.app.Activity;
import android.content.Intent;

/**
 * 画面遷移を実行するクラス。
 * @author id:language_and_engineering
 *
 */
public class Router
{

    /**
     * バリデ失敗時の画面遷移
     */
    public static void go(
        Activity from_activity,
        Class<? extends Activity> to_activity,
        GateValidationResult vres
    )
    {
        Intent intent = new Intent(
            from_activity.getApplicationContext(),
            to_activity
        );

        from_activity.startActivity(intent);

        // バリデ失敗を新画面上で通知
        vres.onFailedActivityStarted(from_activity);
    }


    /**
     *  BL実行時の画面遷移。
     * アクション実行結果ごとのルーティング識別子によって分岐。
     */
    public static void switchByActionResult(
        Activity from_activity,
        ActionResult ares,
        HashMap<String, Class<? extends Activity>> routingTable
    )
    {
        // ルーティング識別子を取得
        String route_id = ares.getRouteId();

        // 遷移先のActivityのクラスを取得
        Class<? extends Activity> to_activity = routingTable.get(route_id);

        if( to_activity != null )
        {
            // 遷移を実行
            Intent intent = new Intent(
                from_activity.getApplicationContext(),
                to_activity
            );
            from_activity.startActivity(intent);
                // TODO: ActionResultをputExtraして，遷移先の画面で参照できるようにする。

            // BL実行完了を新画面上で通知
            ares.onNextActivityStarted(from_activity);
        }
        else
        {
            FWUtil.e("ルーティング識別子「" + route_id + "」に対応する遷移先アクティビティが指定されていません。");
        }

    }


    /**
     * 単純に，別の画面に遷移する。
     */
	public static void go(Activity fromActivity, Class<? extends Activity> toActivity)
	{
        Intent intent = new Intent(
            fromActivity.getApplicationContext(),
            toActivity
        );
        fromActivity.startActivity(intent);
	}

}
