package com.android_mvc.sample_project.activities.main;

import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.controller.MainController;
import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.menu.OptionMenuBuilder;
import com.android_mvc.framework.ui.menu.OptionMenuDescription;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MTextView;

/**
 * サンプルのトップ画面。
 * @author id:language_and_engineering
 *
 */
public class TopActivity extends BaseNormalActivity
{

    MTextView tv1;
    MTextView tv2;
    MButton button1;
    MButton button2;
    MButton button3;
    MButton button4;


    @Override
    public void defineContentView() {
        final TopActivity activity = this;

        // ここに，画面上のUI部品の定義を記述する。

        new UIBuilder(context)
          .add(

              tv1 = new MTextView(context)
                .text("ここは，Top画面です。" )
                .widthWrapContent()
              ,

              tv2 = new MTextView(context)
                .text("このアプリの名称：" + $._(R.string.app_name) )
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
                            MainController.submit(activity, "EDIT_DB");
                        }

                  })
              ,

              button3 = new MButton(context)
                  .text("DB閲覧画面へ")
                  .click(new OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            MainController.submit(activity, "VIEW_DB");
                        }

                  })
                  ,

              button4 = new MButton(context)
                  .text("タブのサンプルへ")
                  .click(new OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            MainController.submit(activity, "TAB_SAMPLE");
                        }

                  })

        )
        .display();
    }


    @Override
    public OptionMenuBuilder defineMenu()
    {
        final TopActivity activity = this;

        // オプションメニューを構築
        return new OptionMenuBuilder(context)
            .add(
                new OptionMenuDescription()
                {
                    @Override
                    protected String displayText() {return "DB登録";}

                    @Override
                    protected void onSelected() {
                        // 画面遷移
                        MainController.submit(activity, "EDIT_DB");
                    }
                }
            )
            .add(
                new OptionMenuDescription()
                {
                    @Override
                    protected String displayText() {return "DB閲覧";}

                    @Override
                    protected void onSelected() {
                        // 画面遷移
                        MainController.submit(activity, "VIEW_DB");
                    }
                }
            )
        ;
    }


    @Override
    public void onBackPressed()
    {
        // 戻るボタンが押されたら終了
        moveTaskToBack(true);
    }


}
