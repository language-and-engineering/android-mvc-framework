package com.android_mvc.sample_project.activities.func_net;

import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.sample_project.controller.FuncNetController;
import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.net.HttpPostResponse;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MEditText;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;

/**
 * サンプルのHTTP通信アクティビティ。
 * @author id:language_and_engineering
 *
 */
public class HttpNetActivity extends BaseNormalActivity {

    MLinearLayout layout1;
    MTextView tv1;
    MTextView tv2;
    MEditText et1;
    MButton button1;


    @Override
    public void defineContentView() {

        final HttpNetActivity activity = this;

        // 入力フォームUIを動的に構築する。
        new UIBuilder(context)
        .add(

            layout1 = new MLinearLayout(context)
              .orientationHorizontal()
              .widthFillParent()
              .add(

                tv1 = new MTextView(context)
                  .text("対象URL：" )
                  .widthWrapContent()
                ,

                et1 = new MEditText(context)
                  .widthPx(400)
                  .text("http://www.yahoo.co.jp/")

              )
            ,

            button1 = new MButton(context)
                .text("このURLにアクセス")
                .click(new OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        FuncNetController.submit(activity);
                    }

                })
            ,

            tv2 = new MTextView(context)
                .text("※ここに通信結果が表示されます。" )
                .widthFillParent()
                .heightWrapContent()
        )
      .display();

    }


    @Override
    public ActivityParams toParams() {
        // 入力された値をすべて回収
        return new ActivityParams()
            .add("対象URL", "http_url", et1.getText().toString() )
        ;
    }


    @Override
    public void afterBLExecuted(ActionResult ares)
    {
        UIUtil.longToast(this, "通信処理が完了しました。");

        // 通信の結果を表示
        HttpPostResponse response = (HttpPostResponse)ares.get("http_response");
        if( response.isSuccess() )
        {
            tv2.setText( response.getText() );
        }
        else
        {
            tv2.setText( response.getErrMsg() );
        }
    }

}