package com.android_mvc.framework.activities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android_mvc.framework.common.BaseUtil;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.task.AsyncTasksRunner;
import com.android_mvc.framework.task.RunnerFollower;
import com.android_mvc.framework.task.SequentialAsyncTask;


import android.app.Activity;

/**
 * Map系＋非Map系のActivityの共通処理を詰め込むクラス
 * @author id:language_and_engineering
 *
 */
public class CommonActivityUtil<T extends IBaseActivity>{

    /**
     * ActivityのonCreate時に呼び出される共通処理
     */
    public void onActivityCreated(final T activity)
    {
        FWUtil.d("onActivityCreated開始");

        if( activity.requireProcBeforeUI() )
        {
            // 非同期タスクで処理を行ってからUI描画
            executeProcBeforeUI(activity);
        }
        else
        {
            renderUI(activity);
        }

        FWUtil.d("onActivityCreated終了");
    }


    /**
     * UI構築前に必要な非同期処理を済ませてから，UIを構築する。
     * @param activity
     */
    private void executeProcBeforeUI(final T activity) {

        new AsyncTasksRunner( new SequentialAsyncTask[]{
            new  SequentialAsyncTask(){
                @Override
                protected boolean main() {
                    // 事前処理を実行
                    activity.procBeforeUI();
                    return true;
                }
            }
        })
        .withSimpleDialog("読み込み中・・・", (Activity)activity)
        .whenAllTasksCompleted(new RunnerFollower(){
            @Override
            protected void exec() {
                // UIスレッド上でUIを構築
                renderUI(activity);
            }})
        .begin();

    }


    /**
     * UIを構築
     */
    private void renderUI(T activity)
    {
        // XMLが存在すれば，レイアウトを描画
        render_xml( (Activity) activity );

        // UI部品を定義
        activity.defineContentView();
    }


    /**
     * 該当アクティビティに対応するレイアウトXMLを検知して描画する
     */
    private void render_xml( Activity activity )
    {
        // NOTE: 残念ながら，layoutフォルダの内部は階層化できない。
        // ソート時に把握しやすくなるようなネーミングを心がけること。
        // http://ameblo.jp/m-ext/entry-10872776112.html


        // クラス名の末尾の「Activity」を除去
        String activity_class_name = activity.getClass().getSimpleName();
        Pattern reg_pattern = Pattern.compile( "Activity$" );
        Matcher reg_matcher = reg_pattern.matcher( activity_class_name );
        String activity_basic_name = reg_matcher.replaceFirst("");

        // クラス名の基本部分をパスカル形式（PascalCase）からスネーク形式（snake_case）に変換
        StringBuilder sb = new StringBuilder();
        int class_name_length = activity_basic_name.length();
        boolean previous_char_was_upper = false; // 1つ前の文字が大文字だったかどうか
        for( int i = 0; i < class_name_length; i ++ )
        {
            Character c = activity_basic_name.charAt(i);

            // 大文字か
            if( Character.isUpperCase(c))
            {
                // 直前が大文字でなければ，アンダーバーを追記
                if( ( i > 0 ) && ( ! previous_char_was_upper ) )
                {
                    sb.append("_");
                }

                // 小文字に変換
                c = Character.toLowerCase(c);
                previous_char_was_upper = true;
            }
            else
            {
                previous_char_was_upper = false;
            }

            // 追記
            sb.append(c);
        }

        // レイアウトXMLのフィールド名が完成
        String xml_base_name = sb.toString();
        BaseUtil.d( "xml name is " + xml_base_name);

        // この名称のレイアウトXMLのリソースIDを取得
        int xml_resource_id = activity
            .getResources()
            .getIdentifier(
                xml_base_name,
                "layout",
                activity.getPackageName()
        );

        // このXMLが存在すればレイアウトを描画
        if( xml_resource_id != 0 )
        {
            activity.setContentView( xml_resource_id );
        }
        else
        {
            // NOTE: アクティビティに対応したXMLを作らない場合もある
            BaseUtil.w("xml not found!");
        }

        return;
    }

}
