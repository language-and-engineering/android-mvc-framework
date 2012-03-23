package com.android_mvc.framework.controller;

import android.app.Activity;

import com.android_mvc.framework.activities.IBaseActivity;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BLExecutor;
import com.android_mvc.framework.controller.routing.Router;
import com.android_mvc.framework.controller.routing.RoutingTable;
import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.framework.controller.validation.ValidationExecutor;
import com.android_mvc.framework.task.AsyncTasksRunner;
import com.android_mvc.framework.task.RunnerFollower;
import com.android_mvc.framework.task.SequentialAsyncTask;


/**
 * 一つのコントロールフローの詳細記述
 */
public class ControlFlowDetail<T_ActivityClass>
{
    // NOTE: T_ActivityClass -> Activityへのキャストを許可する。

    protected T_ActivityClass from_activity;
    private ValidationExecutor validation_executor;
    private BLExecutor bl_executor;
    private RoutingTable routingTable;
    protected ActionResult action_result;



    /**
     * 初期化
     */
    public ControlFlowDetail(T_ActivityClass fromActivity)
    {
        this.from_activity = fromActivity;
    }


    /**
     * バリデーション処理の詳細をセット
     */
    public ControlFlowDetail<T_ActivityClass> setValidation(
        ValidationExecutor validationExecutor
    )
    {
        this.validation_executor = validationExecutor;
        return this;
    }



    /**
     * BL実行の詳細をセット
     */
    public ControlFlowDetail<T_ActivityClass> setBL(
        BLExecutor blExecutor
    )
    {
        this.bl_executor = blExecutor;
        return this;
    }


    /**
     * BL実行完了時のルーティングテーブルをセット。
     * 引数によっては，画面遷移を抑止することも可能。
     */
    public ControlFlowDetail<T_ActivityClass> onBLExecuted( RoutingTable routingTable )
    {
        this.routingTable = routingTable;
        return this;

    }


    /**
     * 制御フローを実行。
     * このクラスの要であり，コントローラ層の要であり，このフレームワークの要でもある重要な部分。
     */
    public void startControl()
    {
        // ActionResult格納用に
        final ControlFlowDetail<T_ActivityClass> parentFlow = this;

        // 一連の制御フローを開始
        new AsyncTasksRunner( new SequentialAsyncTask[]{

            // バリデーションを行なう非同期タスク。
            new  SequentialAsyncTask(){
                public boolean main(){
                    ValidationResult vres;


                    // バリデータがセットされていれば
                    if( validation_executor != null )
                    {
                        // Activityのparamsをバリデート
                        validation_executor.execAndStoreValidationResult();
                        vres = validation_executor.validation_result;
                    }
                    else
                    {
                        // バリデを実行しないので成功とみなす
                        vres = new ValidationResult().setSuccess();
                    }
                    storeData( "validation_result", vres );


                    // バリデの成否に応じて動作を切り替え
                    if( vres.isSuccess() )
                    {
                        FWUtil.d("バリデーションが成功しました。BL実行フェーズに進みます。");

                        // BL実行フェーズに進む
                        return CONTINUE_TASKS;
                    }
                    else
                    {
                        FWUtil.d("バリデーションが失敗しました。BLの実行はキャンセルされます。");

                        // バリデ失敗時の処理を実行
                        validation_executor.from_activity = (Activity)from_activity;
                        validation_executor.onValidationFailed();

                        // 次以降のタスクはキャンセル
                        return BREAK_TASKS;
                    }

                }
            }
            ,

            // もし可能ならBLを行なう非同期タスク。
            // DB操作やNW通信などを想定。
            new SequentialAsyncTask(){
                public boolean main(){

                    // 前のバリデーション処理の結果を取りだす
                    ValidationResult vres = (ValidationResult)getDataFromRunner("validation_result");

                    // もしバリデ結果からしてBLを実行してよいのであれば
                    if( vres.permitsExecitionOfBL() )
                    {
                        FWUtil.d("BLの実行を開始します。");

                        // BLを実行
                        bl_executor.execAndStoreActionResult();

                        // BLの結果を格納
                        storeData( "action_result", bl_executor.action_result );
                        parentFlow.action_result = bl_executor.action_result;

                        FWUtil.d("ActionResultの格納が完了しました。");
                    }

                    // BLの実行結果の是非に関らずルーティングはするので必ずtrue
                    return CONTINUE_TASKS;
                }
            }
            ,

            //  ルーティングを行なう非同期タスク
            new SequentialAsyncTask(){
                public boolean main(){

                    // ルーティングが渡されているか
                    if( routingTable != null )
                    {
                        // BLが実行された場合
                        ActionResult ares = (ActionResult)getDataFromRunner("action_result");

                        // BL実行結果に応じて遷移先を分岐
                        Router.switchByActionResult( (Activity)from_activity, ares, routingTable );
                    }
                    else
                    {
                         // ルーティングテーブルを渡さなければ，画面遷移しない。
                    }

                    return CONTINUE_TASKS;
                }
            }
        })
        .withSimpleDialog("処理中・・・", (Activity)from_activity) // 全非同期タスクが終了するまでダイアログを出す
        .whenAllTasksCompleted(new RunnerFollower(){
            @Override
            protected void exec() {
                ActionResult ares = parentFlow.action_result;

                // BLが実行された場合
                if( ares != null )
                {
                    // BLを非同期で実行完了後に，UIスレッド上で動く処理を実行
                    ((IBaseActivity)from_activity).afterBLExecuted(ares);

                    // NOTE: Intentで画面遷移する場合は，Intent内にActionResultが自動的に格納されるので，
                    // 遷移先の画面でUI操作処理を記述すればよい。
                    // しかし，BL実行完了に画面遷移が伴わない場合は，同一アクティビティ内でBL実行完了後に
                    // UIスレッド上でUI操作する処理を記述できる場所が必要。それがこれ。
                }
            }
        })
        .begin(); // これら全ての処理を開始

        // TODO: 「処理中」の文言表示等をカスタマイズ可能に
    }

}
