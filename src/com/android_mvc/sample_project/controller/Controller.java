package com.android_mvc.sample_project.controller;


import java.util.HashMap;

import android.app.Activity;

import com.android_mvc.sample_project.activities.func_db.DBEditActivity;
import com.android_mvc.sample_project.activities.func_db.DBListActivity;
import com.android_mvc.sample_project.activities.installation.InstallAppActivity;
import com.android_mvc.sample_project.activities.installation.InstallCompletedActivity;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.sample_project.domain.DBEditAction;
import com.android_mvc.framework.controller.BaseController;
import com.android_mvc.framework.controller.Router;


/**
 * MVCのコントローラ層に当たるクラス。
 * ビューから渡された値の検証や，BLの呼び出し，画面遷移の制御などを行う。
 * もし肥大化したら，別クラスに細分化する。
 * @author id:language_and_engineering
 *
 */
public class Controller extends BaseController
{
    // 着想は下記の記事を参照。
    // @see http://d.hatena.ne.jp/language_and_engineering/20120213/p1


    // 遷移元となるActivityごとに，submit()をオーバーロードする。


    /**
     * インストール画面からの遷移時
     */
    public void submit(InstallAppActivity installAppActivity, boolean installExecutedFlag)
    {
        // インストールをスキップしたかどうか
        if( installExecutedFlag )
        {
            // インストール完了画面へ
            Router.go(installAppActivity, InstallCompletedActivity.class);
        }
        else
        {
            // トップ画面へ
            Router.go(installAppActivity, TopActivity.class);
        }
    }


    /**
     * DB登録画面からの遷移時
     */
    @SuppressWarnings("serial")
    public void submit(final DBEditActivity activity)
    {
        new ControlFlowDetail<DBEditActivity>( activity )
            .describeValidation( new ValidationExecutor(){
                @Override
                protected void validate()
                {
                    // バリデーション処理
                    validation_result = GateChecker.validate( activity );
                }
            })
            .describeBL( new BLExecutor(){
                @Override
                protected void doAction()
                {
                    // BL
                    action_result = new DBEditAction( activity ).exec();
                }
            })
            .onValidationFailed( DBEditActivity.class )
            .onBLExecuted(
                new HashMap<String, Class<? extends Activity>>(){{
                    put( "success", DBListActivity.class );
                }}
            )
            .startControl();
        ;

    }


    /**
     * DB参照画面からの遷移時
     */
    public void submit(DBListActivity activity) {
        Router.go(activity, TopActivity.class);
    }


}
