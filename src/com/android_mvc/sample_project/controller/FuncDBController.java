package com.android_mvc.sample_project.controller;


import com.android_mvc.sample_project.activities.func_db.DBEditActivity;
import com.android_mvc.sample_project.activities.func_db.DBListActivity;
import com.android_mvc.sample_project.activities.func_db.SampleTabHostActivity;
import com.android_mvc.sample_project.activities.func_net.HttpNetActivity;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.sample_project.domain.DBDeleteAction;
import com.android_mvc.sample_project.domain.DBEditAction;
import com.android_mvc.sample_project.domain.DBUpdateAction;
import com.android_mvc.framework.controller.BaseController;
import com.android_mvc.framework.controller.ControlFlowDetail;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BLExecutor;
import com.android_mvc.framework.controller.routing.Router;
import com.android_mvc.framework.controller.routing.RoutingTable;
import com.android_mvc.framework.controller.routing.TabContentMapping;
import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.framework.controller.validation.ValidationExecutor;


/**
 * DB操作系画面のコントローラ。
 * @author id:language_and_engineering
 *
 */
public class FuncDBController extends BaseController
{

    /**
     * DB登録画面からの遷移時
     */
    public static void submit(final DBEditActivity activity)
    {
        new ControlFlowDetail<DBEditActivity>( activity )
            .setValidation( new ValidationExecutor(){
                @Override
                public ValidationResult doValidate()
                {
                    // バリデーション処理
                    return new FuncDBValidation().validate( activity );
                }

                @Override
                public void onValidationFailed()
                {
                    showErrMessages();

                    // バリデーション失敗時の遷移先
                    //goOnValidationFailed( DBEditActivity.class );
                    stayInThisPage();
                }
            })
            .setBL( new BLExecutor(){
                @Override
                public ActionResult doAction()
                {
                    // BL
                    return new DBEditAction( activity ).exec();
                }
            })
            .onBLExecuted(
                // BL実行後の遷移先の一覧
                new RoutingTable().map("success", DBListActivity.class )

                // onBLExecutedにこれを渡せば，BLの実行結果にかかわらず画面遷移を常に抑止。
                //STAY_THIS_PAGE_ALWAYS

                // BL実行結果が特定の状況のときのみ，画面遷移を抑止することも可能。
                //new RoutingTable().map("success", STAY_THIS_PAGE )

            )
            .startControl();
        ;

    }


    /**
     * DB参照画面からの遷移時
     */
    public static void submit(final DBListActivity activity, String action_type, final Long friend_id)
    {
        if( "BACK_TO_TOP".equals(action_type) )
        {
            // TOPに戻る
            Router.go(activity, TopActivity.class);
        }
        else
        if( "UPDATE_FAVORITE_FLAG".equals(action_type) )
        {
            // DB更新
            new ControlFlowDetail<DBListActivity>( activity )
                .setBL( new BLExecutor(){
                    @Override
                    public ActionResult doAction()
                    {
                        return new DBUpdateAction( activity, friend_id ).exec();
                    }
                })
                .onBLExecuted(
                    new RoutingTable().map("success", DBListActivity.class )
                )
                .startControl();
            ;
        }
        else
        if( "DELETE_FRIEND".equals(action_type) )
        {
            // DBから削除
            new ControlFlowDetail<DBListActivity>( activity )
                .setBL( new BLExecutor(){
                    @Override
                    public ActionResult doAction()
                    {
                        return new DBDeleteAction( activity, friend_id ).exec();
                    }
                })
                .onBLExecuted(
                    new RoutingTable().map("success", DBListActivity.class )
                )
                .startControl();
            ;
        }

    }


    /**
     * タブ親サンプル画面から呼び出される子画面のリスト
     */
    public static TabContentMapping getChildActivities(SampleTabHostActivity activity) {
        // タブのタグ文字列に対応するアクティビティを指定する。
        return new TabContentMapping()
            .add( "TAB_VIEW_DB", DBListActivity.class )
            .add( "TAB_EDIT_DB", DBEditActivity.class )
            .add( "TAB_FUNC_NET", HttpNetActivity.class )
        ;
    }


}
