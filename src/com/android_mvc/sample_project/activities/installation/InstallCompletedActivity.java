package com.android_mvc.sample_project.activities.installation;

import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.sample_project.controller.MainController;
import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MTextView;

/**
 * サンプルのインストール完了画面。
 * @author id:language_and_engineering
 *
 */
public class InstallCompletedActivity extends BaseNormalActivity
{
    MButton button1;
    MTextView tv1;

    @Override
    public void defineContentView() {
        final InstallCompletedActivity activity = this;

        // ここに，画面上のUI部品の定義を記述する。
        new UIBuilder(context)
            .add(
               tv1 = new MTextView(context)
                   .text("おめでとうございます。インストールが完了しました。" )
                   .widthWrapContent()
               ,
               button1 = new MButton(context)
                   .widthFillParent()
                   .text("トップ画面へ" )
                   .click(new OnClickListener(){
                       @Override
                       public void onClick(View v) {
                           MainController.submit(activity);
                       }
                   })
            )
        .display();

    }


    @Override
    public void onBackPressed()
    {
        // 戻るボタンで戻させない
    }

}
