package com.android_mvc.sample_project.activities.func_html;

import com.android_mvc.sample_project.common.Util;
import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.etc.BaseJSAPI;

/**
 * サンプルのHTML描画アクティビティ。
 * @author id:language_and_engineering
 *
 */
public class SampleHtmlActivity extends BaseNormalActivity {

    @Override
    public void defineContentView() {

        new UIBuilder(context)
            .renderLocalHTML("sample_hoge.html")
            .setJSAPI("droid", new SampleJSAPI())
        .display();

    }


    // JavaScript API
    protected class SampleJSAPI extends BaseJSAPI{
        // 任意の文字列をログ出力するメソッド
        public void x( String s )
        {
            Util.d(s);
        }

        // 文字列を返すメソッド
        public String y()
        {
            return "fuga";
        }
    }

}