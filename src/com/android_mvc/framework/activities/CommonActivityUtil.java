package com.android_mvc.framework.activities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android_mvc.framework.common.BaseUtil;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.routing.Router;
import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.framework.task.AsyncTasksRunner;
import com.android_mvc.framework.task.RunnerFollower;
import com.android_mvc.framework.task.SequentialAsyncTask;
import com.android_mvc.framework.ui.menu.OptionMenuBuilder;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Map系＋非Map系のActivityの共通処理を詰め込むクラス
 * @author id:language_and_engineering
 *
 */
public class CommonActivityUtil<T extends IBaseActivity>
{

    // Activity中では，$という変数名で利用可能。


    // NOTE: 本クラス中では，T→Activity のキャストを許可する。
    // 疑似的な多重継承によってトラブル回避しているため。


    private Activity activity;

    // 前画面から受け取ったIntent内の情報
    private Bundle extras;
    private ValidationResult vres;
    private ActionResult ares;

    // オプションメニュー構築に関する情報
    private OptionMenuBuilder optionMenuBuilder;

    // オプションメニューの構築処理をすでに実行済みか
    private boolean menuBuiltFlag = false;


    // ---------------- Activityの初期化関連 ------------------


    /**
     * ActivityのonCreate時に呼び出される共通処理
     */
    public void onActivityCreated(T activity)
    {
        FWUtil.d("onActivityCreated開始");
        this.activity = (Activity)activity;

        // 前画面から運搬されたデータを引き継ぎ
        carryDataFromPreviousPage(activity);

        // UI描画の事前処理と本処理
        if( activity.requireProcBeforeUI() )
        {
            // 非同期タスクで処理を行ってからUI描画
            executeProcBeforeUIAndRender(activity);
        }
        else
        {
            // すぐに同期的にUI描画
            renderUI(activity);
        }

        FWUtil.d("onActivityCreated終了");
    }


    /**
     * 前画面からIntent経由で運搬されたデータを引き継ぎ，この画面で利用可能にする。
     */
    private void carryDataFromPreviousPage(T activity)
    {
        // Intentからデータ取得
        extras = ((Activity)activity).getIntent().getExtras();

        // なければ終了
        if( extras == null ) return;

        // バリデーション結果があれば格納
        if( extras.containsKey(Router.EXTRA_KEY_VALIDATION_RESULT))
        {
            vres = (ValidationResult) extras.getSerializable(Router.EXTRA_KEY_VALIDATION_RESULT);
        }

        // アクション実行結果があれば格納
        if( extras.containsKey(Router.EXTRA_KEY_ACTION_RESULT))
        {
            ares = (ActionResult) extras.getSerializable(Router.EXTRA_KEY_ACTION_RESULT);
        }

    }


    /**
     * UI構築前に必要な非同期処理を済ませてから，UIを構築する。
     */
    private void executeProcBeforeUIAndRender(final T activity) {

        new AsyncTasksRunner( new SequentialAsyncTask[]{
            new  SequentialAsyncTask(){
                @Override
                protected boolean main() {
                    // 事前処理を実行
                    activity.procAsyncBeforeUI();
                    return CONTINUE_TASKS;
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

        // メニューを定義
        this.optionMenuBuilder = activity.defineMenu();

        // 終わったらその時用の処理を呼び出し
        activity.afterViewDefined();
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


    // ---------------- 初期化後の画面内操作関連 ------------------


    /**
     * 前画面から受け取ったIntent内のデータを返す。
     * 何もデータがなければnullを返す。
     */
    public Bundle extras()
    {
        return extras;
            // NOTE: AC側ではBundleの持つアクセッサをそのまま流用できる。
            // TODO: RoutingTableもそうだが，Intentで渡される値に静的制約を設けたほうがよいか？
    }


    /**
     * バリデーション実行結果を返す。
     * 何もデータがなければnullを返す。
     */
    public ValidationResult getValidationResult()
    {
        return vres;
    }


    /**
     * アクション実行結果を返す。
     * 何もデータがなければnullを返す。
     */
    public ActionResult getActionResult()
    {
        return ares;
    }

    /**
     * バリデーション実行結果が存在するかどうか。
     */
    public boolean hasValidationResult()
    {
        return (vres != null);
    }


    /**
     * アクション実行結果が存在するかどうか。
     */
    public boolean hasActionResult()
    {
        return (ares != null);
    }


    /**
     * アクション実行結果に特定のキーが存在するかどうか。
     */
    public boolean actionResultHasKey(String key)
    {
        if( ! hasActionResult() ) return false;

        return ( ares.get(key) != null );
    }


    /**
     * 受け取ったIntentが特定のキーを含んでいるかどうかを返す。
     */
    public boolean intentHasKey(String key)
    {
        if( extras() != null )
        {
            return extras().containsKey( key );
        }
        else
        {
            return false;
        }
    }


    /**
     * getTextのように，文字列リソースを取得する。
     */
    public String _(int target_string_id)
    {
        // リソースから取得
        String target_string = activity
            .getResources()
            .getString( target_string_id )
        ;

        return target_string;
            // @see http://d.hatena.ne.jp/language_and_engineering/20110815/p1

            // TODO: Activity以外からも呼べるように
    }


    // ---------------- メニュー関連 ------------------


    /**
     * オプションメニューの描画
     */
    public Menu renderOptionMenuAsDescribed(Menu menu)
    {
        // 初回であれば
        if( ! menuBuiltFlag )
        {
            // ユーザがメニュー構築を指示していれば
            if( optionMenuBuilder != null )
            {
                menu = optionMenuBuilder.registerItemsInMenu(menu);
            }
        }

        // 次回からは実行させない
        menuBuiltFlag = true;

        return menu;
    }


    /**
     * オプションメニュー内の特定の項目が押されたイベントを処理
     */
    public void onOptionItemSelected(MenuItem item)
    {
        if( optionMenuBuilder != null )
        {
            optionMenuBuilder.onItemSelected(item);
        }
    }

}
