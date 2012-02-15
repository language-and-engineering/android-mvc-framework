package com.android_mvc.sample_project.activities.func_db;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.Controller;
import com.android_mvc.sample_project.db.dao.FriendDAO;
import com.android_mvc.sample_project.db.entity.Friend;
import com.android_mvc.framework.activities.BaseNonMapActivity;
import com.android_mvc.framework.controller.ActivityParams;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;

/**
 * サンプルのDB参照アクティビティ。
 * @author id:language_and_engineering
 *
 */
public class DBListActivity extends BaseNonMapActivity {

    MLinearLayout layout1;
    MTextView tv1;


    // 全友達のリスト
    ArrayList<Friend> friends;


    @Override
    public boolean requireProcBeforeUI(){
        // UI構築前に処理を要求する
        return true;
    }


    // UI構築前に別スレッドで実行される処理
    @Override
    public void procBeforeUI(){
        //
        Util.d("UI構築前に実行される処理です。");

        // 全FriendをDBからロード
        friends = new FriendDAO(this).findAll();
    }


    @Override
    public void defineContentView() {
        // まず親レイアウトを定義
        new UIBuilder(context)
        .add(
            layout1 = new MLinearLayout(context)
              .orientationVertical()
              .widthFillParent()
              .heightWrapContent()
              .add(

                tv1 = new MTextView(context)
                  .text("ここにDBの中身が列挙されます。" )
                  .widthWrapContent()
                  .paddingPx(10)

              )
        )
        .display();


        // レイアウト内に動的に全友達の情報を表示。Adapterは不要。
        for( final Friend f : friends )
        {
        	// ※↑friendをfinal宣言してるのは，Toastの中から参照する事が目的

            layout1.add(
                // 水平方向のレイアウトを１個追加
                new MLinearLayout(context)
                    .orientationHorizontal()
                    .widthFillParent()
                    .heightWrapContent()
                    .paddingPx(10)
                    .add(

                        new MTextView(context)
                            .text( f.getDescription() ) // この友達の説明を取得
                            .widthWrapContent()
                        ,

                        new MButton(context)
                            .text("名前を表示")
                            .click(new OnClickListener(){

                                  @Override
                                  public void onClick(View v) {
                                      UIUtil.longToast(context, f.getName() + "さんのボタンが押されました。");
                                  }

                            })
                    )
            );
        }

        // 戻るボタン
        final DBListActivity activity = this;
        layout1.add(
            new MButton(context)
              .text("トップに戻る")
              .click(new OnClickListener(){

                  @Override
                  public void onClick(View v) {
                      new Controller().submit(activity);
                  }

              })
        );

        // 描画
        layout1.inflateInside();

    }


    @Override
    public ActivityParams toParams() {
        return null;
    }


}