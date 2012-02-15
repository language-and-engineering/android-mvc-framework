package com.android_mvc.sample_project.domain;

import android.app.Activity;
import android.widget.Toast;

import com.android_mvc.sample_project.activities.func_db.DBEditActivity;
import com.android_mvc.sample_project.db.dao.FriendDAO;
import com.android_mvc.sample_project.db.entity.Friend;
import com.android_mvc.framework.controller.ActionResult;
import com.android_mvc.framework.controller.ActivityParams;

/**
 * DB登録に関するBL。
 * @author id:language_and_engineering
 *
 */
public class DBEditAction {

    private DBEditActivity activity;


    public DBEditAction(DBEditActivity activity) {
        this.activity = activity;
    }


    // BL本体
    public ActionResult exec() {

        ActivityParams params = activity.toParams();

        // 登録用の値を取得（バリデ通過済み）
        String name = (String)params.get("name");
        Integer age = Integer.valueOf((String)params.get("age"));
        Boolean favorite_flag = (Boolean)params.get("favorite_flag");

        // DB登録（すでに非同期でラップされているので，DB操作も同期的でよい）
        Friend f = new FriendDAO(activity).create( name, age, favorite_flag );

        // 実行結果を返す
        return new ActionResult(){
            @Override
            public void onNextActivityStarted(final Activity activity)
            {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                            activity,
                            get("new_friend_name") + "さんを登録しました。",
                            Toast.LENGTH_LONG
                        ).show();
                    }
                });
            }
        }
            .setRouteId("success")
            .add("new_friend_name", f.getName())
        ;
    }

}
