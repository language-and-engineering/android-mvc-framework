package com.android_mvc.sample_project.domain;

import android.app.Activity;

import com.android_mvc.sample_project.activities.func_db.DBEditActivity;
import com.android_mvc.sample_project.db.dao.FriendDAO;
import com.android_mvc.sample_project.db.entity.Friend;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIUtil;

/**
 * DB登録に関するBL。
 * @author id:language_and_engineering
 *
 */
public class DBEditAction extends BaseAction
{

    private DBEditActivity activity;


    public DBEditAction(DBEditActivity activity) {
        this.activity = activity;
    }


    // BL本体
    @Override
    public ActionResult exec()
    {
        ActivityParams params = activity.toParams();

        // 登録用の値を取得（バリデ通過済み）
        String name = (String)params.getValue("name");
        Integer age = Integer.valueOf((String)params.getValue("age"));
        Boolean favorite_flag = (Boolean)params.getValue("favorite_flag");


        // DB登録（すでに非同期でラップされているので，DB操作も同期的でよい）
        Friend f = new FriendDAO(activity).create( name, age, favorite_flag );


        // 実行結果を返す
        return new DBEDitActionResult()
            .setRouteId("success")
            .add("new_friend_name", f.getName())
            .add("new_friend_obj", f)
        ;
    }


    // 実行結果オブジェクト
    static class DBEDitActionResult extends ActionResult
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void onNextActivityStarted(final Activity activity)
        {
            UIUtil.longToast(activity, get("new_friend_name") + "さんを登録しました。");
        }
    }
    // NOTE: この内部クラスは，execメソッド中で匿名クラスとして定義することができない。
    // staticな内部クラスとして実装する必要がある。
    // 理由は，JavaのインナークラスとSerializableの仕様のため。
    // @see http://d.hatena.ne.jp/language_and_engineering/20120313/p1


}
