package com.android_mvc.sample_project.controller;


import com.android_mvc.sample_project.activities.func_net.HttpNetActivity;
import com.android_mvc.sample_project.domain.HttpNetAction;
import com.android_mvc.framework.controller.BaseController;
import com.android_mvc.framework.controller.ControlFlowDetail;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BLExecutor;
import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.framework.controller.validation.ValidationExecutor;


/**
 * 通信系画面のコントローラ。
 * @author id:language_and_engineering
 *
 */
public class FuncNetController extends BaseController
{

    /**
     * HTTP通信画面からの遷移時
     */
    public static void submit(final HttpNetActivity activity)
    {
        new ControlFlowDetail<HttpNetActivity>( activity )
            .setValidation( new ValidationExecutor(){
                @Override
                public ValidationResult doValidate()
                {
                    // バリデーション処理
                    return new FuncNetValidation().validate( activity );
                }

                @Override
                public void onValidationFailed()
                {
                    showErrMessages();

                    // バリデーション失敗時の遷移先
                    stayInThisPage();
                }
            })
            .setBL( new BLExecutor(){
                @Override
                public ActionResult doAction()
                {
                    // BL
                    return new HttpNetAction( activity ).exec();
                }
            })
            .onBLExecuted(
                // BL実行後の遷移先
                STAY_THIS_PAGE_ALWAYS
            )
            .startControl();
        ;

    }


}
