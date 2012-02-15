package com.android_mvc.sample_project.activities.main;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.sample_project.activities.func_db.DBEditActivity;
import com.android_mvc.framework.activities.BaseNonMapActivity;
import com.android_mvc.framework.controller.ActivityParams;
import com.android_mvc.framework.controller.Router;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MTextView;

/**
 * サンプルのトップ画面。
 * @author id:language_and_engineering
 *
 */
public class TopActivity extends BaseNonMapActivity
{

    MTextView tv1;
    MButton button1;
    MButton button2;


    @Override
    public void defineContentView() {
        // ここに，画面上のUI部品の定義を記述する。

        new UIBuilder(context)
          .add(

              tv1 = new MTextView(context)
                .text("ここは，Top画面です。" )
                .widthWrapContent()
              ,

              button1 = new MButton(context)
                .text("Toastを表示")
                .click(new OnClickListener(){

                      @Override
                      public void onClick(View v) {
                          UIUtil.longToast(context, "Toastです。");
                      }

                })
                ,

              button2 = new MButton(context)
                  .text("DB登録画面へ")
                  .click(new OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Router.go(context, DBEditActivity.class);
                        }

                  })
          )
        .display();
    }


    @Override
    public ActivityParams toParams() {
        return null;
    }


    // 戻るボタンが押されたときに終了させたい
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK )
        {
            moveTaskToBack(true);
            return true;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }
    }


}
