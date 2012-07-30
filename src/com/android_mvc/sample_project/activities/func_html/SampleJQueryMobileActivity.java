package com.android_mvc.sample_project.activities.func_html;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.ui.UIBuilder;

/**
 * サンプルのjQuery Mobile利用アクティビティ。
 * @author id:language_and_engineering
 *
 */
public class SampleJQueryMobileActivity extends BaseNormalActivity {

    @Override
    public void defineContentView() {

        new UIBuilder(context)
            .renderLocalHTML("sample_jquery_mobile.html")
        .display();

        // サンプルの出典：
        // http://d.hatena.ne.jp/language_and_engineering/20120717/CreateAndroidNativeAppsByJQueryMobile
    }

}