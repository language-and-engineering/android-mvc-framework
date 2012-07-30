package com.android_mvc.framework.ui.anim;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.ui.anim.desc.AnimationDescription;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;


/**
 * 複数のAnimationを順番に実行するためのランナー。
 * @author id:language_and_engineering
 *
 */
public class SequentialAnimationsRunner {

    // @see http://d.hatena.ne.jp/language_and_engineering/20120416/AndroidAnimationSetSequentialDSL

    // アニメーションを走らせる画面
    private Activity target_activity = null;

    // アニメーション詳細設定たち
    private List<AnimationDescription> descriptions = new ArrayList<AnimationDescription>();

    // 全終了後のリスナ
    private AnimationsFinishListener animationsFinishListener = null;

    // アニメーション適用対象Viewたち
    private ArrayList<View> current_target_views = new ArrayList<View>();

    // 現在取り扱い中の詳細設定のインデックス
    private int current_description_cursor = 0;

    // １スレッドを使いまわすサービス
    private ExecutorService exService = null;

    // 全アニメーションを実行途中であるかどうか（簡易ロック用）
    private boolean executing_flag;

    private RelativeLayout root_layout;


    // ------- 初期化処理 --------


    /**
     * 初期化
     */
    public SequentialAnimationsRunner(Activity activity)
    {
        this.target_activity = activity;
    }


    /**
     * アニメーション詳細設定たちを追加。
     * 可変長引数で何個でも可能。
     */
    public SequentialAnimationsRunner add( AnimationDescription...descs )
    {
        for( AnimationDescription desc : descs )
        {
            descriptions.add( desc );

            //FWUtil.d(descriptions.size() + "個目のdescriptionがaddされました。");
        }
        return this;
    }


    /**
     * アニメーションの舞台となるルートレイアウトを指定する。
     */
    public SequentialAnimationsRunner rootLayoutIs(RelativeLayout layout) {
        this.root_layout = layout;

        return this;
    }


    /**
     * アニメーション終了時の挙動を設定。
     */
    public SequentialAnimationsRunner onFinish(AnimationsFinishListener animationsFinishListener)
    {
        this.animationsFinishListener = animationsFinishListener;

        //FWUtil.d("runnerにanimationsFinishListenerがセットされました。");
        return this;
    }


    /**
     * アニメのタイトルを記述
     */
    public SequentialAnimationsRunner title(String s) {
        // 何もせず
        return this;
    }


    // ------- 全Descriptionのスキャン処理 --------


    /**
     * 全アニメーションを開始する。
     */
    public void start()
    {
        // 開始済み？
        if( executionAlreadyStarted() )
        {
            // ランナーのインスタンス単位で排他する。
            FWUtil.d("このインスタンスのアニメーションは既に開始済みです。");
        }
        else
        {
            executing_flag = true;

            // 全部実行開始
            execAllDescriptions();
        }
    }


    /**
     * 登録された全詳細を実行開始
     */
    private void execAllDescriptions()
    {
        // NOTE: 1個以上の追加は前提とする

        // アニメーションはパフォーマンスを気にすべき処理なので
        // シングルスレッドを使いまわして毎回のスレッド生成のオーバーヘッドを省く
        exService = Executors.newSingleThreadExecutor();
            // @see http://www.techscore.com/tech/Java/JavaSE/Thread/7-2/
            // https://gist.github.com/1764033

        // カーソルを先頭にセット
        current_description_cursor = 0;
        //FWUtil.d("最初の詳細を実行開始します。");

        // 開始
        execCurrentDescription();
    }


    /**
     * 現在のカーソルが指し示すアニメーション詳細設定を実行する。
     */
    private void execCurrentDescription()
    {
        // 現在のDescriptionを取得
        AnimationDescription desc = descriptions.get(current_description_cursor);

        // 具体的なAnimationと前後のフック
        executeDescribedAnimation(desc);

        // 現在のターゲットとなるViewを覚えさせる。
        // ユーザ記述側のメソッド内でセットされた可能性もあるので，それよりも後に行なう。
        updateTargetsIfSpecified(desc);
    }


    // ------- 個別のアニメーション実行処理 --------


    /**
     * １つのアニメーションまたはAnimationSetを実行
     */
    private void executeDescribedAnimation( final AnimationDescription desc )
    {
        // 処理させる準備
        desc.current_target_views = current_target_views;
        desc.target_activity = target_activity;
        desc.root_layout = root_layout;

        // スレッド生成のコストを省きつつ，別スレッドでアニメを開始。
        // NOTE: 別スレッドに分ける理由は待機処理などが入るから。
        exService.execute(new Runnable(){
            @Override
            public void run() {
                // 別スレッドでアニメーションを実行
                desc.carryAnimationFlowOnOtherThread();

                // 元スレッドに終了を通知
                onCurrentDescriptionFinished();
            }
        });
    }


    // ------- 複数アニメーションの制御 --------


    /**
     * アニメーションの実行が開始しているかどうか
     */
    private boolean executionAlreadyStarted()
    {
        return executing_flag;
    }


    /**
     * １つ分の詳細を扱い終わった際に呼ばれる。
     */
    protected void onCurrentDescriptionFinished()
    {
        //FWUtil.d("１ステップのアニメーション実行完了時点として，ランナーに終了を通知します。");

        // これで全部終わりか判定
        if( allDescriptionsFinished() )
        {
            //FWUtil.d("全詳細の扱いが終了しました。");

            // 全部実行終了
            target_activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    // 事後処理
                    //FWUtil.d("全詳細の事後処理を開始します。");
                    afterAllExecuted();
                }
            });

            executing_flag = false;
        }
        else
        {
            // 次の詳細へ
            execNextDescription();
        }
    }


    /**
     * 全詳細を扱い終えたかどうか判定
     */
    private boolean allDescriptionsFinished()
    {
        return ( current_description_cursor == ( descriptions.size() - 1 ) );
    }


    /**
     * 次の詳細を実行する
     */
    private void execNextDescription()
    {
        // カーソルをインクリメント
        current_description_cursor ++;

        //FWUtil.d("次の詳細へ進みます。現在のカーソルは" + current_description_cursor);

        // 開始
        execCurrentDescription();
    }


    /**
     * もし必要なら，詳細の指示通りに，アニメーション適用対象を変更する。
     */
    private void updateTargetsIfSpecified(AnimationDescription desc)
    {
        // ターゲットが変更されたか
        if( desc.new_target_views != null )
        {
            // まず全部クリア
            current_target_views.clear();

            // １個ずつ登録しなおす
            for( View target_view : desc.new_target_views )
            {
                if( target_view == null ){
                    FWUtil.e("targetとしてnullが追加されています。");
                }
                current_target_views.add( target_view );
            }

            FWUtil.d("ターゲットが変更されました。個数は" + current_target_views.size() );
        }
    }


    /**
     * 全アニメーション終了後の処理をUIスレッド上で実行
     */
    private void afterAllExecuted()
    {
        // 登録されていれば
        if( animationsFinishListener != null )
        {
            animationsFinishListener.exec();
        }
    }


}
