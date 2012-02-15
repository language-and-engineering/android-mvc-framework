package com.android_mvc.framework.controller;

import java.util.HashMap;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.task.AsyncTasksRunner;
import com.android_mvc.framework.task.RunnerFollower;
import com.android_mvc.framework.task.SequentialAsyncTask;

import android.app.Activity;

/**
 * コントローラの基底クラス。
 * @author id:language_and_engineering
 *
 */
public class BaseController
{

    /**
     * 一つのコントロールフロー内でバリデーション操作を実行するクラス
     */
    protected abstract class ValidationExecutor
    {
        protected abstract void validate();

        public GateValidationResult validation_result;
    }


    /**
     * 一つのコントロールフロー内でBLを実行するクラス
     */
    protected abstract class BLExecutor
    {
        protected abstract void doAction();

        public ActionResult action_result;

    }
        // NOTE: これらのインナークラスは，親クラスのジェネリクスで解決できなかった問題を
        // なんとかするために，クロージャの代わりとして仕方なく提供されている。


    /**
     * 一つのコントロールフローの詳細記述
     */
    protected class ControlFlowDetail<ActivityClass>
    {

        protected ActivityClass from_activity;
        private ValidationExecutor validation_executor;
        private BLExecutor bl_executor;
        private Class<? extends Activity> validation_failed_activity;
        private HashMap<String, Class<? extends Activity>> routingTable;
        private RunnerFollower follower;


        /**
         * 初期化
         */
        public ControlFlowDetail(ActivityClass fromActivity)
        {
            this.from_activity = fromActivity;
        }


        /**
         * バリデーション処理の詳細をセット
         */
        public ControlFlowDetail<ActivityClass> describeValidation(
            ValidationExecutor validationExecutor
        )
        {
            this.validation_executor = validationExecutor;
            return this;
        }


        /**
         * バリデーション失敗時の遷移先をセット
         */
        public ControlFlowDetail<ActivityClass> onValidationFailed(
            Class<? extends Activity> validation_failed_activity
        )
        {
            this.validation_failed_activity = validation_failed_activity;
            return this;
        }


        /**
         * BL実行の詳細をセット
         */
        public ControlFlowDetail<ActivityClass> describeBL(
            BLExecutor blExecutor
        )
        {
            this.bl_executor = blExecutor;
            return this;
        }


        /**
         * BL実行完了時のルーティングテーブルをセット
         */
        public ControlFlowDetail<ActivityClass> onBLExecuted(
            HashMap<String, Class<? extends Activity>> routingTable
        )
        {
            this.routingTable = routingTable;
            return this;

        }


        /**
         * 完了時のコールバックをセット
         */
        public ControlFlowDetail<ActivityClass> onCompleted( RunnerFollower follower )
        {
            this.follower = follower;
            return this;
        }


        /**
         * 制御フローを実行
         */
        public void startControl()
        {
            new AsyncTasksRunner( new SequentialAsyncTask[]{

                // （UIで済まなかった）バリデーションを行なう非同期タスク。
                // Webでいうサーバサイド・バリデーションに相当
                new  SequentialAsyncTask(){
                    public boolean main(){
                        FWUtil.d( "１つ目のタスクが実行開始＠main");

                        // Activityのparamsをバリデート
                        validation_executor.validate();
                        storeData( "validation_result", validation_executor.validation_result );

                        // バリデ結果の是非に関らずルーティングへ進むので必ずtrue
                        FWUtil.d( "１つ目のタスクが実行終了＠main");
                        return true;
                    }
                }
                ,

                // もし可能ならBLを行なう非同期タスク。
                // DB操作やNW通信などを想定。
                new SequentialAsyncTask(){
                    public boolean main(){
                        FWUtil.d( "２つ目のタスクが実行開始＠main");

                        // 前のバリデーション処理の結果を取りだす
                        GateValidationResult vres = (GateValidationResult)getDataFromRunner("validation_result");

                        // もしバリデ結果からしてBLを実行してよいのであれば
                        if( vres.permitsExecitionOfBL() )
                        {
                            // BLを実行
                            bl_executor.doAction();
                            storeData( "action_result", bl_executor.action_result );
                        }

                        // BLの実行結果の是非に関らずルーティングはするので必ずtrue
                        return true;
                    }
                }
                ,

                //  ルーティングを行なう非同期タスク
                new SequentialAsyncTask(){
                    public boolean main(){

                        // バリデーション処理の結果を取りだす
                        GateValidationResult vres = (GateValidationResult)getDataFromRunner("validation_result");

                        if( vres.didNotExecuteBL() )
                        {
                            // バリデーションではじかれた場合のルートへ
                            Router.go( (Activity)from_activity, validation_failed_activity, vres );
                        }
                        else
                        if( routingTable != null ) // ルーティングテーブルを渡さなければ，画面遷移しない。
                        {
                            // BLが実行された場合
                            ActionResult ares = (ActionResult)getDataFromRunner("action_result");

                            // BL実行結果に応じて遷移先を分岐
                            Router.switchByActionResult( (Activity)from_activity, ares, routingTable );
                        }

                        return true;
                    }
                }
            })
            .withSimpleDialog("処理中・・・", (Activity)from_activity) // 全非同期タスクが終了するまでダイアログを出す
            .whenAllTasksCompleted(follower) // 完了時のコールバックをUIスレッド上で実行
            .begin(); // これら全ての処理を開始

        }

    }
}
