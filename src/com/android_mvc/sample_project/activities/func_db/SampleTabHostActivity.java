package com.android_mvc.sample_project.activities.func_db;

import com.android_mvc.framework.activities.base.BaseTabHostActivity;
import com.android_mvc.framework.ui.tab.TabDescription;
import com.android_mvc.framework.ui.tab.TabHostBuilder;
import com.android_mvc.sample_project.controller.FuncDBController;


/**
 * 親タブのサンプル画面。
 * @author id:language_and_engineering
 *
 */
public class SampleTabHostActivity extends BaseTabHostActivity
{

    @Override
    public void defineContentView() {

        // タブの定義を記述する。
        new TabHostBuilder(context)
            .setChildActivities( FuncDBController.getChildActivities(this) )
            .add(
                new TabDescription("TAB_EDIT_DB")
                    .text("DB登録")
                    .icon(android.R.drawable.ic_menu_add)
                ,

                new TabDescription("TAB_VIEW_DB")
                    .text("DB閲覧")
                    .icon(android.R.drawable.ic_menu_agenda)
                ,

                new TabDescription("TAB_FUNC_NET")
                    .text("通信")
                    .noIcon()

            )
            .display()
        ;

    }

}
