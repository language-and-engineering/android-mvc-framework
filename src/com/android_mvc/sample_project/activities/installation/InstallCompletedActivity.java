package com.android_mvc.sample_project.activities.installation;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.framework.activities.BaseNonMapActivity;
import com.android_mvc.framework.controller.ActivityParams;
import com.android_mvc.framework.controller.Router;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MTextView;

/**
 * サンプルのインストール完了画面。
 * @author id:language_and_engineering
 *
 */
public class InstallCompletedActivity extends BaseNonMapActivity
{
    MButton button1;
    MTextView tv1;

    @Override
    public void defineContentView() {
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
                           Router.go(context, TopActivity.class);
                       }
                   })
            )
        .display();

    }

    @Override
    public ActivityParams toParams() {
        return null;
    }


    // 戻るボタンで戻させない
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK )
        {
            //
            return true;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }
    }

}
