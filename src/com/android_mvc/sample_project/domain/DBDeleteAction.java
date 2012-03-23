package com.android_mvc.sample_project.domain;

import android.app.Activity;

import com.android_mvc.sample_project.activities.func_db.DBListActivity;
import com.android_mvc.sample_project.db.dao.FriendDAO;
import com.android_mvc.sample_project.db.entity.Friend;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.ui.UIUtil;

/**
 * DBからの削除に関するBL。
 * @author id:language_and_engineering
 *
 */
public class DBDeleteAction extends BaseAction
{

    private DBListActivity activity;
    private Long friend_id;


    public DBDeleteAction(DBListActivity activity, Long friend_id) {
        this.activity = activity;
        this.friend_id = friend_id;
    }


    // BL本体
    @Override
    public ActionResult exec()
    {
        Friend f = new FriendDAO(activity).findById(friend_id);
        String target_friend_name = f.getName();

        // DBからの削除を実行
        new FriendDAO(activity).deleteById(friend_id);

        // 実行結果を返す
        DBDeleteActionResult ares = new DBDeleteActionResult();
        ares.setRouteId("success");
        ares.friend_name = target_friend_name; // 名前だけ控えておく

        return ares;
    }


    // 実行結果オブジェクト
    static class DBDeleteActionResult extends ActionResult
    {
        private static final long serialVersionUID = 1L;

        protected String friend_name;

        @Override
        public void onNextActivityStarted(Activity activity)
        {
            UIUtil.longToast(activity, friend_name + "さんを削除しました。");
        }
    }

}
