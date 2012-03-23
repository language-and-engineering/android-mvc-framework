package com.android_mvc.sample_project.activities.func_db;

import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.sample_project.controller.FuncDBController;
import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MCheckBox;
import com.android_mvc.framework.ui.view.MEditText;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;

/**
 * サンプルのDB登録アクティビティ。
 * @author id:language_and_engineering
 *
 */
public class DBEditActivity extends BaseNormalActivity {

    MLinearLayout layout1;
    MTextView tv1;
    MEditText et1;

    MLinearLayout layout2;
    MTextView tv2;
    MEditText et2;

    MLinearLayout layout3;
    MTextView tv3;
    MCheckBox chbox1;

    MButton button1;


    @Override
    public void defineContentView() {

        final DBEditActivity activity = this;

        // 入力フォームUIを動的に構築する。
        new UIBuilder(context)
        .add(

            layout1 = new MLinearLayout(context)
              .orientationHorizontal()
              .widthFillParent()
              .add(

                tv1 = new MTextView(context)
                  .text("名前：" )
                  .widthWrapContent()
                ,

                et1 = new MEditText(context)
                  .widthPx(300)

              )
            ,

            layout2 = new MLinearLayout(context)
              .orientationHorizontal()
              .widthFillParent()
              .add(

                  tv2 = new MTextView(context)
                    .text("年齢：" )
                    .widthWrapContent()
                  ,

                  et2 = new MEditText(context)
                    .widthPx(200)

              )
            ,

            layout3 = new MLinearLayout(context)
              .orientationHorizontal()
              .widthFillParent()
              .add(

                  tv3 = new MTextView(context)
                    .text("お気に入りにする：" )
                    .widthWrapContent()
                  ,

                  chbox1 = new MCheckBox(context)
                      .unChecked()

              )
            ,

            button1 = new MButton(context)
              .text("この内容でDB登録")
              .click(new OnClickListener(){

                  @Override
                  public void onClick(View v) {
                      FuncDBController.submit(activity);
                  }

              })
        )
      .display();

    }


    @Override
    public void afterViewDefined()
    {
        if( $.intentHasKey("hoge") )
        {
            // Intentから受け取った値をToastで表示
            UIUtil.longToast(this, $.extras().getString("hoge") );
        }
    }


    @Override
    public ActivityParams toParams() {
        // 入力された値をすべて回収
        return new ActivityParams()
            .add("名前", "name", et1.getText().toString() )
            .add("年齢", "age", et2.getText().toString() )
            .add("お気に入り", "favorite_flag", chbox1.isChecked())
        ;
    }

}