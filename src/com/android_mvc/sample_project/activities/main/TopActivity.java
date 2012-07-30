package com.android_mvc.sample_project.activities.main;

import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.bat.SamplePeriodicService;
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

    // これらのメンバ宣言は，書かなくても動作する。
    MTextView tv1;
    MTextView tv2;
    MButton button1;
    MButton button2;
    MButton button3;
    MButton button4;
    MButton button5;
    MButton button6;
    MButton button7;
    MButton button8;
    MButton button9;
    MButton button10;


    @Override
    public void defineContentView() {
        final TopActivity activity = this;

        // ここに，画面上のUI部品の定義を記述する。

        new UIBuilder(context)
          .add(

              tv1 = new MTextView(context)
                .text("ここは，Top画面です。\n画面のレイアウトには，XMLもHTMLも使っていません。" )
                .widthWrapContent()
              ,

              tv2 = new MTextView(context)
                .text("このアプリの名称：" + $._(R.string.app_name) + "\n" )
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

              new MTextView(context)
                .text("\n友達情報をDBで管理するサンプル：" )
                .widthWrapContent()
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

              new MTextView(context)
                .text("\nGPS機能のサンプル：" )
                .widthWrapContent()
              ,

              button4 = new MButton(context)
                  .text("サービスを起動")
                  .click(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            new SamplePeriodicService().startResident(context);
                            UIUtil.longToast(activity, "サンプルのサービス常駐を開始しました。");
                        }
                  })
              ,

              button5 = new MButton(context)
                  .text("サービスの常駐を解除")
                  .click(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            SamplePeriodicService.stopResidentIfActive(context);
                            UIUtil.longToast(activity, "サンプルのサービス常駐を解除しました。");
                        }
                  })
              ,

              button6 = new MButton(context)
                  .text("GoogleMapのサンプルへ")
                  .click(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            MainController.submit(activity, "MAP_SAMPLE");
                        }
                  })
              ,

              new MTextView(context)
                  .text("\nレイアウト・UIのサンプル：" )
                  .widthWrapContent()
              ,

              button7 = new MButton(context)
                  .text("HTMLによる画面描画のサンプルへ")
                  .click(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            MainController.submit(activity, "HTML_SAMPLE");
                        }
                  })
              ,

              button8 = new MButton(context)
                  .text("jQuery Mobileによる画面描画のサンプルへ")
                  .click(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            MainController.submit(activity, "JQUERY_SAMPLE");
                        }
                  })
              ,

              button9 = new MButton(context)
                  .text("タブと通信のサンプルへ")
                  .click(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            MainController.submit(activity, "TAB_SAMPLE");
                        }
                  })
              ,

              button10 = new MButton(context)
                  .text("アニメーションのサンプル")
                  .click(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            MainController.submit(activity, "ANIM_SAMPLE");
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
        // 戻るキーが押されたら終了
        moveTaskToBack(true);
    }


}
